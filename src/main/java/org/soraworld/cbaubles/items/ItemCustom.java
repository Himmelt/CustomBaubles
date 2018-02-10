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
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        System.out.println(stack);
        return stack == null ? null : ((IItemStack) (Object) stack).getBauble().getType();
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
        if (ticks++ >= 20 && stack != null) {
            Bauble bauble = ((IItemStack) (Object) stack).getBauble();
            for (PotionEffect effect : bauble.getEffects()) {
                player.addPotionEffect(effect);
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
        if (!world.isRemote) {
            IInventory baubles = BaublesApi.getBaubles(player);
            for (int i = 0; baubles != null && i < baubles.getSizeInventory(); i++) {
                if (baubles.getStackInSlot(i) == null && baubles.isItemValidForSlot(i, stack)) {
                    baubles.setInventorySlotContents(i, stack.copy());
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }
                    onEquipped(stack, player);
                    break;
                }
            }
        }
        return stack;
    }

    public String getRegisterName() {
        return registerName;
    }
}
