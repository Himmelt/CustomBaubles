package org.soraworld.cbaubles.command;

import net.minecraft.entity.player.EntityPlayerMP;
import org.soraworld.cbaubles.items.Bauble;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public abstract class SubCommand extends IICommand {

    public SubCommand(String name, String... aliases) {
        super(name, aliases);
    }

    public abstract void execute(@Nonnull Bauble bauble, EntityPlayerMP player, ArrayList<String> args);

}
