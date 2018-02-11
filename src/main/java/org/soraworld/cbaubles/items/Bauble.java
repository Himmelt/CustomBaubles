package org.soraworld.cbaubles.items;

import com.azanor.baubles.api.BaubleType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Bauble {

    private List<PotionEffect> effects;
    private static final List<PotionEffect> EMPTY = new ArrayList<>();
    private BaubleType type;

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {

    }

    @Nonnull
    public List<PotionEffect> getEffects() {
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

    public void addEffect(@Nonnull PotionEffect effect) {
        System.out.println("Add Effect: " + effect);
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
        bauble.effects = effects;
        return bauble;
    }
}
