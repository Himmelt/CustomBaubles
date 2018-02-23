package org.soraworld.cbaubles.mixin;

import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static org.soraworld.cbaubles.constant.Constants.LOGGER;

public class CustomMixinConfig implements IMixinConfigPlugin {

    private MixinEnvironment env;

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String target, String mixin) {
        if (env == null) {
            env = MixinEnvironment.getCurrentEnvironment();
        }
        if (env.getSide() == MixinEnvironment.Side.SERVER && target.contains("client")) {
            LOGGER.info("Mixin Skip Client Class " + mixin + " -> " + target);
            return false;
        } else {
            LOGGER.info("Mixin " + mixin + " -> " + target);
            return true;
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
