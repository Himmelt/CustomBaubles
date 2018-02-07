package org.soraworld.cbaubles.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;
import org.soraworld.cbaubles.constant.Constants;

@SideOnly(Side.CLIENT)
public class ItemCustomRender implements IItemRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final RenderItem renderItem = RenderItem.getInstance();

    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        if (stack != null && stack.stackTagCompound != null) {
            NBTTagCompound bauble = stack.stackTagCompound.getCompoundTag(Constants.TAG_BAUBLE);
            Item item = Item.getItemById(bauble.getInteger(Constants.TAG_ICON));
            return item != null && item instanceof ItemBlock;
        }
        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        if (type == ItemRenderType.INVENTORY && stack != null && stack.stackTagCompound != null) {
            NBTTagCompound bauble = stack.stackTagCompound.getCompoundTag(Constants.TAG_BAUBLE);
            Item item = Item.getItemById(bauble.getInteger(Constants.TAG_ICON));
            if (item != null && item instanceof ItemBlock) {
                switch (type) {
                    case INVENTORY:
                        renderItemIntoGUI(new ItemStack(item));
                        break;
                    case EQUIPPED:
                }
            }
        }
    }

    private void renderItemIntoGUI(ItemStack stack) {
        RenderHelper.enableStandardItemLighting();
        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 0, 0);
    }

    private void renderBlockIntoGUI(Block block) {
        RenderBlocks.getInstance().renderBlockAsItem(block, 0, 1.0F);
    }
}
