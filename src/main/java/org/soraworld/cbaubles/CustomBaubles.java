package org.soraworld.cbaubles;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import org.soraworld.cbaubles.command.CommandBauble;
import org.soraworld.cbaubles.constant.Constants;
import org.soraworld.cbaubles.proxy.CommonProxy;

@Mod(
        modid = Constants.MOD_ID,
        name = Constants.NAME,
        version = Constants.VERSION,
        acceptedMinecraftVersions = Constants.AC_MC_VERSION
)
public class CustomBaubles {

    @SidedProxy(clientSide = "org.soraworld.cbaubles.proxy.ClientProxy", serverSide = "org.soraworld.cbaubles.proxy.ServerProxy")
    private static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        proxy.Init(event);
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBauble());
    }
}
