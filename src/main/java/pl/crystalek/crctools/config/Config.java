package pl.crystalek.crctools.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crctools.command.model.CommandData;
import pl.crystalek.crctools.command.model.ICommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Config {
    final FileConfiguration config;
    final JavaPlugin plugin;
    Map<Class<? extends ICommand>, CommandData> commandDataMap;

    public boolean load() {
        this.commandDataMap = loadCommands();
        return commandDataMap != null;
    }

    private Map<Class<? extends ICommand>, CommandData> loadCommands() {
        final Map<Class<? extends ICommand>, CommandData> commandDataMap = new HashMap<>();

        final ConfigurationSection commandsConfigurationSection = config.getConfigurationSection("command");
        for (final String command : commandsConfigurationSection.getKeys(false)) {
            final ConfigurationSection commandConfigurationSection = commandsConfigurationSection.getConfigurationSection(command);

            final String commandName = commandConfigurationSection.getString("name");
            final List<String> commandAliases = Arrays.asList(commandConfigurationSection.getString("aliases").split(", "));

            if (commandName == null || commandAliases == null) {
                plugin.getLogger().severe("Wystąpił błąd podczas ładowania komendy: " + command);
                return null;
            }

            final Class<? extends ICommand> commandClass;
            try {
                commandClass = (Class<? extends ICommand>) Class.forName("pl.crystalek.crctools.command." + command);
            } catch (final ClassNotFoundException exception) {
                plugin.getLogger().severe("Wystąpił błąd podczas ładowania komendy: " + command);
                plugin.getLogger().severe("Nie odnaleziono klasy: " + command);
                return null;
            }

            commandDataMap.put(commandClass, new CommandData(commandName, commandAliases));
        }

        return commandDataMap;
    }
}