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

    public String getRegisterName() {
        return registerName;
    }
}
