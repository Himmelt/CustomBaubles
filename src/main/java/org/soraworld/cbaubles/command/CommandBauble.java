package org.soraworld.cbaubles.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import org.soraworld.cbaubles.constant.Constants;
import org.soraworld.cbaubles.items.Bauble;
import org.soraworld.cbaubles.items.EffectPotion;
import org.soraworld.cbaubles.items.IItemStack;
import org.soraworld.cbaubles.items.ItemCustom;
import org.soraworld.cbaubles.util.I19n;
import org.soraworld.cbaubles.util.ItemUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class CommandBauble extends IICommand {

    public CommandBauble() {
        super(Constants.MOD_ID, "bauble");
        addSub(new SubCommand("display") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("baubleDisplay"));
                } else {
                    player.getHeldItem().setStackDisplayName(args.get(0).replace('&', Constants.COLOR_CHAR));
                }
            }
        });
        addSub(new SubCommand("type") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("baubleType", I19n.translate(bauble.getType().name())));
                } else {
                    switch (args.get(0)) {
                        case "0":
                            bauble.setType((byte) 0);
                            break;
                        case "1":
                            bauble.setType((byte) 1);
                            break;
                        case "2":
                            bauble.setType((byte) 2);
                            break;
                        default:
                            bauble.setType(null);
                            break;
                    }
                }
            }
        });
        addSub(new SubCommand("icon") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                ItemStack stack = player.inventory.getStackInSlot(1);
                if (stack == null || stack.getItem() instanceof ItemCustom) {
                    bauble.setIcon(null);
                } else {
                    bauble.setIcon(stack);
                }
            }
        });
        addSub(new SubCommand("perm") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("baublePerm", bauble.getPerm()));
                } else {
                    if (args.get(0).isEmpty() || args.get(0).equals("remove")) {
                        bauble.setPerm(null);
                    } else {
                        bauble.setPerm(args.get(0));
                    }
                }
            }
        });
        addSub(new SubCommand("bind") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("baubleBind", bauble.bind()));
                } else {
                    try {
                        bauble.setBind(Boolean.valueOf(args.get(0)));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalidBoolean"));
                    }
                }
            }
        });
        addSub(new SubCommand("health", "hp") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("baubleHP", bauble.getHP()));
                } else {
                    try {
                        bauble.setHP(Integer.valueOf(args.get(0)));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalidFloat"));
                    }
                }
            }
        });
        addSub(new SubCommand("knock", "kb") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("baubleKB", bauble.getKB()));
                } else {
                    try {
                        bauble.setKB(Byte.valueOf(args.get(0)));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalidByte"));
                    }
                }
            }
        });
        addSub(new SubCommand("attack", "at") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("baubleAT", bauble.getAT()));
                } else {
                    try {
                        bauble.setAT(Float.valueOf(args.get(0)));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalidFloat"));
                    }
                }
            }
        });
        addSub(new SubCommand("effect") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("baubleEffects"));
                } else if (args.size() == 1) {
                    try {
                        bauble.addEffect(new EffectPotion(Byte.valueOf(args.get(0)), (byte) 0));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalidByte"));
                    }
                } else {
                    try {
                        bauble.addEffect(new EffectPotion(Byte.valueOf(args.get(0)), Byte.valueOf(args.get(1))));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalidByte"));
                    }
                }
            }
        });
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (args.size() >= 1) {
            if (sender instanceof EntityPlayerMP) {
                ItemStack itemStack = ((EntityPlayerMP) sender).getHeldItem();
                if (itemStack != null && itemStack.getItem() instanceof ItemCustom) {
                    Bauble bauble = ((IItemStack) (Object) itemStack).getOrCreateBauble();
                    IICommand sub = subMap.get(args.remove(0));
                    if (sub instanceof SubCommand) {
                        ((SubCommand) sub).execute(bauble, (EntityPlayerMP) sender, args);
                        ItemUtils.updateHeldItem(((EntityPlayerMP) sender).getHeldItem(), bauble);
                    } else if (sub != null) {
                        sub.execute(sender, args);
                    }
                } else {
                    sender.addChatMessage(I19n.translate("emptyHand"));
                }
            } else {
                sender.addChatMessage(I19n.translate("onlyPlayer"));
            }
        } else {
            // TODO xxx
            sender.addChatMessage(new ChatComponentText("emptyArgs"));
        }
    }
}
