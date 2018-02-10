package com.azanor.baubles.api;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import java.lang.reflect.Method;

/**
 * @author Azanor
 */
public class BaublesApi {
    private static Method getBaubles;

    /**
     * Retrieves the baubles inventory for the supplied player
     */
    public static IInventory getBaubles(EntityPlayer player) {
        IInventory inventory = null;

        try {
            if (getBaubles == null) {
                Class<?> fake = Class.forName("com.azanor.baubles.common.lib.PlayerHandler");
                getBaubles = fake.getMethod("getPlayerBaubles", EntityPlayer.class);
            }
            inventory = (IInventory) getBaubles.invoke(null, player);
        } catch (Exception ex) {
            FMLLog.warning("[Baubles API] Could not invoke baubles.common.lib.PlayerHandler method getPlayerBaubles");
        }
        return inventory;
    }

}
