package io.github.dougkeller.spigot_misc.common;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class AutoSortableInventory {
    public static final int DEFAULT_RADIUS = 3;
    public static final int MAX_RADIUS = 6;

    private Inventory inventory;
    private Chest chest;
    private Sign sign;
    private int radius;

    public AutoSortableInventory(Inventory inventory) {
        if (!isAutoSortable(inventory)) {
            throw new IllegalArgumentException();
        }

        this.inventory = inventory;
        this.chest = getAutoSortableChest(inventory);
        this.sign = getAutoSortableSign(chest);
        this.radius = parseRadius();
        syncSignText();
    }

    public static boolean isAutoSortable(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        boolean isSingleWoodenChest = holder instanceof Chest;
        if (!isSingleWoodenChest) {
            return false;
        }

        Chest chest = (Chest) holder;
        return isAutoSortable(chest);
    }

    public static Chest getAutoSortableChest(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        boolean isSingleWoodenChest = holder instanceof Chest;
        if (!isSingleWoodenChest) {
            return null;
        }

        Chest chest = (Chest) holder;
        if (getAutoSortableSign(chest) == null) {
            return null;
        }

        return chest;
    }

    public void sort() {
        Inventory inventory = chest.getInventory();
        ItemStack[] contents = inventory.getContents();

        for (int i = 0; i < contents.length; ++i) {
            ItemStack itemStack = contents[i];
            if (itemStack == null) {
                continue;
            }

            store(itemStack);
            if (itemStack.getAmount() == 0) {
                contents[i] = null;
            }
        }

        inventory.setContents(contents);
    }

    private void store(ItemStack itemStack) {
        for (Chest targetChest : getTargetChests()) {
            Inventory targetInventory = targetChest.getInventory();
            if (!targetInventory.containsAtLeast(itemStack, 1)) {
                continue;
            }

            HashMap<Integer, ItemStack> excessItemStacks = targetInventory.addItem(itemStack);
            int remaining = excessItemStacks.isEmpty() ? 0 : excessItemStacks.get(0).getAmount();
            itemStack.setAmount(remaining);

            if (itemStack.getAmount() == 0) {
                return;
            }
        }
    }

    public ArrayList<Chest> getTargetChests() {
        ArrayList<Chest> targetChests = new ArrayList<>();
        Location center = chest.getLocation();
        World world = center.getWorld();

        for (int x = center.getBlockX() - radius; x <= center.getBlockX() + radius; ++x) {
            for (int y = center.getBlockY() - radius; y <= center.getBlockY() + radius; ++y) {
                for (int z = center.getBlockZ() - radius; z <= center.getBlockZ() + radius; ++z) {
                    Location targetLocation = new Location(world, x, y, z);
                    Block targetBlock = targetLocation.getBlock();
                    BlockState targetBlockState = targetBlock.getState();
                    if (targetBlockState instanceof Chest && !isAutoSortable((Chest) targetBlockState)) {
                        targetChests.add((Chest) targetBlockState);
                    }
                }
            }
        }

        return targetChests;
    }

    public static boolean isAutoSortable(Chest chest) {
        return getAutoSortableSign(chest) != null;
    }

    public static Sign getAutoSortableSign(Chest chest) {
        Block chestBlock = chest.getBlock();

        for (int offsetX = -1; offsetX <= 1; ++offsetX) {
            for (int offsetY = -1; offsetY <= 1; ++offsetY) {
                for (int offsetZ = -1; offsetZ <= 1; ++offsetZ) {
                    Block blockAtFace = chestBlock.getRelative(offsetX, offsetY, offsetZ);
                    BlockState blockState = blockAtFace.getState();
                    boolean isSign = blockState instanceof Sign;
                    if (!isSign) {
                        continue;
                    }

                    Sign sign = (Sign) blockState;
                    if (isAutoSortable(sign)) {
                        return sign;
                    }
                }
            }
        }

        return null;
    }

    public static boolean isAutoSortable(Sign sign) {
        String[] lines = sign.getLines();
        return lines[0].toLowerCase().matches("\\[autosort]");
    }

    private int parseRadius() {
        String radiusLine = sign.getLines()[1];
        if (!radiusLine.matches("^\\d+$")) {
            sign.setLine(1, String.valueOf(DEFAULT_RADIUS));
            return DEFAULT_RADIUS;
        }

        int parsed = Integer.parseInt(radiusLine);
        if (parsed > MAX_RADIUS) {
            sign.setLine(1, String.valueOf(MAX_RADIUS));
            return MAX_RADIUS;
        }

        return parsed;
    }

    private void syncSignText() {
        sign.setLine(2, String.format("Default: %d", DEFAULT_RADIUS));
        sign.setLine(2, String.format("Max: %d", MAX_RADIUS));
    }
}
