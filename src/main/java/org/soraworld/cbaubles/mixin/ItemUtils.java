package org.soraworld.cbaubles.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.soraworld.cbaubles.constant.Constants;
import org.soraworld.cbaubles.items.ItemCustom;

public final class ItemUtils {

    public static ItemStack changeRenderTarget(final ItemStack itemStack) {
        if (itemStack != null && itemStack.stackTagCompound != null) {
            NBTTagCompound bauble = itemStack.stackTagCompound.getCompoundTag(Constants.TAG_CUSTOM);
            String item_id = bauble.getString(Constants.TAG_ITEM);
            if (item_id != null) {
                String[] ss = item_id.split("/");
                if (ss.length == 2) {
                    Item item = Item.getItemById(Integer.valueOf(ss[0]));
                    int meta = Integer.valueOf(ss[1]);
                    if (meta < 0 || meta > 15) meta = 0;
                    if (item != null && !(item instanceof ItemCustom)) {
                        return new ItemStack(item, itemStack.stackSize, meta);
                    }
                }
            }
        }
        return itemStack;
    }
}
