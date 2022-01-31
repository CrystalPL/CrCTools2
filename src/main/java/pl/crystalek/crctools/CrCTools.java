package pl.crystalek.crctools;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.command.CommandRegistry;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.api.MessageAPIProvider;
import pl.crystalek.crctools.command.FeedCommand;
import pl.crystalek.crctools.command.GameModeCommand;
import pl.crystalek.crctools.command.HealCommand;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.listener.PlayerCommandListener;

import java.io.IOException;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CrCTools extends JavaPlugin {
    MessageAPI messageAPI;
    Config config;

    @Override
    public void onEnable() {
        config = new Config(this, "config.yml");
        try {
            config.checkExist();
            config.load();
        } catch (final IOException exception) {
            getLogger().severe("Nie udało się utworzyć pliku konfiguracyjnego..");
            getLogger().severe("Wyłączanie pluginu..");
            Bukkit.getPluginManager().disablePlugin(this);
            exception.printStackTrace();
            return;
        }

        if (!config.loadConfig()) {
            getLogger().severe("Wyłączanie pluginu..");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        //save default languages files
//        if (!new File(getDataFolder(), "lang/pl_PL.yml").exists()) {
        saveResource("lang/pl_PL.yml", true); //temporary true
//        }

        messageAPI = Bukkit.getServicesManager().getRegistration(MessageAPIProvider.class).getProvider().getLocalizedMessage(this);
        if (!messageAPI.init()) {
            return;
        }

        registerListeners();
    }

    private void registerCommands() {
        final Map<Class<? extends SingleCommand>, CommandData> commandDataMap = config.getCommandDataMap();

        CommandRegistry.register(new FeedCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new HealCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new GameModeCommand(messageAPI, commandDataMap));
    }

    private void registerListeners() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerCommandListener(config, messageAPI), this);
    }
}
