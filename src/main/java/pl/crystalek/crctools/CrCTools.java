package pl.crystalek.crctools;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import pl.crystalek.crcapi.command.CommandRegistry;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.core.config.exception.ConfigLoadException;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.api.MessageAPIProvider;
import pl.crystalek.crctools.command.FeedCommand;
import pl.crystalek.crctools.command.GameModeCommand;
import pl.crystalek.crctools.command.HealCommand;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.listener.*;
import pl.crystalek.crctools.task.AfkTask;
import pl.crystalek.crctools.user.UserCache;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CrCTools extends JavaPlugin {
    MessageAPI messageAPI;
    Config config;
    UserCache userCache;

    @Override
    public void onEnable() {
        //save default languages files
        if (!new File(getDataFolder(), "lang/pl_PL.yml").exists()) {
            saveResource("lang/pl_PL.yml", false); //temporary true
        }

        messageAPI = Bukkit.getServicesManager().getRegistration(MessageAPIProvider.class).getProvider().getLocalizedMessage(this);
        if (!messageAPI.init()) {
            return;
        }

        config = new Config(this, "config.yml", messageAPI);
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

        try {
            config.loadConfig();
        } catch (final ConfigLoadException exception) {
            getLogger().severe(exception.getMessage());
            getLogger().severe("Wyłączanie pluginu..");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        userCache = new UserCache();
        registerListeners();
        registerCommands();
        runTasks();
    }

    private void registerCommands() {
        final Map<Class<? extends SingleCommand>, CommandData> commandDataMap = config.getCommandDataMap();

        CommandRegistry.register(new FeedCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new HealCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new GameModeCommand(messageAPI, commandDataMap));
    }

    private void registerListeners() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerChatListener(config, userCache), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(config, userCache), this);
        pluginManager.registerEvents(new PlayerCommandListener(config, messageAPI, userCache), this);
        pluginManager.registerEvents(new PlayerJoinListener(userCache), this);
        pluginManager.registerEvents(new PlayerMoveListener(config, userCache), this);
        pluginManager.registerEvents(new PlayerPickupItemListener(config, userCache), this);
        pluginManager.registerEvents(new PlayerFishListener(config, userCache), this);
        pluginManager.registerEvents(new PlayerQuitListener(userCache), this);
    }

    private void runTasks() {
        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimerAsynchronously(this, new AfkTask(userCache, config, this), 0, 20L);
    }
}
