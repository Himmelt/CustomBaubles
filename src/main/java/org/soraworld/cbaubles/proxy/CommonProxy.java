package org.soraworld.cbaubles.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;
import org.soraworld.cbaubles.constant.Constants;
import org.soraworld.cbaubles.handler.EventBusHandler;
import org.soraworld.cbaubles.handler.FMLEventHandler;
import org.soraworld.cbaubles.items.ItemCustom;

public abstract class CommonProxy {

    private static final ItemCustom item_custom = new ItemCustom(Constants.ITEM_CUSTOM_BAUBLE);

    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerItem(item_custom, item_custom.getRegisterName());
    }

    public void Init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventBusHandler());
        FMLCommonHandler.instance().bus().register(new FMLEventHandler());
    }
}
