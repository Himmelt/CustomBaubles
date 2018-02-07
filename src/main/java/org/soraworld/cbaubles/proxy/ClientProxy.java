package org.soraworld.cbaubles.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import org.soraworld.cbaubles.client.handler.EventHandler;
import org.soraworld.cbaubles.client.render.ItemCustomRender;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MinecraftForgeClient.registerItemRenderer(item_custom, new ItemCustomRender());
    }

    @Override
    public void Init(FMLInitializationEvent event) {
        super.Init(event);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}
