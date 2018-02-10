package org.soraworld.cbaubles.items;

import com.azanor.baubles.api.BaubleType;
import com.azanor.baubles.api.BaublesApi;
import com.azanor.baubles.api.IBauble;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.soraworld.cbaubles.constant.Constants;

public class ItemCustom extends Item implements IBauble {

    private final String registerName;
    private byte ticks = 0;

    public ItemCustom(String registerName) {
        this.registerName = registerName;
        this.setTextureName(Constants.MOD_ID + ":" + registerName);
        this.setUnlocalizedName(registerName);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        if (stack != null && stack.stackTagCompound != null) {
            NBTTagCompound bauble = stack.stackTagCompound.getCompoundTag(Constants.TAG_CUSTOM);
            switch (bauble.getByte(Constants.TAG_TYPE)) {
                case 0:
                    return BaubleType.RING;
                case 1:
                    return BaubleType.AMULET;
                case 2:
                    return BaubleType.BELT;
            }
        }
        return null;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (ticks++ >= 20 && itemstack != null && itemstack.stackTagCompound != null) {
            NBTTagList potionEffects = itemstack.stackTagCompound.getCompoundTag(Constants.TAG_CUSTOM).getTagList(Constants.TAG_EFFECT, 10);
            int size = potionEffects.tagCount();
            for (int i = 0; i < size; i++) {
                NBTTagCompound effect = potionEffects.getCompoundTagAt(i);
                if (effect != null) {
                    byte id = effect.getByte("id");
                    byte lvl = effect.getByte("lvl");
                    player.addPotionEffect(new PotionEffect(id, 60, lvl - 1, true));
                }
            }
            ticks = 0;
        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase livingBase) {
        return itemStack != null && itemStack.getItem() instanceof IBauble && livingBase instanceof EntityPlayer;
    }

    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        // TODO cant unEquip
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            IInventory baubles = BaublesApi.getBaubles(player);
            for (int i = 0; baubles != null && i < baubles.getSizeInventory(); i++) {
                if (baubles.getStackInSlot(i) == null && baubles.isItemValidForSlot(i, itemStack)) {
                    baubles.setInventorySlotContents(i, itemStack.copy());
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }
                    onEquipped(itemStack, player);
                    break;
                }
            }
        }
        return itemStack;
    }

    public String getRegisterName() {
        return registerName;
    }
}
