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
        super(Constants.MOD_ID, "cbs");
        addSub(new SubCommand("display") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("bauble.display", player.getHeldItem().getDisplayName()));
                } else {
                    ItemStack held = player.getHeldItem();
                    ItemUtils.updateHeldItem(held, bauble);
                    held.setStackDisplayName(args.get(0).replace('&', Constants.COLOR_CHAR));
                }
            }
        });
        addSub(new SubCommand("type") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("bauble.type." + bauble.getType()));
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
                    player.addChatMessage(I19n.translate("bauble.perm", bauble.getPerm()));
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
                    player.addChatMessage(I19n.translate("bauble.bind", bauble.bind()));
                } else {
                    try {
                        bauble.setBind(Boolean.valueOf(args.get(0)));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalid.bool"));
                    }
                }
            }
        });
        addSub(new SubCommand("health", "hp") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("bauble.hp", bauble.getHP()));
                } else {
                    try {
                        bauble.setHP(Integer.valueOf(args.get(0)));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalid.int"));
                    }
                }
            }
        });
        addSub(new SubCommand("speed", "sp") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("bauble.sp", bauble.getSP()));
                } else {
                    try {
                        bauble.setSP(Float.valueOf(args.get(0)));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalid.float"));
                    }
                }
            }
        });
        addSub(new SubCommand("attack", "at") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("bauble.at", bauble.getAT()));
                } else {
                    try {
                        bauble.setAT(Float.valueOf(args.get(0)));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalid.float"));
                    }
                }
            }
        });
        addSub(new SubCommand("knock", "kb") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("bauble.kb", bauble.getKB()));
                } else {
                    try {
                        bauble.setKB(Byte.valueOf(args.get(0)));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalid.byte"));
                    }
                }
            }
        });
        addSub(new SubCommand("effect") {
            @Override
            public void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args) {
                if (args.isEmpty()) {
                    player.addChatMessage(I19n.translate("bauble.effects", bauble.getEffects().size()));
                } else if (args.size() == 1) {
                    try {
                        bauble.addEffect(new EffectPotion(Byte.valueOf(args.get(0)), (byte) 0));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalid.byte"));
                    }
                } else {
                    try {
                        bauble.addEffect(new EffectPotion(Byte.valueOf(args.get(0)), Byte.valueOf(args.get(1))));
                    } catch (Throwable e) {
                        player.addChatMessage(I19n.translate("invalid.byte"));
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
                        ItemUtils.updateHeldItem(itemStack, bauble);
                    } else if (sub != null) {
                        sub.execute(sender, args);
                    }
                } else {
                    sender.addChatMessage(I19n.translate("empty.hand"));
                }
            } else {
                sender.addChatMessage(I19n.translate("only.player"));
            }
        } else {
            // TODO xxx
            sender.addChatMessage(new ChatComponentText("empty.args"));
        }
    }
}
