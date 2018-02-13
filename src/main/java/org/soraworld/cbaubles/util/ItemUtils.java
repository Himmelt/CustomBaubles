package org.soraworld.cbaubles.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import org.soraworld.cbaubles.items.Bauble;
import org.soraworld.cbaubles.items.IItemStack;

@SideOnly(Side.CLIENT)
public final class ItemUtils {

    @SideOnly(Side.CLIENT)
    public static ItemStack changeRenderTarget(final ItemStack itemStack) {
        if (itemStack != null) {
            Bauble bauble = ((IItemStack) (Object) itemStack).getBauble();
            if (bauble != null && bauble.getIcon() != null) return bauble.getIcon();
        }
        return itemStack;
    }
}
