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
import pl.crystalek.crcapi.database.storage.Storage;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.api.MessageAPIProvider;
import pl.crystalek.crctools.command.*;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.economy.EconomyImpl;
import pl.crystalek.crctools.listener.*;
import pl.crystalek.crctools.storage.Provider;
import pl.crystalek.crctools.storage.mysql.MySQLProvider;
import pl.crystalek.crctools.task.AfkTask;
import pl.crystalek.crctools.user.UserCache;
import pl.crystalek.crctools.user.model.User;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CrCTools extends JavaPlugin {
    Storage<Provider> storage;
    MessageAPI messageAPI;
    Config config;
    UserCache userCache;
    EconomyImpl economy;

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

        storage = new Storage<>(config.getDatabaseConfig(), this);
        if (!storage.initDatabase()) {
            getLogger().severe("Wyłączanie pluginu..");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        storage.initProvider(MySQLProvider.class, null, null);
        final Provider provider = storage.getProvider();

        userCache = new UserCache();
        economy = new EconomyImpl(config, userCache, this, provider);
        economy.applyVault();

        registerListeners();
        registerCommands();
        runTasks();


        Bukkit.getOnlinePlayers().forEach(player -> userCache.addUser(player, new User(player)));

    }

    private void registerCommands() {
        final Map<Class<? extends SingleCommand>, CommandData> commandDataMap = config.getCommandDataMap();

        CommandRegistry.register(new FeedCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new HealCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new GameModeCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new RenameCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new LoreCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new EcoCommand(messageAPI, commandDataMap, economy, userCache, userCache));
    }

    private void registerListeners() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerChatListener(config, userCache), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(config, userCache), this);
        pluginManager.registerEvents(new PlayerCommandListener(config, messageAPI, userCache), this);
        pluginManager.registerEvents(new PlayerJoinListener(config, userCache, messageAPI, this, storage.getProvider()), this);
        pluginManager.registerEvents(new PlayerMoveListener(config, userCache), this);
        pluginManager.registerEvents(new PlayerPickupItemListener(config, userCache), this);
        pluginManager.registerEvents(new PlayerFishListener(config, userCache), this);
        pluginManager.registerEvents(new PlayerQuitListener(config, userCache, messageAPI), this);
    }

    private void runTasks() {
        final BukkitScheduler scheduler = Bukkit.getScheduler();
        if (config.isAntiAfk()) {
            scheduler.runTaskTimerAsynchronously(this, new AfkTask(userCache, config, this), 0, 20L);
        }
    }
}
