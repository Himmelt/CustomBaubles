package org.soraworld.cbaubles.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void Init(FMLInitializationEvent event) {
        super.Init(event);
        //MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}
