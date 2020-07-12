package io.github.dougkeller.spigot_misc.common;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

public class ItemStackComparator implements Comparator<ItemStack> {
    @Override
    public int compare(ItemStack a, ItemStack b) {
        if (a == null && b == null)
            return 0;
        if (a == null)
            return 1;
        if (b == null)
            return -1;

        Material aType = a.getType(), bType = b.getType();
        if (aType != bType) {
            return aType.name().compareTo(bType.name());
        }

        short aDamage = a.getDurability(), bDamage = b.getDurability();
        if (aDamage != bDamage) {
            return aDamage - bDamage;
        }

        int aAmount = a.getAmount(), bAmount = b.getAmount();
        return bAmount - aAmount;
    }

}
