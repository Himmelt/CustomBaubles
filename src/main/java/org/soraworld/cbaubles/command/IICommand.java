package org.soraworld.cbaubles.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import org.soraworld.cbaubles.util.ListUtils;

import java.util.*;

public abstract class IICommand implements ICommand {

    private final String name;
    private final List<String> aliases;
    protected final TreeMap<String, IICommand> subMap = new TreeMap<>();

    private IICommand() {
        name = "invalid";
        aliases = new ArrayList<>();
    }

    public IICommand(String name, String... aliases) {
        this.name = name;
        this.aliases = ListUtils.arrayList(aliases);
    }

    private static List<String> getMatchList(String arg, Collection<String> possibles) {
        if (arg.isEmpty()) return new ArrayList<>(possibles);
        ArrayList<String> list = new ArrayList<>();
        for (String possible : possibles) {
            if (possible.startsWith(arg)) {
                list.add(possible);
            }
        }
        return list;
    }

    final void addSub(IICommand sub) {
        this.subMap.put(sub.getCommandName(), sub);
        for (String alias : sub.getCommandAliases()) {
            IICommand command = this.subMap.get(alias);
            if (command == null || !command.getCommandName().equals(alias)) {
                this.subMap.put(alias, sub);
            }
        }
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public final String getCommandName() {
        return name;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] argArray) throws CommandException {
        execute(sender, ListUtils.arrayList(argArray));
    }

    @Override
    public final List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender.canCommandSenderUseCommand(4, "op");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] argArray) {
        return getTabCompletions(sender, new ArrayList<>(Arrays.asList(argArray)));
    }

    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (args.size() >= 1) {
            IICommand sub = subMap.get(args.remove(0));
            if (sub != null) {
                sub.execute(sender, args);
            }
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(Object command) {
        return command instanceof IICommand ? getCommandName().compareTo(((IICommand) command).getCommandName()) : 0;
    }

    private List<String> getTabCompletions(ICommandSender sender, ArrayList<String> args) {
        if (args.size() == 1) {
            return getMatchList(args.get(0), subMap.keySet());
        } else if (args.size() >= 2) {
            IICommand sub = subMap.get(args.remove(0));
            if (sub != null) return sub.getTabCompletions(sender, args);
            else return new ArrayList<>();
        } else {
            return new ArrayList<>(subMap.keySet());
        }
    }

}
