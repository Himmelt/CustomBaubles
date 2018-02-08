package org.soraworld.cbaubles;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.List;

public class CustomTweak implements ITweaker {

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        // This statement must appear first, failing to include
        // it will cause a runtime error
        MixinBootstrap.init();

        // Retrieves the DEFAULT mixin environment and registers
        // the config file
        Mixins.addConfiguration("mixins.cbaubles.json");
        System.out.println("acceptOptions:" + gameDir + assetsDir + profile);
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        System.out.println("injectIntoClassLoader:" + classLoader);
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
