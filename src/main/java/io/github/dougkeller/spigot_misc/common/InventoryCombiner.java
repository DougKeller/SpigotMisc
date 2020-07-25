package io.github.dougkeller.spigot_misc.common;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryCombiner {
    private Inventory inventory;

    public InventoryCombiner(Inventory inventory) {
        this.inventory = inventory;
    }

    public void combine() {
        ItemStack[] contents = inventory.getContents();
        ItemStack[] combinedContents = combineContents(contents);
        inventory.setContents(combinedContents);
    }

    private ItemStack[] combineContents(ItemStack[] contents) {
        for (int i = 0; i < contents.length; i++) {
            ItemStack a = contents[i];
            if (a == null) {
                continue;
            }

            for (int j = i + 1; j < contents.length; j++) {
                ItemStack b = contents[j];
                if (b == null || !ItemStackComparator.isSameItem(a, b)) {
                    continue;
                }

                int maxStackSize = a.getMaxStackSize();
                int aAmount = a.getAmount();
                if (aAmount >= maxStackSize) {
                    continue;
                }

                int bAmount = b.getAmount();
                int amountToCombine = Math.min(maxStackSize - aAmount, bAmount);
                a.setAmount(aAmount + amountToCombine);
                b.setAmount(bAmount - amountToCombine);

                if (b.getAmount() == 0) {
                    contents[j] = null;
                }
            }
        }

        return contents;
    }
}
