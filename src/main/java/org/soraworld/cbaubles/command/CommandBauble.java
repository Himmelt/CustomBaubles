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
        if (sender instanceof EntityPlayer && args.size() == 4) {
            EntityPlayer player = (EntityPlayer) sender;
            ItemStack stack = player.getHeldItem();
            if (stack != null) {
                Bauble bauble = ((IItemStack) (Object) player.getHeldItem()).getOrCreateBauble();
                bauble.setType(Byte.valueOf(args.get(0)));
                bauble.setHp(Float.valueOf(args.get(1)));
                bauble.addEffect(new EffectPotion(Byte.valueOf(args.get(2)), Byte.valueOf(args.get(3))));
                //player.setCurrentItemOrArmor(0, stack);
            }
        }
    }
}
