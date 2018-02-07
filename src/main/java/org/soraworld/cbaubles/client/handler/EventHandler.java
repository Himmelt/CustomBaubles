package org.soraworld.cbaubles.client.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

@SideOnly(Side.CLIENT)
public class EventHandler {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void on(RenderHandEvent event) {

        //mc.thePlayer.setCurrentItemOrArmor(0, new ItemStack(Items.ender_eye));
        System.out.println(event);
    }

    @SubscribeEvent
    public void on(RenderWorldLastEvent event) {
        System.out.println(event);
    }
}
