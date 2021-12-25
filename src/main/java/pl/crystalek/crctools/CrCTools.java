package pl.crystalek.crctools;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.config.ConfigHelper;
import pl.crystalek.crcapi.message.MessageAPI;
import pl.crystalek.crcapi.messagei18n.LocalizedMessageAPI;
import pl.crystalek.crctools.command.CommandManager;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.listener.PlayerCommandListener;

import java.io.File;
import java.io.IOException;

public final class CrCTools extends JavaPlugin {

    @Override
    public void onEnable() {
        final ConfigHelper configHelper = new ConfigHelper("config.yml", this);
        try {
            configHelper.checkExist();
            configHelper.load();
        } catch (final IOException exception) {
            Bukkit.getLogger().severe("Nie udało się utworzyć pliku konfiguracyjnego..");
            Bukkit.getLogger().severe("Wyłączanie pluginu..");
            Bukkit.getPluginManager().disablePlugin(this);
            exception.printStackTrace();
            return;
        }

        final Config config = new Config(configHelper.getConfiguration(), this);
        if (!config.load()) {
            Bukkit.getLogger().severe("Wyłączanie pluginu..");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        //save default languages files
        if (!new File(getDataFolder(), "lang/pl_PL.yml").exists()) {
            saveResource("lang/pl_PL.yml", false);
        }

        final MessageAPI messageAPI = new LocalizedMessageAPI(this);
        if (!messageAPI.init()) {
            return;
        }

        final CommandManager commandManager = new CommandManager(config, this, messageAPI);
        commandManager.registerCommands();
        registerListeners(config, messageAPI);
    }

    private void registerListeners(final Config config, final MessageAPI messageAPI) {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerCommandListener(config, messageAPI), this);
    }
}
