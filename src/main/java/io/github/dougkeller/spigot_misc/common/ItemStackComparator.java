package io.github.dougkeller.spigot_misc.common;

import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

public class ItemStackComparator implements Comparator<ItemStack> {
    public static boolean isSimilarItem(ItemStack a, ItemStack b) {
        return a.getType().equals(b.getType()) || a.getData().equals(b.getData());
    }

    @Override
    public int compare(ItemStack a, ItemStack b) {
        if (a == null && b == null)
            return 0;
        if (a == null)
            return 1;
        if (b == null)
            return -1;

        if (!ItemStackComparator.isSameItem(a, b)) {
            if (a.getType() == b.getType()) {
                return a.getData().toString().compareTo(b.getData().toString());
            }

            return a.getType().compareTo(b.getType());
        }

        int aAmount = a.getAmount(), bAmount = b.getAmount();
        if (aAmount != bAmount) {
            return bAmount - aAmount;
        }

        short aDamage = a.getDurability(), bDamage = b.getDurability();
        return aDamage - bDamage;
    }

    public static boolean isSameItem(ItemStack a, ItemStack b) {
        return a.isSimilar(b);
    }
}
