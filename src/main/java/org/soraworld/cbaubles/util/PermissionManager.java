package org.soraworld.cbaubles.util;

import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.Bukkit;

import java.lang.reflect.Method;
import java.util.UUID;

import static org.soraworld.cbaubles.constant.Constants.LOGGER;

public class PermissionManager {

    private static PermissionManager instance;

    private boolean bukkitServer = false;

    private PermissionManager() {
        checkBukkit();
    }

    public static PermissionManager getPermissionManager() {
        if (instance == null) instance = new PermissionManager();
        return instance;
    }

    private void checkBukkit() {
        try {
            Class<?> clzBukkit = Class.forName("org.bukkit.Bukkit");
            Class<?> clzPlayer = Class.forName("org.bukkit.entity.Player");
            if (clzBukkit != null && clzPlayer != null) {
                Method md_getPlayer = clzBukkit.getDeclaredMethod("getPlayer", UUID.class);
                Method md_hasPermission = clzPlayer.getMethod("hasPermission", String.class);
                if (md_getPlayer != null && md_hasPermission != null) {
                    bukkitServer = true;
                    LOGGER.info("Found Bukkit environment. Will support Bukkit permission.");
                    return;
                }
            }
        } catch (Throwable ignored) {
        }
        bukkitServer = false;
        LOGGER.info("Not found Bukkit environment. Will not support Bukkit permission.");
    }

    public boolean hasPermission(EntityPlayer player, String perm) {
        if (perm == null || perm.isEmpty()) return true;
        if (bukkitServer) return Bukkit.getPlayer(player.getUniqueID()).hasPermission(perm);
        return player.canCommandSenderUseCommand(4, "op");
    }
}
