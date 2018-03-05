package org.soraworld.cbaubles.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.soraworld.cbaubles.constant.Constants;
import org.soraworld.cbaubles.items.Bauble;
import org.soraworld.cbaubles.items.IItemStack;

public final class ItemUtils {

    @SideOnly(Side.CLIENT)
    public static ItemStack changeRenderTarget(final ItemStack itemStack) {
        if (itemStack != null) {
            Bauble bauble = ((IItemStack) (Object) itemStack).getBauble();
            if (bauble != null && bauble.getIcon() != null) {
                return bauble.getIcon();
            }
        }
        return itemStack;
    }

    public static void updateBaubleToNBT(ItemStack stack, Bauble bauble) {
        if (stack != null && bauble != null) {
            if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
            stack.stackTagCompound.setTag(Constants.TAG_CUSTOM, bauble.writeToNBT(new NBTTagCompound()));
        }
    }

}
