package me.codalot.gui.utils;

import org.bukkit.Material;

public class MaterialUtils {

    public static boolean isAir(Material material) {
        switch (material.toString()) {
            case "LEGACY_AIR":
            case "AIR":
            case "CAVE_AIR":
            case "VOID_AIR":
                return true;
            default:
                return false;
        }
    }

    static Material getMaterial(String name) {
        try {
            return (Material) Material.class.getDeclaredField(name).get(null);
        } catch (Exception ignored) {}

        return Material.STONE;
    }

}
