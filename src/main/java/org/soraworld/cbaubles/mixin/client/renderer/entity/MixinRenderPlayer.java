package org.soraworld.cbaubles.mixin.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import org.soraworld.cbaubles.mixin.ItemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity {

    @Shadow
    public ModelBiped modelBipedMain;

    public MixinRenderPlayer(ModelBase modelBase, float shadowSize) {
        super(modelBase, shadowSize);
    }

    /**
     * @author Himmelt
     * @reason change 3rd-person render target
     */
    @Overwrite
    protected void renderEquippedItems(AbstractClientPlayer clientPlayer, float slot) {
        Object instance = this;
        RenderPlayerEvent.Specials.Pre event = new RenderPlayerEvent.Specials.Pre(clientPlayer, (RenderPlayer) instance, slot);
        if (MinecraftForge.EVENT_BUS.post(event)) return;
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        super.renderEquippedItems(clientPlayer, slot);
        super.renderArrowsStuckInEntity(clientPlayer, slot);

        // custom
        ItemStack itemstack = ItemUtils.changeRenderTarget(clientPlayer.inventory.armorItemInSlot(3));

        if (itemstack != null && event.renderHelmet) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedHead.postRender(0.0625F);
            float f1;

            if (itemstack.getItem() instanceof ItemBlock) {
                IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, ItemRenderType.EQUIPPED);
                boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(ItemRenderType.EQUIPPED, itemstack, ItemRendererHelper.BLOCK_3D));

                if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
                    f1 = 0.625F;
                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(f1, -f1, -f1);
                }

                this.renderManager.itemRenderer.renderItem(clientPlayer, itemstack, 0);
            } else if (itemstack.getItem() == Items.skull) {
                f1 = 1.0625F;
                GL11.glScalef(f1, -f1, -f1);
                GameProfile gameprofile = null;

                if (itemstack.hasTagCompound()) {
                    NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                        gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
                    } else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner"))) {
                        gameprofile = new GameProfile(null, nbttagcompound.getString("SkullOwner"));
                    }
                }

                TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack.getItemDamage(), gameprofile);
            }

            GL11.glPopMatrix();
        }

        float f2;

        if (clientPlayer.getCommandSenderName().equals("deadmau5") && clientPlayer.func_152123_o()) {
            this.bindTexture(clientPlayer.getLocationSkin());

            for (int j = 0; j < 2; ++j) {
                float f9 = clientPlayer.prevRotationYaw + (clientPlayer.rotationYaw - clientPlayer.prevRotationYaw) * slot - (clientPlayer.prevRenderYawOffset + (clientPlayer.renderYawOffset - clientPlayer.prevRenderYawOffset) * slot);
                float f10 = clientPlayer.prevRotationPitch + (clientPlayer.rotationPitch - clientPlayer.prevRotationPitch) * slot;
                GL11.glPushMatrix();
                GL11.glRotatef(f9, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f10, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (float) (j * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-f10, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-f9, 0.0F, 1.0F, 0.0F);
                f2 = 1.3333334F;
                GL11.glScalef(f2, f2, f2);
                this.modelBipedMain.renderEars(0.0625F);
                GL11.glPopMatrix();
            }
        }

        boolean flag = clientPlayer.func_152122_n();
        flag = event.renderCape && flag;
        float f4;

        if (flag && !clientPlayer.isInvisible() && !clientPlayer.getHideCape()) {
            this.bindTexture(clientPlayer.getLocationCape());
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d3 = clientPlayer.field_71091_bM + (clientPlayer.field_71094_bP - clientPlayer.field_71091_bM) * (double) slot - (clientPlayer.prevPosX + (clientPlayer.posX - clientPlayer.prevPosX) * (double) slot);
            double d4 = clientPlayer.field_71096_bN + (clientPlayer.field_71095_bQ - clientPlayer.field_71096_bN) * (double) slot - (clientPlayer.prevPosY + (clientPlayer.posY - clientPlayer.prevPosY) * (double) slot);
            double d0 = clientPlayer.field_71097_bO + (clientPlayer.field_71085_bR - clientPlayer.field_71097_bO) * (double) slot - (clientPlayer.prevPosZ + (clientPlayer.posZ - clientPlayer.prevPosZ) * (double) slot);
            f4 = clientPlayer.prevRenderYawOffset + (clientPlayer.renderYawOffset - clientPlayer.prevRenderYawOffset) * slot;
            double d1 = (double) MathHelper.sin(f4 * (float) Math.PI / 180.0F);
            double d2 = (double) (-MathHelper.cos(f4 * (float) Math.PI / 180.0F));
            float f5 = (float) d4 * 10.0F;

            if (f5 < -6.0F) {
                f5 = -6.0F;
            }

            if (f5 > 32.0F) {
                f5 = 32.0F;
            }

            float f6 = (float) (d3 * d1 + d0 * d2) * 100.0F;
            float f7 = (float) (d3 * d2 - d0 * d1) * 100.0F;

            if (f6 < 0.0F) {
                f6 = 0.0F;
            }

            float f8 = clientPlayer.prevCameraYaw + (clientPlayer.cameraYaw - clientPlayer.prevCameraYaw) * slot;
            f5 += MathHelper.sin((clientPlayer.prevDistanceWalkedModified + (clientPlayer.distanceWalkedModified - clientPlayer.prevDistanceWalkedModified) * slot) * 6.0F) * 32.0F * f8;

            if (clientPlayer.isSneaking()) {
                f5 += 25.0F;
            }

            GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }

        // custom
        ItemStack itemstack1 = ItemUtils.changeRenderTarget(clientPlayer.inventory.getCurrentItem());

        if (itemstack1 != null && event.renderItem) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            if (clientPlayer.fishEntity != null) {
                itemstack1 = new ItemStack(Items.stick);
            }

            EnumAction enumaction = null;

            if (clientPlayer.getItemInUseCount() > 0) {
                enumaction = itemstack1.getItemUseAction();
            }

            net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack1, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack1, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

            if (is3D || itemstack1.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack1.getItem()).getRenderType())) {
                f2 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f2 *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-f2, -f2, f2);
            } else if (itemstack1.getItem() == Items.bow) {
                f2 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else if (itemstack1.getItem().isFull3D()) {
                f2 = 0.625F;

                if (itemstack1.getItem().shouldRotateAroundWhenRendering()) {
                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                if (clientPlayer.getItemInUseCount() > 0 && enumaction == EnumAction.block) {
                    GL11.glTranslatef(0.05F, 0.0F, -0.1F);
                    GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else {
                f2 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f2, f2, f2);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            float f3;
            int k;
            float f12;

            if (itemstack1.getItem().requiresMultipleRenderPasses()) {
                for (k = 0; k < itemstack1.getItem().getRenderPasses(itemstack1.getItemDamage()); ++k) {
                    int i = itemstack1.getItem().getColorFromItemStack(itemstack1, k);
                    f12 = (float) (i >> 16 & 255) / 255.0F;
                    f3 = (float) (i >> 8 & 255) / 255.0F;
                    f4 = (float) (i & 255) / 255.0F;
                    GL11.glColor4f(f12, f3, f4, 1.0F);
                    this.renderManager.itemRenderer.renderItem(clientPlayer, itemstack1, k);
                }
            } else {
                k = itemstack1.getItem().getColorFromItemStack(itemstack1, 0);
                float f11 = (float) (k >> 16 & 255) / 255.0F;
                f12 = (float) (k >> 8 & 255) / 255.0F;
                f3 = (float) (k & 255) / 255.0F;
                GL11.glColor4f(f11, f12, f3, 1.0F);
                this.renderManager.itemRenderer.renderItem(clientPlayer, itemstack1, 0);
            }

            GL11.glPopMatrix();
        }
        MinecraftForge.EVENT_BUS.post(new RenderPlayerEvent.Specials.Post(clientPlayer, (RenderPlayer) instance, slot));
    }

}
