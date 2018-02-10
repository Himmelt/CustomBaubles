package org.soraworld.cbaubles.mixin.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.soraworld.cbaubles.util.ItemUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
import static net.minecraftforge.client.IItemRenderer.ItemRenderType.FIRST_PERSON_MAP;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Final
    @Shadow
    private static ResourceLocation RES_MAP_BACKGROUND;
    @Shadow
    private Minecraft mc;
    @Shadow
    private ItemStack itemToRender;
    @Shadow
    private float equippedProgress;
    @Shadow
    private float prevEquippedProgress;

    @Shadow
    public abstract void renderItem(EntityLivingBase p_78443_1_, ItemStack p_78443_2_, int p_78443_3_, ItemRenderType type);

    /**
     * Renders the active item in the player's hand when in first person mode. Args: partialTickTime
     *
     * @author Himmelt
     * @reason change render ItemStack
     */
    @Overwrite
    public void renderItemInFirstPerson(float p_78440_1_) {
        float f1 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * p_78440_1_;
        EntityClientPlayerMP entityclientplayermp = this.mc.thePlayer;
        float f2 = entityclientplayermp.prevRotationPitch + (entityclientplayermp.rotationPitch - entityclientplayermp.prevRotationPitch) * p_78440_1_;
        GL11.glPushMatrix();
        GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(entityclientplayermp.prevRotationYaw + (entityclientplayermp.rotationYaw - entityclientplayermp.prevRotationYaw) * p_78440_1_, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        EntityPlayerSP entityplayersp = entityclientplayermp;
        float f3 = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_78440_1_;
        float f4 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_78440_1_;
        GL11.glRotatef((entityclientplayermp.rotationPitch - f3) * 0.1F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef((entityclientplayermp.rotationYaw - f4) * 0.1F, 0.0F, 1.0F, 0.0F);

        // custom
        ItemStack itemstack = ItemUtils.changeRenderTarget(this.itemToRender);

        if (itemstack != null && itemstack.getItem() instanceof ItemCloth) {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }

        int i = this.mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(entityclientplayermp.posX), MathHelper.floor_double(entityclientplayermp.posY), MathHelper.floor_double(entityclientplayermp.posZ), 0);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F, (float) k / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f5;
        float f6;
        float f7;

        if (itemstack != null) {
            int l = itemstack.getItem().getColorFromItemStack(itemstack, 0);
            f5 = (float) (l >> 16 & 255) / 255.0F;
            f6 = (float) (l >> 8 & 255) / 255.0F;
            f7 = (float) (l & 255) / 255.0F;
            GL11.glColor4f(f5, f6, f7, 1.0F);
        } else {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        float f8;
        float f9;
        float f10;
        float f13;
        Render render;
        RenderPlayer renderplayer;

        if (itemstack != null && itemstack.getItem() instanceof ItemMap) {
            GL11.glPushMatrix();
            f13 = 0.8F;
            f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
            f6 = MathHelper.sin(f5 * (float) Math.PI);
            f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI);
            GL11.glTranslatef(-f7 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI * 2.0F) * 0.2F, -f6 * 0.2F);
            f5 = 1.0F - f2 / 45.0F + 0.1F;

            if (f5 < 0.0F) {
                f5 = 0.0F;
            }

            if (f5 > 1.0F) {
                f5 = 1.0F;
            }

            f5 = -MathHelper.cos(f5 * (float) Math.PI) * 0.5F + 0.5F;
            GL11.glTranslatef(0.0F, 0.0F * f13 - (1.0F - f1) * 1.2F - f5 * 0.5F + 0.04F, -0.9F * f13);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(f5 * -85.0F, 0.0F, 0.0F, 1.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            this.mc.getTextureManager().bindTexture(entityclientplayermp.getLocationSkin());

            for (int i1 = 0; i1 < 2; ++i1) {
                int j1 = i1 * 2 - 1;
                GL11.glPushMatrix();
                GL11.glTranslatef(-0.0F, -0.6F, 1.1F * (float) j1);
                GL11.glRotatef((float) (-45 * j1), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef((float) (-65 * j1), 0.0F, 1.0F, 0.0F);
                render = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
                renderplayer = (RenderPlayer) render;
                f10 = 1.0F;
                GL11.glScalef(f10, f10, f10);
                renderplayer.renderFirstPersonArm(this.mc.thePlayer);
                GL11.glPopMatrix();
            }

            f6 = entityclientplayermp.getSwingProgress(p_78440_1_);
            f7 = MathHelper.sin(f6 * f6 * (float) Math.PI);
            f8 = MathHelper.sin(MathHelper.sqrt_float(f6) * (float) Math.PI);
            GL11.glRotatef(-f7 * 20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f8 * 20.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f8 * 80.0F, 1.0F, 0.0F, 0.0F);
            f9 = 0.38F;
            GL11.glScalef(f9, f9, f9);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
            f10 = 0.015625F;
            GL11.glScalef(f10, f10, f10);
            this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
            Tessellator tessellator = Tessellator.instance;
            GL11.glNormal3f(0.0F, 0.0F, -1.0F);
            tessellator.startDrawingQuads();
            byte b0 = 7;
            tessellator.addVertexWithUV((double) (0 - b0), (double) (128 + b0), 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double) (128 + b0), (double) (128 + b0), 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double) (128 + b0), (double) (0 - b0), 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV((double) (0 - b0), (double) (0 - b0), 0.0D, 0.0D, 0.0D);
            tessellator.draw();

            IItemRenderer custom = MinecraftForgeClient.getItemRenderer(itemstack, FIRST_PERSON_MAP);
            MapData mapdata = ((ItemMap) itemstack.getItem()).getMapData(itemstack, this.mc.theWorld);

            if (custom == null) {
                if (mapdata != null) {
                    this.mc.entityRenderer.getMapItemRenderer().func_148250_a(mapdata, false);
                }
            } else {
                custom.renderItem(FIRST_PERSON_MAP, itemstack, mc.thePlayer, mc.getTextureManager(), mapdata);
            }

            GL11.glPopMatrix();
        } else if (itemstack != null) {
            GL11.glPushMatrix();
            f13 = 0.8F;

            if (entityclientplayermp.getItemInUseCount() > 0) {
                EnumAction enumaction = itemstack.getItemUseAction();

                if (enumaction == EnumAction.eat || enumaction == EnumAction.drink) {
                    f6 = (float) entityclientplayermp.getItemInUseCount() - p_78440_1_ + 1.0F;
                    f7 = 1.0F - f6 / (float) itemstack.getMaxItemUseDuration();
                    f8 = 1.0F - f7;
                    f8 = f8 * f8 * f8;
                    f8 = f8 * f8 * f8;
                    f8 = f8 * f8 * f8;
                    f9 = 1.0F - f8;
                    GL11.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos(f6 / 4.0F * (float) Math.PI) * 0.1F) * (float) ((double) f7 > 0.2D ? 1 : 0), 0.0F);
                    GL11.glTranslatef(f9 * 0.6F, -f9 * 0.5F, 0.0F);
                    GL11.glRotatef(f9 * 90.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(f9 * 10.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(f9 * 30.0F, 0.0F, 0.0F, 1.0F);
                }
            } else {
                f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
                f6 = MathHelper.sin(f5 * (float) Math.PI);
                f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI);
                GL11.glTranslatef(-f7 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI * 2.0F) * 0.2F, -f6 * 0.2F);
            }

            GL11.glTranslatef(0.7F * f13, -0.65F * f13 - (1.0F - f1) * 0.6F, -0.9F * f13);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
            f6 = MathHelper.sin(f5 * f5 * (float) Math.PI);
            f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI);
            GL11.glRotatef(-f6 * 20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f7 * 20.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f7 * 80.0F, 1.0F, 0.0F, 0.0F);
            f8 = 0.4F;
            GL11.glScalef(f8, f8, f8);
            float f11;
            float f12;

            if (entityclientplayermp.getItemInUseCount() > 0) {
                EnumAction enumaction1 = itemstack.getItemUseAction();

                if (enumaction1 == EnumAction.block) {
                    GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
                    GL11.glRotatef(30.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
                } else if (enumaction1 == EnumAction.bow) {
                    GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
                    f10 = (float) itemstack.getMaxItemUseDuration() - ((float) entityclientplayermp.getItemInUseCount() - p_78440_1_ + 1.0F);
                    f11 = f10 / 20.0F;
                    f11 = (f11 * f11 + f11 * 2.0F) / 3.0F;

                    if (f11 > 1.0F) {
                        f11 = 1.0F;
                    }

                    if (f11 > 0.1F) {
                        GL11.glTranslatef(0.0F, MathHelper.sin((f10 - 0.1F) * 1.3F) * 0.01F * (f11 - 0.1F), 0.0F);
                    }

                    GL11.glTranslatef(0.0F, 0.0F, f11 * 0.1F);
                    GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(0.0F, 0.5F, 0.0F);
                    f12 = 1.0F + f11 * 0.2F;
                    GL11.glScalef(1.0F, 1.0F, f12);
                    GL11.glTranslatef(0.0F, -0.5F, 0.0F);
                    GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
                }
            }

            if (itemstack.getItem().shouldRotateAroundWhenRendering()) {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }

            if (itemstack.getItem().requiresMultipleRenderPasses()) {
                this.renderItem(entityclientplayermp, itemstack, 0, EQUIPPED_FIRST_PERSON);
                for (int x = 1; x < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); x++) {
                    int k1 = itemstack.getItem().getColorFromItemStack(itemstack, x);
                    f10 = (float) (k1 >> 16 & 255) / 255.0F;
                    f11 = (float) (k1 >> 8 & 255) / 255.0F;
                    f12 = (float) (k1 & 255) / 255.0F;
                    GL11.glColor4f(1.0F * f10, 1.0F * f11, 1.0F * f12, 1.0F);
                    this.renderItem(entityclientplayermp, itemstack, x, EQUIPPED_FIRST_PERSON);
                }
            } else {
                this.renderItem(entityclientplayermp, itemstack, 0, EQUIPPED_FIRST_PERSON);
            }

            GL11.glPopMatrix();
        } else if (!entityclientplayermp.isInvisible()) {
            GL11.glPushMatrix();
            f13 = 0.8F;
            f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
            f6 = MathHelper.sin(f5 * (float) Math.PI);
            f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI);
            GL11.glTranslatef(-f7 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI * 2.0F) * 0.4F, -f6 * 0.4F);
            GL11.glTranslatef(0.8F * f13, -0.75F * f13 - (1.0F - f1) * 0.6F, -0.9F * f13);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
            f6 = MathHelper.sin(f5 * f5 * (float) Math.PI);
            f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI);
            GL11.glRotatef(f7 * 70.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f6 * 20.0F, 0.0F, 0.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(entityclientplayermp.getLocationSkin());
            GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
            GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(5.6F, 0.0F, 0.0F);
            render = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
            renderplayer = (RenderPlayer) render;
            f10 = 1.0F;
            GL11.glScalef(f10, f10, f10);
            renderplayer.renderFirstPersonArm(this.mc.thePlayer);
            GL11.glPopMatrix();
        }

        if (itemstack != null && itemstack.getItem() instanceof ItemCloth) {
            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
    }
}