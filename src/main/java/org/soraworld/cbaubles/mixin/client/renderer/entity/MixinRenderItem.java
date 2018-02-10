package org.soraworld.cbaubles.mixin.client.renderer.entity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.soraworld.cbaubles.util.ItemUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem extends Render {

    @Shadow
    public float zLevel;
    @Shadow
    private Random random;
    @Shadow
    public static boolean renderInFrame;
    @Shadow
    public boolean renderWithColor;
    @Shadow
    private RenderBlocks renderBlocksRi;
    @Final
    @Shadow
    private static ResourceLocation RES_ITEM_GLINT;

    @Shadow
    public abstract boolean shouldBob();

    @Shadow
    public abstract byte getMiniBlockCount(ItemStack stack, byte original);

    @Shadow
    public abstract void renderItemIntoGUI(FontRenderer p_77015_1_, TextureManager p_77015_2_, ItemStack p_77015_3_, int p_77015_4_, int p_77015_5_, boolean renderEffect);

    @Shadow
    protected abstract void renderGlint(int p_77018_1_, int p_77018_2_, int p_77018_3_, int p_77018_4_, int p_77018_5_);

    @Shadow
    public abstract byte getMiniItemCount(ItemStack stack, byte original);

    @Shadow
    public abstract boolean shouldSpreadItems();

    /**
     * Render the item's icon or block into the GUI, including the glint effect.
     *
     * @author Himmelt
     * @reason change inventory item rendering
     */
    @Overwrite
    public void renderItemAndEffectIntoGUI(FontRenderer fontRenderer, TextureManager textureManager, final ItemStack final_item, int p_82406_4_, int p_82406_5_) {

        ItemStack itemStack = ItemUtils.changeRenderTarget(final_item);

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

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     *
     * @author Himmelt
     * @reason change entity item rendering
     */
    @Overwrite
    public void doRender(EntityItem entityItem, double x, double y, double z, float p_76986_8_, float p_76986_9_) {

        ItemStack itemstack = ItemUtils.changeRenderTarget(entityItem.getEntityItem());

        if (itemstack.getItem() != null) {
            this.bindEntityTexture(entityItem);
            TextureUtil.func_152777_a(false, false, 1.0F);
            this.random.setSeed(187L);
            GL11.glPushMatrix();
            float f2 = shouldBob() ? MathHelper.sin(((float) entityItem.age + p_76986_9_) / 10.0F + entityItem.hoverStart) * 0.1F + 0.1F : 0F;
            float f3 = (((float) entityItem.age + p_76986_9_) / 20.0F + entityItem.hoverStart) * (180F / (float) Math.PI);
            byte b0 = 1;

            if (entityItem.getEntityItem().stackSize > 1) {
                b0 = 2;
            }

            if (entityItem.getEntityItem().stackSize > 5) {
                b0 = 3;
            }

            if (entityItem.getEntityItem().stackSize > 20) {
                b0 = 4;
            }

            if (entityItem.getEntityItem().stackSize > 40) {
                b0 = 5;
            }

            b0 = getMiniBlockCount(itemstack, b0);

            GL11.glTranslatef((float) x, (float) y + f2, (float) z);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            float f6;
            float f7;
            int k;

            if (ForgeHooksClient.renderEntityItem(entityItem, itemstack, f2, f3, random, renderManager.renderEngine, field_147909_c, b0)) {
            } else // Code Style break here to prevent the patch from editing this line
                if (itemstack.getItemSpriteNumber() == 0 && itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
                    Block block = Block.getBlockFromItem(itemstack.getItem());
                    GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);

                    if (renderInFrame) {
                        GL11.glScalef(1.25F, 1.25F, 1.25F);
                        GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                    }

                    float f9 = 0.25F;
                    k = block.getRenderType();

                    if (k == 1 || k == 19 || k == 12 || k == 2) {
                        f9 = 0.5F;
                    }

                    if (block.getRenderBlockPass() > 0) {
                        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                        GL11.glEnable(GL11.GL_BLEND);
                        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    }

                    GL11.glScalef(f9, f9, f9);

                    for (int l = 0; l < b0; ++l) {
                        GL11.glPushMatrix();

                        if (l > 0) {
                            f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
                            f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
                            float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
                            GL11.glTranslatef(f6, f7, f8);
                        }

                        this.renderBlocksRi.renderBlockAsItem(block, itemstack.getItemDamage(), 1.0F);
                        GL11.glPopMatrix();
                    }

                    if (block.getRenderBlockPass() > 0) {
                        GL11.glDisable(GL11.GL_BLEND);
                    }
                } else {
                    float f5;

                    if (/*itemstack.getItemSpriteNumber() == 1 &&*/ itemstack.getItem().requiresMultipleRenderPasses()) {
                        if (renderInFrame) {
                            GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                            GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                        } else {
                            GL11.glScalef(0.5F, 0.5F, 0.5F);
                        }

                        for (int j = 0; j < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++j) {
                            this.random.setSeed(187L);
                            IIcon iicon1 = itemstack.getItem().getIcon(itemstack, j);

                            if (this.renderWithColor) {
                                k = itemstack.getItem().getColorFromItemStack(itemstack, j);
                                f5 = (float) (k >> 16 & 255) / 255.0F;
                                f6 = (float) (k >> 8 & 255) / 255.0F;
                                f7 = (float) (k & 255) / 255.0F;
                                GL11.glColor4f(f5, f6, f7, 1.0F);
                                this.renderDroppedItem(entityItem, iicon1, b0, p_76986_9_, f5, f6, f7, j);
                            } else {
                                this.renderDroppedItem(entityItem, iicon1, b0, p_76986_9_, 1.0F, 1.0F, 1.0F, j);
                            }
                        }
                    } else {
                        if (itemstack != null && itemstack.getItem() instanceof ItemCloth) {
                            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                            GL11.glEnable(GL11.GL_BLEND);
                            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                        }

                        if (renderInFrame) {
                            GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                            GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                        } else {
                            GL11.glScalef(0.5F, 0.5F, 0.5F);
                        }

                        IIcon iicon = itemstack.getIconIndex();

                        if (this.renderWithColor) {
                            int i = itemstack.getItem().getColorFromItemStack(itemstack, 0);
                            float f4 = (float) (i >> 16 & 255) / 255.0F;
                            f5 = (float) (i >> 8 & 255) / 255.0F;
                            f6 = (float) (i & 255) / 255.0F;
                            this.renderDroppedItem(entityItem, iicon, b0, p_76986_9_, f4, f5, f6, 0);
                        } else {
                            this.renderDroppedItem(entityItem, iicon, b0, p_76986_9_, 1.0F, 1.0F, 1.0F, 0);
                        }

                        if (itemstack != null && itemstack.getItem() instanceof ItemCloth) {
                            GL11.glDisable(GL11.GL_BLEND);
                        }
                    }
                }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            this.bindEntityTexture(entityItem);
            TextureUtil.func_147945_b();
        }
    }

    /**
     * Renders a dropped item
     *
     * @author Himmelt
     * @reason change inventory item rendering
     */
    @Overwrite(remap = false)
    private void renderDroppedItem(EntityItem entityItem, IIcon icon, int p_77020_3_, float p_77020_4_, float p_77020_5_, float p_77020_6_, float p_77020_7_, int pass) {
        Tessellator tessellator = Tessellator.instance;
        ItemStack itemStack = ItemUtils.changeRenderTarget(entityItem.getEntityItem());


        if (icon == null) {
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation resourcelocation = texturemanager.getResourceLocation(itemStack.getItemSpriteNumber());
            icon = ((TextureMap) texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
        }

        float f14 = icon.getMinU();
        float f15 = icon.getMaxU();
        float f4 = icon.getMinV();
        float f5 = icon.getMaxV();
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        float f10;

        if (this.renderManager.options.fancyGraphics) {
            GL11.glPushMatrix();

            if (renderInFrame) {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            } else {
                GL11.glRotatef((((float) entityItem.age + p_77020_4_) / 20.0F + entityItem.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            }

            float f9 = 0.0625F;
            f10 = 0.021875F;
            //ItemStack itemStack = entityItem.getEntityItem();
            int j = itemStack.stackSize;
            byte b0;

            if (j < 2) {
                b0 = 1;
            } else if (j < 16) {
                b0 = 2;
            } else if (j < 32) {
                b0 = 3;
            } else {
                b0 = 4;
            }

            b0 = getMiniItemCount(itemStack, b0);

            GL11.glTranslatef(-f7, -f8, -((f9 + f10) * (float) b0 / 2.0F));

            for (int k = 0; k < b0; ++k) {
                // Makes items offset when in 3D, like when in 2D, looks much better. Considered a vanilla bug...
                if (k > 0 && shouldSpreadItems()) {
                    float x = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    float y = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    float z = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    GL11.glTranslatef(x, y, f9 + f10);
                } else {
                    GL11.glTranslatef(0f, 0f, f9 + f10);
                }

                if (itemStack.getItemSpriteNumber() == 0) {
                    this.bindTexture(TextureMap.locationBlocksTexture);
                } else {
                    this.bindTexture(TextureMap.locationItemsTexture);
                }

                GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, f15, f4, f14, f5, icon.getIconWidth(), icon.getIconHeight(), f9);

                if (itemStack.hasEffect(pass)) {
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    this.renderManager.renderEngine.bindTexture(RES_ITEM_GLINT);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                    float f11 = 0.76F;
                    GL11.glColor4f(0.5F * f11, 0.25F * f11, 0.8F * f11, 1.0F);
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    GL11.glPushMatrix();
                    float f12 = 0.125F;
                    GL11.glScalef(f12, f12, f12);
                    float f13 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(f13, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(f12, f12, f12);
                    f13 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-f13, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                }
            }

            GL11.glPopMatrix();
        } else {
            for (int l = 0; l < p_77020_3_; ++l) {
                GL11.glPushMatrix();

                if (l > 0) {
                    f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(f10, f16, f17);
                }

                if (!renderInFrame) {
                    GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                }

                GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV((double) (0.0F - f7), (double) (0.0F - f8), 0.0D, (double) f14, (double) f5);
                tessellator.addVertexWithUV((double) (f6 - f7), (double) (0.0F - f8), 0.0D, (double) f15, (double) f5);
                tessellator.addVertexWithUV((double) (f6 - f7), (double) (1.0F - f8), 0.0D, (double) f15, (double) f4);
                tessellator.addVertexWithUV((double) (0.0F - f7), (double) (1.0F - f8), 0.0D, (double) f14, (double) f4);
                tessellator.draw();
                GL11.glPopMatrix();
            }
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     *
     * @author Himmelt
     * @reason change inventory item rendering
     */
    @Overwrite
    protected ResourceLocation getEntityTexture(EntityItem entityItem) {
        ItemStack itemStack = ItemUtils.changeRenderTarget(entityItem.getEntityItem());
        return this.renderManager.renderEngine.getResourceLocation(itemStack.getItemSpriteNumber());
    }

}
