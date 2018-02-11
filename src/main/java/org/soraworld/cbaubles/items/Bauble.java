package org.soraworld.cbaubles.items;

import com.azanor.baubles.api.BaubleType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Bauble {

    private List<EffectPotion> effects;
    private static final List<EffectPotion> EMPTY = new ArrayList<>();
    private BaubleType type;
    private float hp;

    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        compound.setByte("type", (byte) type.ordinal());
        compound.setFloat("hp", hp);
        if (effects != null && !effects.isEmpty()) {
            NBTTagList list = new NBTTagList();
            for (EffectPotion effect : effects) {
                NBTTagCompound potion = new NBTTagCompound();
                potion.setByte("id", effect.id);
                potion.setByte("lvl", effect.lvl);
                list.appendTag(potion);
            }
            compound.setTag("effects", list);
        }
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        type = null;
        effects = null;
        if (compound != null) {
            setType(compound.getByte("type"));
            setHp(compound.getFloat("hp"));
            if (compound.hasKey("effects", 9)) {
                NBTTagList list = compound.getTagList("effects", 10);
                if (list.tagCount() > 0) {
                    effects = new ArrayList<>();
                    for (int i = 0; i < list.tagCount(); i++) {
                        NBTTagCompound potion = list.getCompoundTagAt(i);
                        effects.add(new EffectPotion(potion.getByte("id"), potion.getByte("lvl")));
                    }
                }
            }
        }
    }

    @Nonnull
    public List<EffectPotion> getEffects() {
        return effects == null ? EMPTY : effects;
    }

    public void setType(BaubleType type) {
        this.type = type;
    }

    public void setType(byte type) {
        switch (type) {
            case 0:
                this.type = BaubleType.RING;
                break;
            case 1:
                this.type = BaubleType.AMULET;
                break;
            case 2:
                this.type = BaubleType.BELT;
        }
    }

    public BaubleType getType() {
        return type;
    }

    public void addEffect(@Nonnull EffectPotion effect) {
        if (effects == null) effects = new ArrayList<>();
        effects.add(effect);
    }

    @Override
    public String toString() {
        return type == null ? "null" : type.name();
    }

    public Bauble copy() {
        Bauble bauble = new Bauble();
        bauble.type = type;
        bauble.hp = hp;
        bauble.effects = effects;
        return bauble;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getHP() {
        return this.hp;
    }
}
