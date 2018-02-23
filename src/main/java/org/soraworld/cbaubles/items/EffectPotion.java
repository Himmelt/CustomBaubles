package org.soraworld.cbaubles.items;

public class EffectPotion {

    public byte id;
    public byte lvl;

    public EffectPotion(byte id, byte lvl) {
        this.id = id;
        this.lvl = lvl;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EffectPotion && this.id == ((EffectPotion) obj).id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
