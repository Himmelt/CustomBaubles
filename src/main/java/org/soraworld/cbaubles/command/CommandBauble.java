package org.soraworld.cbaubles.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.soraworld.cbaubles.constant.Constants;

import java.util.ArrayList;

public class CommandBauble extends IICommand {

    public CommandBauble() {
        super(Constants.MOD_ID, "bauble");
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (sender instanceof EntityPlayer && args.size() == 2) {
            EntityPlayer player = (EntityPlayer) sender;
            ItemStack stack = player.getHeldItem();
            if (stack.stackTagCompound == null) {
                stack.stackTagCompound = new NBTTagCompound();
            }
            NBTTagCompound bauble = stack.stackTagCompound.getCompoundTag(Constants.TAG_CUSTOM);
            bauble.setByte(Constants.TAG_TYPE, Byte.valueOf(args.get(0)));
            bauble.setString(Constants.TAG_ITEM, args.get(1));
            stack.stackTagCompound.setTag(Constants.TAG_CUSTOM, bauble);
        }
    }
}
