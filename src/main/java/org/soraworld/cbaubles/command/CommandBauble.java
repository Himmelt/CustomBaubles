package org.soraworld.cbaubles.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.soraworld.cbaubles.constant.Constants;
import org.soraworld.cbaubles.items.Bauble;
import org.soraworld.cbaubles.items.EffectPotion;
import org.soraworld.cbaubles.items.IItemStack;

import java.util.ArrayList;

public class CommandBauble extends IICommand {

    public CommandBauble() {
        super(Constants.MOD_ID, "bauble");
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (sender instanceof EntityPlayer && args.size() == 3) {
            EntityPlayer player = (EntityPlayer) sender;
            ItemStack stack = player.getHeldItem();
            if (stack != null) {
                System.out.println(stack.hashCode());
                Bauble bauble = ((IItemStack) (Object) player.getHeldItem()).getBauble();
                bauble.setType(Byte.valueOf(args.get(0)));
                bauble.addEffect(new EffectPotion(Byte.valueOf(args.get(1)), Byte.valueOf(args.get(2))));
                System.out.println(stack.hashCode() + "AfterCMD:" + bauble);
                player.setCurrentItemOrArmor(0, stack);
            }
        }
    }
}
