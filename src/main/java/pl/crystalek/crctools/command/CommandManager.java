package pl.crystalek.crctools.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.command.CommandRegistry;
import pl.crystalek.crcapi.message.MessageAPI;
import pl.crystalek.crctools.command.model.CommandData;
import pl.crystalek.crctools.command.model.ICommand;
import pl.crystalek.crctools.config.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class CommandManager {
    Map<CommandSwitcher, ICommand> commandMap = new HashMap<>();
    Config config;
    JavaPlugin plugin;
    MessageAPI messageAPI;

    void execute(final CommandSwitcher commandSwitcher, final CommandSender sender, final String[] args) {
        final ICommand command = commandMap.get(commandSwitcher);
        if (command == null) {
            //wiadomosc, że cos sie zjebalo, mimo ze nie powinno
        }

        command.execute(sender, args);
    }

    List<String> tabComplete(final CommandSwitcher commandSwitcher, final CommandSender sender, final String[] args) {
        return new ArrayList<>();
    }

    public void registerCommands() {
        final Map<Class<? extends ICommand>, CommandData> commandDataMap = config.getCommandDataMap();
        final Map<Class<? extends ICommand>, CommandSwitcher> commandSwitcherMap = new HashMap<>();

        for (final Map.Entry<Class<? extends ICommand>, CommandData> entry : commandDataMap.entrySet()) {
            final Class<? extends ICommand> command = entry.getKey();
            final CommandData commandData = entry.getValue();
            final CommandSwitcher commandSwitcher = new CommandSwitcher(commandData.getCommandName(), commandData.getCommandAliases(), this);

            commandSwitcherMap.put(command, commandSwitcher);
            CommandRegistry.register(commandSwitcher);
        }

        final Map<CommandSwitcher, ICommand> commandToRegister = new HashMap<>();


        for (final Map.Entry<CommandSwitcher, ICommand> entry : commandToRegister.entrySet()) {
            final ICommand command = entry.getValue();
            final CommandSwitcher commandSwitcher = entry.getKey();

            commandMap.put(commandSwitcher, command);

            plugin.getLogger().info("Registered new command: " + command.getClass().getSimpleName());
            plugin.getLogger().info("Named: " + commandSwitcher.getName());
            plugin.getLogger().info("With aliases: " + String.join(", ", commandSwitcher.getAliases()));
        }
    }
}
