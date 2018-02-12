package org.soraworld.cbaubles.util;

import net.minecraft.item.ItemStack;
import org.soraworld.cbaubles.items.Bauble;
import org.soraworld.cbaubles.items.IItemStack;

public final class ItemUtils {

    public static ItemStack changeRenderTarget(final ItemStack itemStack) {
        if (itemStack != null) {
            Bauble bauble = ((IItemStack) (Object) itemStack).getBauble();
            if (bauble != null && bauble.getIcon() != null) return bauble.getIcon();
        }
        return itemStack;
    }
}
