package org.soraworld.cbaubles.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.soraworld.cbaubles.constant.Constants;

import java.util.ArrayList;

public class CommandBauble extends IICommand {

    public CommandBauble() {
        super(Constants.MOD_ID, "bauble");
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (sender instanceof EntityPlayer && args.size() == 4) {
            EntityPlayer player = (EntityPlayer) sender;
            ItemStack stack = player.getHeldItem();
            if (stack.stackTagCompound == null) {
                stack.stackTagCompound = new NBTTagCompound();
            }
            NBTTagCompound bauble = stack.stackTagCompound.getCompoundTag(Constants.TAG_CUSTOM);
            bauble.setByte(Constants.TAG_TYPE, Byte.valueOf(args.get(0)));
            bauble.setString(Constants.TAG_ITEM, args.get(1));
            NBTTagCompound effect = new NBTTagCompound();
            effect.setByte("id", Byte.valueOf(args.get(2)));
            effect.setByte("lvl", Byte.valueOf(args.get(3)));
            NBTTagList potionEffects = bauble.getTagList(Constants.TAG_EFFECT, 10);
            potionEffects.appendTag(effect);
            bauble.setTag(Constants.TAG_EFFECT, potionEffects);
            stack.stackTagCompound.setTag(Constants.TAG_CUSTOM, bauble);
        }
    }
}
