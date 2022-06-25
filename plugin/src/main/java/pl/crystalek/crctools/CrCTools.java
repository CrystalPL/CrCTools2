package pl.crystalek.crctools;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import pl.crystalek.crcapi.command.CommandRegistry;
import pl.crystalek.crcapi.command.impl.Command;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.core.config.exception.ConfigLoadException;
import pl.crystalek.crcapi.database.storage.Storage;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.api.MessageAPIProvider;
import pl.crystalek.crctools.command.*;
import pl.crystalek.crctools.command.economy.EcoCommand;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.economy.EconomyImpl;
import pl.crystalek.crctools.listener.*;
import pl.crystalek.crctools.storage.Provider;
import pl.crystalek.crctools.storage.mysql.MySQLProvider;
import pl.crystalek.crctools.task.AfkTask;
import pl.crystalek.crctools.user.UserCache;
import pl.crystalek.crctools.user.model.UserData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

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

        userCache = loadUsers();
        economy = new EconomyImpl(config, userCache, this, provider);
        economy.applyVault();

        registerListeners();
        registerCommands();
        runTasks();


        Bukkit.getOnlinePlayers().forEach(userCache::createUser);
    }

    @Override
    public void onDisable() {
        if (storage != null & userCache != null) {
            userCache.getUserMap().values().forEach(storage.getProvider()::saveUser);

            storage.close();
        }
    }

    private void registerCommands() {
        final Map<Class<? extends Command>, CommandData> commandDataMap = config.getCommandDataMap();

        CommandRegistry.register(new FeedCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new HealCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new GameModeCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new RenameCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new LoreCommand(messageAPI, commandDataMap));
        CommandRegistry.register(new EcoCommand(messageAPI, commandDataMap, economy, config, userCache));
        CommandRegistry.register(new MoneyCommand(messageAPI, commandDataMap, economy));
    }

    private void registerListeners() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerChatListener(config, userCache), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(config, userCache), this);
        pluginManager.registerEvents(new PlayerCommandListener(config, messageAPI, userCache), this);
        pluginManager.registerEvents(new PlayerJoinListener(config, userCache, messageAPI, this), this);
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

    private UserCache loadUsers() {
        final Map<UUID, UserData> userDataByUUIDCache = new HashMap<>();
        final Map<String, UserData> userDataByNicknameCache = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        final Provider provider = storage.getProvider();
        for (final UserData userData : provider.loadUserData()) {
            userDataByUUIDCache.put(userData.getUuid(), userData);
            userDataByNicknameCache.put(userData.getNickname(), userData);
        }

        return new UserCache(userDataByUUIDCache, userDataByNicknameCache, provider, config);
    }
}
