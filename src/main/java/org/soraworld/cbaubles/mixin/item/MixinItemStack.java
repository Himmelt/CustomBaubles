package org.soraworld.cbaubles.mixin.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.soraworld.cbaubles.constant.Constants;
import org.soraworld.cbaubles.items.Bauble;
import org.soraworld.cbaubles.items.IItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements IItemStack {

    @Shadow
    int itemDamage;
    @Shadow
    private Item field_151002_e;
    @Shadow
    public int stackSize;
    @Shadow
    public NBTTagCompound stackTagCompound;

    private Bauble bauble;

    @Shadow
    public abstract void func_150996_a(Item item);

    @Shadow
    public abstract String toString();


    /**
     * Returns a new stack with the same properties.
     *
     * @author Himmelt
     * @reason customize ItemStack
     */
    @Overwrite
    public ItemStack copy() {
        ItemStack itemstack = new ItemStack(this.field_151002_e, this.stackSize, this.itemDamage);

        if (this.stackTagCompound != null) {
            itemstack.stackTagCompound = (NBTTagCompound) this.stackTagCompound.copy();
        }

        if (this.bauble != null) {
            ((IItemStack) (Object) itemstack).setBauble(bauble.copy());
        }

        return itemstack;
    }

    /**
     * Remove the argument from the stack size. Return a new stack object with argument size.
     *
     * @author Himmelt
     * @reason customize ItemStack
     */
    @Overwrite
    public ItemStack splitStack(int splitSize) {
        ItemStack itemstack = new ItemStack(this.field_151002_e, splitSize, this.itemDamage);

        if (this.stackTagCompound != null) {
            itemstack.stackTagCompound = (NBTTagCompound) this.stackTagCompound.copy();
        }

        if (this.bauble != null) {
            ((IItemStack) (Object) itemstack).setBauble(bauble.copy());
        }

        this.stackSize -= splitSize;
        return itemstack;
    }


    /**
     * Write the stack fields to a NBT object. Return the new NBT object.
     *
     * @author Himmelt
     * @reason customize ItemStack
     */
    @Overwrite
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setShort("id", (short) Item.getIdFromItem(this.field_151002_e));
        compound.setByte("Count", (byte) this.stackSize);
        compound.setShort("Damage", (short) this.itemDamage);

        if (this.stackTagCompound != null) {
            if (bauble != null)
                this.stackTagCompound.setTag(Constants.TAG_CUSTOM, bauble.writeToNBT(new NBTTagCompound()));
            compound.setTag("tag", this.stackTagCompound);
        } else if (bauble != null) {
            this.stackTagCompound = new NBTTagCompound();
            this.stackTagCompound.setTag(Constants.TAG_CUSTOM, bauble.writeToNBT(new NBTTagCompound()));
            compound.setTag("tag", this.stackTagCompound);
        }

        return compound;
    }

    /**
     * Read the stack fields from a NBT object.
     *
     * @author Himmelt
     * @reason customize ItemStack
     */
    @Overwrite
    public void readFromNBT(NBTTagCompound compound) {
        func_150996_a(Item.getItemById(compound.getShort("id")));
        this.stackSize = compound.getByte("Count");
        this.itemDamage = compound.getShort("Damage");

        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }

        if (compound.hasKey("tag", 10)) {
            this.stackTagCompound = compound.getCompoundTag("tag");
        }
        if (this.stackTagCompound != null) {
            bauble = new Bauble();
            bauble.readFromNBT(this.stackTagCompound.getCompoundTag(Constants.TAG_CUSTOM));
        }
    }

    @Nonnull
    @Override
    public Bauble getBauble() {
        if (bauble == null) bauble = new Bauble();
        return bauble;
    }

    @Override
    public void setBauble(@Nonnull Bauble bauble) {
        this.bauble = bauble;
    }
}
