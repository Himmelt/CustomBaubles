package org.soraworld.cbaubles.items;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.soraworld.cbaubles.constant.Constants;
import org.soraworld.cbaubles.util.I19n;

import java.util.ArrayList;
import java.util.List;

public class ItemCustom extends Item implements IBauble {

    private final String registerName;
    private byte ticks = 0;

    public ItemCustom(String registerName) {
        this.registerName = registerName;
        this.setTextureName(Constants.MOD_ID + ":" + registerName);
        this.setUnlocalizedName(registerName);
        this.setMaxStackSize(16);
        this.setMaxDamage(10);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        if (stack != null) {
            Bauble bauble = ((IItemStack) (Object) stack).getBauble();
            if (bauble != null) {
                return bauble.getType();
            }
        }
        return null;
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
        if (ticks++ >= 20 && stack != null && player instanceof EntityPlayer) {
            Bauble bauble = ((IItemStack) (Object) stack).getBauble();
            if (bauble != null && bauble.canUse((EntityPlayer) player)) {
                for (EffectPotion effect : bauble.getEffects()) {
                    player.addPotionEffect(new PotionEffect(effect.id, 60, effect.lvl, true));
                }
            }
            ticks = 0;
        }
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase player) {
        if (stack != null && player instanceof EntityPlayer) {
            Bauble bauble = ((IItemStack) (Object) stack).getBauble();
            if (bauble != null && bauble.canBind()) {
                bauble.setOwner(player.getCommandSenderName());
            }
        }
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player) {
    }

    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase living) {
        return itemStack != null && itemStack.getItem() instanceof IBauble && living instanceof EntityPlayer;
    }

    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        // TODO cant unEquip
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && stack != null) {
            Bauble bauble = ((IItemStack) (Object) stack).getBauble();
            if (bauble != null && bauble.canUse(player)) {
                IInventory baubles = BaublesApi.getBaubles(player);
                for (int i = 0; baubles != null && i < baubles.getSizeInventory(); i++) {
                    if (baubles.getStackInSlot(i) == null && baubles.isItemValidForSlot(i, stack)) {
                        baubles.setInventorySlotContents(i, stack.copy());
                        if (!player.capabilities.isCreativeMode) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }
                        break;
                    }
                }
            }
        }
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        if (stack != null && list != null) {
            Bauble bauble = ((IItemStack) (Object) stack).getBauble();
            if (bauble != null) {
                ArrayList<String> tips = new ArrayList<>();
                tips.add(I19n.format("tooltip.type." + bauble.getType()));
                if (bauble.getPerm() != null && !bauble.getPerm().isEmpty()) {
                    tips.add(I19n.format("tooltip.perm", bauble.getPerm()));
                }
                if (bauble.bind()) {
                    if (bauble.getOwner() != null && !bauble.getOwner().isEmpty()) {
                        tips.add(I19n.format("tooltip.owner", bauble.getOwner()));
                    } else {
                        tips.add(I19n.format("tooltip.bind.can"));
                    }
                }
                //else {
                //    tips.add(I19n.format("tooltip.bind.cant"));
                //}
                if (bauble.getHP() != 0) tips.add(I19n.format("tooltip.hp", bauble.getHP()));
                if (bauble.getSP() != 0) tips.add(I19n.format("tooltip.sp", bauble.getSP()));
                if (bauble.getAT() != 0) tips.add(I19n.format("tooltip.at", bauble.getAT()));
                if (bauble.getKB() > 0) tips.add(I19n.format("tooltip.kb", bauble.getKB()));
                if (GuiScreen.isShiftKeyDown()) {
                    for (EffectPotion potion : bauble.getEffects()) {
                        tips.add(I19n.format("tooltip.effect") + I19n.format(Potion.potionTypes[potion.id].getName()) + " " + potion.lvl);
                    }
                } else if (bauble.getEffects().size() > 0) {
                    tips.add(I19n.format("tooltip.effect.hide", bauble.getEffects().size()));
                }
                list.addAll(tips);
            }
        }
    }

    public String getRegisterName() {
        return registerName;
    }
}
