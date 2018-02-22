package org.soraworld.cbaubles.items;

import com.azanor.baubles.api.BaubleType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.soraworld.cbaubles.util.PermissionManager;

import javax.annotation.Nonnull;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.soraworld.cbaubles.constant.Constants.LOGGER;

public class Bauble {

    private LinkedHashSet<EffectPotion> effects;
    private static final Set<EffectPotion> EMPTY = new LinkedHashSet<>();
    private BaubleType type;
    private int hp;
    /* knock back Resistance */
    private int kp;
    private String perm;
    private boolean bind;
    private String owner;
    private Item item;
    private int damage;

    private static final PermissionManager pm = PermissionManager.getPermissionManager();

    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        if (type != null) compound.setByte("type", (byte) type.ordinal());
        compound.setInteger("hp", hp);
        compound.setInteger("kp", kp);
        if (perm != null && !perm.isEmpty()) compound.setString("perm", perm);
        compound.setBoolean("bind", bind);
        if (owner != null && !owner.isEmpty()) compound.setString("owner", owner);
        if (item != null && !(item instanceof ItemCustom))
            compound.setString("icon", Item.getIdFromItem(item) + "/" + damage);
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
        hp = 0;
        kp = 0;
        perm = null;
        bind = false;
        owner = null;
        item = null;
        damage = 0;
        effects = null;
        if (compound != null) {
            if (compound.hasKey("type")) setType(compound.getByte("type"));
            if (compound.hasKey("hp")) setHP(compound.getInteger("hp"));
            if (compound.hasKey("kp")) setKP(compound.getInteger("kp"));
            if (compound.hasKey("perm")) setPerm(compound.getString("perm"));
            if (compound.hasKey("bind")) setBind(compound.getBoolean("bind"));
            if (compound.hasKey("owner")) setOwner(compound.getString("owner"));
            if (compound.hasKey("icon")) setIcon(compound.getString("icon"));
            if (compound.hasKey("effects", 9)) {
                NBTTagList list = compound.getTagList("effects", 10);
                if (list.tagCount() > 0) {
                    effects = new LinkedHashSet<>();
                    for (int i = 0; i < list.tagCount(); i++) {
                        NBTTagCompound potion = list.getCompoundTagAt(i);
                        effects.add(new EffectPotion(potion.getByte("id"), potion.getByte("lvl")));
                    }
                }
            }
        }
    }

    @Nonnull
    public Set<EffectPotion> getEffects() {
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
        if (effects == null) effects = new LinkedHashSet<>();
        effects.add(effect);
    }

    @Override
    public String toString() {
        return "[" + type + "/" + hp + "/" + perm + "/" + bind + "/" + owner + "/" + item + "/" + damage + "]";
    }

    public Bauble copy() {
        Bauble bauble = new Bauble();
        bauble.type = type;
        bauble.hp = hp;
        bauble.kp = kp;
        bauble.perm = perm;
        bauble.bind = bind;
        bauble.owner = owner;
        bauble.item = item;
        bauble.damage = damage;
        bauble.effects = effects;
        return bauble;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }

    public int getHP() {
        return this.hp;
    }

    public void setKP(int kp) {
        this.kp = kp;
    }

    public int getKP() {
        return this.kp;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }

    public boolean bind() {
        return bind;
    }

    public boolean canBind() {
        return bind && (owner == null || owner.isEmpty());
    }

    public boolean canUse(EntityPlayer player) {
        return pm.hasPermission(player, perm) && (!bind || owner == null || owner.isEmpty() || player.getCommandSenderName().equals(owner));
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getIcon() {
        return item != null ? new ItemStack(item, damage) : null;
    }

    public void setIcon(ItemStack icon) {
        if (icon == null) {
            this.item = null;
            this.damage = 0;
        } else {
            this.item = icon.getItem();
            this.damage = icon.getItemDamage();
        }
    }

    public void setIcon(int id, int damage) {
        if (Item.getItemById(id) != null) {
            this.item = Item.getItemById(id);
            this.damage = damage;
        }
    }

    private void setIcon(String icon) {
        try {
            String[] ss = icon.split("/");
            this.item = Item.getItemById(Integer.valueOf(ss[0]));
            this.damage = Integer.valueOf(ss[1]);
        } catch (Throwable e) {
            LOGGER.catching(e);
        }
    }

}
