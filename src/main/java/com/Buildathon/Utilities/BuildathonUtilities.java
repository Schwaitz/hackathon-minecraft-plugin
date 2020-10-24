package com.Buildathon.Utilities;

import com.Buildathon.Buildathon;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BuildathonUtilities {

    Buildathon plugin;

    public BuildathonUtilities(Buildathon plugin) {
        this.plugin = plugin;
    }

    public ItemMeta createMeta(ItemMeta meta, String name) {
        meta.setDisplayName(name);
        return meta;
    }

    public ItemMeta createMeta(ItemMeta meta, String name, String[] lore_strings) {
        ArrayList<String> lore = new ArrayList<String>(Arrays.asList(lore_strings));
        meta.setDisplayName(name);
        meta.setLore(lore);
        return meta;
    }

    public ItemStack createMetaStack(ItemStack stack, String name) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return stack;
    }

    public ItemStack createMetaStack(ItemStack stack, String name, List<String> lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }


    public static Integer randomNumber(int min, int max) {
        Random i = new Random();
        return min + i.nextInt(max - min);
    }
}
