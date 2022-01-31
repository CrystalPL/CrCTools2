package pl.crystalek.crctools.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.loader.CommandLoader;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.core.config.ConfigHelper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Config extends ConfigHelper {
    Map<Class<? extends SingleCommand>, CommandData> commandDataMap;
    List<String> blockedCommandList;

    public Config(final JavaPlugin plugin, final String fileName) {
        super(plugin, fileName);
    }

    @SneakyThrows
    public boolean loadConfig() {
        final Optional<Map<Class<? extends SingleCommand>, CommandData>> commandDataMapOptional = CommandLoader.loadCommands(configuration.getConfigurationSection("command"), plugin.getClass().getClassLoader(), plugin);
        if (!commandDataMapOptional.isPresent()) {
            return false;
        }
        this.commandDataMap = commandDataMapOptional.get();

        this.blockedCommandList = configuration.getStringList("blockedCommands");

        return true;
    }

}