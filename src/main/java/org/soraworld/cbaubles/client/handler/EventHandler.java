package org.soraworld.cbaubles.client.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.RenderItemInFrameEvent;

@SideOnly(Side.CLIENT)
public class EventHandler {

    @SubscribeEvent
    public void on(RenderItemInFrameEvent event) {
        System.out.println("RenderItemInFrameEvent");
    }
}
