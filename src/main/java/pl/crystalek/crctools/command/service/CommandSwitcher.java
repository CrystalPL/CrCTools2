package pl.crystalek.crctools.command.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class CommandSwitcher extends Command {
    CommandManager commandManager;

    public CommandSwitcher(final String commandName, final List<String> aliases, final CommandManager commandManager) {
        super(commandName);
        setAliases(aliases);

        this.commandManager = commandManager;
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        commandManager.execute(this, sender, args);
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        return commandManager.tabComplete(this, sender, args);
    }
}
