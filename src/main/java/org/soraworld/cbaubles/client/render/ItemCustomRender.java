package org.soraworld.cbaubles.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
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
    private final RenderBlocks renderBlocks = RenderBlocks.getInstance();

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
        if (stack != null && stack.stackTagCompound != null) {
            NBTTagCompound bauble = stack.stackTagCompound.getCompoundTag(Constants.TAG_BAUBLE);
            int id = bauble.getInteger(Constants.TAG_ICON);
            Block blk = Block.getBlockById(id);
            if (blk != null && blk != Blocks.air) {
                switch (type) {
                    case INVENTORY:
                        renderItemIntoGUI(new ItemStack(blk));
                        break;
                    default:
                        renderBlockIntoGUI(blk);
                }
            }
        }
    }

    private void renderItemIntoGUI(ItemStack stack) {
        RenderHelper.enableGUIStandardItemLighting();
        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 0, 0);
    }

    private void renderBlockIntoGUI(Block block) {
        RenderManager.instance.itemRenderer.renderItem(mc.thePlayer, new ItemStack(block), 0, ItemRenderType.EQUIPPED);
    }
}
