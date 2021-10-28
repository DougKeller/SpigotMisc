package io.github.dougkeller.spigot_misc.common;

import io.github.dougkeller.spigot_misc.mini_plugins.AutoSortChests;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DepositBox {
    public static final int DEFAULT_RADIUS = 5;
    public static final int MAX_RADIUS = 10;

    private Inventory inventory;
    private Player player;
    private Location centerPoint;
    private int radius;
    private boolean laxMode;
    private int indexStart;
    private int length;

    public DepositBox(Inventory inventory, Player player, Location centerPoint, int radius, boolean laxMode, int indexStart, int length) {
        this.inventory = inventory;
        this.player = player;
        this.centerPoint = centerPoint;
        this.radius = radius;
        this.laxMode = laxMode;
        this.indexStart = indexStart;
        this.length = length;
    }

    public static DepositBox newFromChest(Chest chest, Player player) {
        if (!isDepositBox(chest)) {
            throw new IllegalArgumentException();
        }

        Inventory inventory = chest.getInventory();
        Location centerPoint = chest.getLocation();
        Sign sign = getDepositBoxSign(chest);
        int radius = parseRadius(sign);
        boolean laxMode = parseLaxMode(sign);
        int indexStart = 0;
        int length = inventory.getContents().length;

        return new DepositBox(inventory, player, centerPoint, radius, laxMode, indexStart, length);
    }

    public static DepositBox newFromSign(Sign sign, Player player) {
        if (!isDepositBox(sign)) {
            throw new IllegalArgumentException();
        }

        Inventory inventory = player.getInventory();
        Location centerPoint = sign.getLocation();
        int radius = parseRadius(sign);
        boolean laxMode = parseLaxMode(sign);
        int indexStart = AutoSortChests.MAIN_INVENTORY_START;
        int length = AutoSortChests.MAIN_INVENTORY_SIZE;

        return new DepositBox(inventory, player, centerPoint, radius, laxMode, indexStart, length);
    }

    public static boolean isDepositBox(Sign sign) {
        String[] lines = sign.getLines();
        return lines[0].toLowerCase().matches("\\[deposit box]");
    }

    protected static int parseRadius(Sign sign) {
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

    protected static boolean parseLaxMode(Sign sign) {
        return !sign.getLine(2).toLowerCase().equals("strict");
    }

    public static boolean isDepositBox(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        boolean isSingleWoodenChest = holder instanceof Chest;
        if (!isSingleWoodenChest) {
            return false;
        }

        Chest chest = (Chest) holder;
        return isDepositBox(chest);
    }

    public static boolean isDepositBox(Chest chest) {
        return getDepositBoxSign(chest) != null;
    }

    public static Sign getDepositBoxSign(Chest chest) {
        Block chestBlock = chest.getBlock();
        Sign sign;

        for (int offsetY = -2; offsetY <= 1; ++offsetY) {
            sign = findSignAt(chestBlock, 0, offsetY, 0);
            if (sign != null) {
                return sign;
            }
        }
        for (int offsetX = -1; offsetX <= 1; ++offsetX) {
            sign = findSignAt(chestBlock, offsetX, 0, 0);
            if (sign != null) {
                return sign;
            }
        }
        for (int offsetZ = -1; offsetZ <= 1; ++offsetZ) {
            sign = findSignAt(chestBlock, 0, 0, offsetZ);
            if (sign != null) {
                return sign;
            }
        }

        return null;
    }

    private static Sign findSignAt(Block chestBlock, int offsetX, int offsetY, int offsetZ) {
        Block blockAtFace = chestBlock.getRelative(offsetX, offsetY, offsetZ);
        BlockState blockState = blockAtFace.getState();
        boolean isSign = blockState instanceof Sign;
        if (!isSign) {
            return null;
        }

        Sign sign = (Sign) blockState;
        if (isDepositBox(sign)) {
            return sign;
        }

        return null;
    }

    public static Chest getAutoSortableChest(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        boolean isSingleWoodenChest = holder instanceof Chest;
        if (!isSingleWoodenChest) {
            return null;
        }

        Chest chest = (Chest) holder;
        if (getDepositBoxSign(chest) == null) {
            return null;
        }

        return chest;
    }

    public void sort() {
        ItemStack[] contents = inventory.getContents();

        int totalSorted = 0;
        int totalFailed = 0;

        for (int i = indexStart; i < indexStart + length; ++i) {
            ItemStack itemStack = contents[i];
            if (itemStack == null) {
                continue;
            }

            if (store(itemStack)) {
                contents[i] = null;
                totalSorted++;
            } else {
                totalFailed++;
            }
        }

        if (totalSorted > 0) {
            String noun = totalSorted == 1 ? "item" : "items";
            player.sendMessage(String.format(ChatColor.GREEN + "Deposited %d %s.", totalSorted, noun));
        }

        if (totalFailed > 0) {
            String noun = totalFailed == 1 ? "item" : "items";
            player.sendMessage(String.format(ChatColor.GOLD + "Could not deposit %d %s.", totalFailed, noun));
        }

        if (totalSorted == 0 && totalFailed == 0 && inventory instanceof PlayerInventory) {
            player.sendMessage(String.format(ChatColor.GOLD + "No items to deposit!"));
        }

        inventory.setContents(contents);
    }

    private boolean store(ItemStack itemStack) {
        ArrayList<Chest> targetChests = getTargetChests();
        HashMap<Chest, Integer> scores = getScores(itemStack, targetChests);

        targetChests.sort(new Comparator<Chest>() {
            @Override
            public int compare(Chest o1, Chest o2) {
                return scores.get(o2) - scores.get(o1);
            }
        });

        for (Chest targetChest : targetChests) {
            if (scores.get(targetChest) == 0) {
                return false;
            }

            Inventory targetInventory = targetChest.getInventory();
            HashMap<Integer, ItemStack> excessItemStacks = targetInventory.addItem(itemStack);
            int remaining = excessItemStacks.isEmpty() ? 0 : excessItemStacks.get(0).getAmount();
            itemStack.setAmount(remaining);

            if (itemStack.getAmount() == 0) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<Chest> getTargetChests() {
        ArrayList<Chest> targetChests = new ArrayList<>();
        World world = centerPoint.getWorld();

        for (int x = centerPoint.getBlockX() - radius; x <= centerPoint.getBlockX() + radius; ++x) {
            for (int y = centerPoint.getBlockY() - radius; y <= centerPoint.getBlockY() + radius; ++y) {
                for (int z = centerPoint.getBlockZ() - radius; z <= centerPoint.getBlockZ() + radius; ++z) {
                    Location targetLocation = new Location(world, x, y, z);
                    Block targetBlock = targetLocation.getBlock();
                    BlockState targetBlockState = targetBlock.getState();
                    if (targetBlockState instanceof Chest && !isDepositBox((Chest) targetBlockState)) {
                        targetChests.add((Chest) targetBlockState);
                    }
                }
            }
        }

        return targetChests;
    }

    private HashMap<Chest, Integer> getScores(ItemStack itemStack, ArrayList<Chest> targetChests) {
        HashMap<Chest, Integer> scores = new HashMap<>();

        for (Chest targetChest : targetChests) {
            scores.put(targetChest, getScore(itemStack, targetChest));
        }

        return scores;
    }

    private int getScore(ItemStack itemStack, Chest targetChest) {
        int score = 0;

        Inventory targetInventory = targetChest.getInventory();
        ItemStack[] targetStacks = targetInventory.getContents();
        for (ItemStack targetStack : targetStacks) {
            if (targetStack == null) {
                continue;
            }

            if (ItemStackComparator.isSameItem(itemStack, targetStack)) {
                score += 3025; // 55^2
            } else if (laxMode && ItemStackComparator.isSimilarItem(itemStack, targetStack)) {
                score += 55; // 55^1
            }
        }

        return score;
    }
}
