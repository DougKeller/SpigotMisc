package io.github.dougkeller.spigot_misc.common;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InventorySorter {
    private Inventory inventory;
    private int indexStart;
    private int length;

    public InventorySorter(Inventory inventory) {
        this(inventory, 0, inventory.getContents().length);
    }

    public InventorySorter(Inventory inventory, int indexStart, int length) {
        this.inventory = inventory;
        this.indexStart = indexStart;
        this.length = length;
    }

    public void sort() {
        System.out.println("SORT");
        ItemStack[] contents = inventory.getContents();
        ItemStack[] sortedContents = sortContents(contents);
        inventory.setContents(sortedContents);
    }

    private ItemStack[] sortContents(ItemStack[] contents) {
        List<ItemStack> list = Arrays.asList(contents);

        List<ItemStack> sortableList = list.subList(indexStart, indexStart + length);
        Collections.sort(sortableList, new ItemStackComparator());

        for (int i = 0; i < length; ++i) {
            list.set(i + indexStart, sortableList.get(i));
        }

        return (ItemStack[]) list.toArray();
    }
}
