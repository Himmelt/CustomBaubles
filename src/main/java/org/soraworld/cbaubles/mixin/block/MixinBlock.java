package org.soraworld.cbaubles.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Block.class)
public abstract class MixinBlock {

    /**
     * @author Himmelt
     * @reason Test
     */
    @Overwrite
    public void onBlockDestroyedByPlayer(World world, int p_149664_2_, int p_149664_3_, int p_149664_4_, int p_149664_5_) {
        System.out.println("onBlockDestroyedByPlayer:" + world);
    }

}
