package org.soraworld.cbaubles.mixin.client.renderer.entity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.soraworld.cbaubles.constant.Constants;
import org.soraworld.cbaubles.items.ItemCustom;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem extends Render {

    @Shadow
    public float zLevel;
    @Shadow
    public boolean renderWithColor;
    @Final
    @Shadow
    private static ResourceLocation RES_ITEM_GLINT;

    @Shadow
    public void renderItemIntoGUI(FontRenderer p_77015_1_, TextureManager p_77015_2_, ItemStack p_77015_3_, int p_77015_4_, int p_77015_5_, boolean renderEffect) {
    }

    @Shadow
    private void renderGlint(int p_77018_1_, int p_77018_2_, int p_77018_3_, int p_77018_4_, int p_77018_5_) {
    }

    /**
     * Render the item's icon or block into the GUI, including the glint effect.
     *
     * @author Himmelt
     * @reason change render target
     */
    @Overwrite
    public void renderItemAndEffectIntoGUI(FontRenderer fontRenderer, TextureManager textureManager, final ItemStack final_item, int p_82406_4_, int p_82406_5_) {
        ItemStack itemStack = final_item;

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
                        itemStack = new ItemStack(item, meta);
                    }
                }
            }
        }

        if (itemStack != null) {
            this.zLevel += 50.0F;

            try {
                if (!ForgeHooksClient.renderInventoryItem(this.field_147909_c, textureManager, itemStack, renderWithColor, zLevel, (float) p_82406_4_, (float) p_82406_5_)) {
                    this.renderItemIntoGUI(fontRenderer, textureManager, itemStack, p_82406_4_, p_82406_5_, true);
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.addCrashSection("Item Type", String.valueOf(itemStack.getItem()));
                crashreportcategory.addCrashSection("Item Aux", String.valueOf(itemStack.getItemDamage()));
                crashreportcategory.addCrashSection("Item NBT", String.valueOf(itemStack.getTagCompound()));
                crashreportcategory.addCrashSection("Item Foil", String.valueOf(itemStack.hasEffect()));
                throw new ReportedException(crashreport);
            }

            // Forge: Bugfix, Move this to a per-render pass, modders must handle themselves
            if (false && itemStack.hasEffect()) {
                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                textureManager.bindTexture(RES_ITEM_GLINT);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
                this.renderGlint(p_82406_4_ * 431278612 + p_82406_5_ * 32178161, p_82406_4_ - 2, p_82406_5_ - 2, 20, 20);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }

            this.zLevel -= 50.0F;
        }
    }
}
