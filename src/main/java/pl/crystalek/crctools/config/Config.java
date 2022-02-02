package pl.crystalek.crctools.config;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.loader.CommandLoader;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.core.config.ConfigHelper;
import pl.crystalek.crcapi.core.config.ConfigParserUtil;
import pl.crystalek.crcapi.core.config.exception.ConfigLoadException;
import pl.crystalek.crcapi.core.util.ColorUtil;
import pl.crystalek.crcapi.core.util.NumberUtil;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.afk.AfkActionType;
import pl.crystalek.crctools.afk.IAfkPunishment;
import pl.crystalek.crctools.afk.impl.CommandAfkPunishment;
import pl.crystalek.crctools.afk.impl.MessageAfkPunishment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Config extends ConfigHelper {
    final MessageAPI messageAPI;
    Map<Class<? extends SingleCommand>, CommandData> commandDataMap;
    boolean commandNotFoundMessage;
    List<String> blockedCommandList;
    boolean antiAfk;
    boolean cancelAfkWhenUseCommand;
    boolean cancelAfkWhenSendMessage;
    boolean cancelAfkWhenMove;
    boolean cancelAfkWhenMoveY;
    boolean attackEntityWhileAfk;
    boolean pickupItemsWhileAfk;
    boolean fishWhileAfk;
    long afkTime;
    Map<Long, IAfkPunishment> afkActionTypeMap;
    boolean joinMessage;
    boolean quitMessage;
    boolean motdMessage;
    boolean commandCoolDown;
    long commandCoolDownTime;
    String timeDelimiter;
    boolean shortFormTime;

    public Config(final JavaPlugin plugin, final String fileName, final MessageAPI messageAPI) {
        super(plugin, fileName);

        this.messageAPI = messageAPI;
    }

    public void loadConfig() throws ConfigLoadException {
        this.commandDataMap = CommandLoader.loadCommands(configuration.getConfigurationSection("command"), plugin.getClass().getClassLoader(), plugin);
        this.commandNotFoundMessage = ConfigParserUtil.getBoolean(configuration, "commandNotFoundMessage");
        this.blockedCommandList = configuration.getStringList("blockedCommands");
        this.antiAfk = ConfigParserUtil.getBoolean(configuration, "antiAfk");
        this.cancelAfkWhenUseCommand = ConfigParserUtil.getBoolean(configuration, "cancelAfkWhenUseCommand");
        this.cancelAfkWhenSendMessage = ConfigParserUtil.getBoolean(configuration, "cancelAfkWhenSendMessage");
        this.cancelAfkWhenMove = ConfigParserUtil.getBoolean(configuration, "cancelAfkWhenMove");
        this.cancelAfkWhenMoveY = ConfigParserUtil.getBoolean(configuration, "cancelAfkWhenMoveY");
        this.attackEntityWhileAfk = ConfigParserUtil.getBoolean(configuration, "attackEntityWhileAfk");
        this.pickupItemsWhileAfk = ConfigParserUtil.getBoolean(configuration, "pickupItemsWhileAfk");
        this.fishWhileAfk = ConfigParserUtil.getBoolean(configuration, "fishWhileAfk");
        this.afkTime = ConfigParserUtil.getLong(configuration, "afkTime") * 1000L;
        this.afkActionTypeMap = loadAfkActionTypeMap();
        this.joinMessage = ConfigParserUtil.getBoolean(configuration, "joinMessage");
        this.quitMessage = ConfigParserUtil.getBoolean(configuration, "quitMessage");
        this.motdMessage = ConfigParserUtil.getBoolean(configuration, "motdMessage");
        this.commandCoolDown = ConfigParserUtil.getBoolean(configuration, "commandCoolDown");
        this.commandCoolDownTime = ConfigParserUtil.getLong(configuration, "commandCoolDownTime") * 1000L;
        this.timeDelimiter = ConfigParserUtil.getString(configuration, "timeDelimiter");
        this.shortFormTime = ConfigParserUtil.getBoolean(configuration, "shortFormTime");
    }

    private Map<Long, IAfkPunishment> loadAfkActionTypeMap() throws ConfigLoadException {
        final Map<Long, IAfkPunishment> afkActionTypeMap = new HashMap<>();

        final ConfigurationSection afkActionConfiguration = configuration.getConfigurationSection("afkAction");
        for (final String afkTime : afkActionConfiguration.getKeys(false)) {
            final Optional<Long> afkTimeOptional = NumberUtil.getLong(afkTime);
            if (!afkTimeOptional.isPresent()) {
                throw new ConfigLoadException("Pole " + afkTime + " nie jest liczbą całkowitą!");
            }
            final long time = afkTimeOptional.get() * 1000L;

            final String stringActionType = ConfigParserUtil.getString(afkActionConfiguration, afkTime + ".actionType");
            final AfkActionType afkActionType;
            try {
                afkActionType = AfkActionType.valueOf(stringActionType.toUpperCase());
            } catch (final IllegalArgumentException exception) {
                throw new ConfigLoadException("Nie odnaleziono akcji typu: " + stringActionType);
            }

            final IAfkPunishment afkPunishment;
            switch (afkActionType) {
                case MESSAGE:
                    afkPunishment = new MessageAfkPunishment(messageAPI, ConfigParserUtil.getString(afkActionConfiguration, afkTime + ".action"));
                    break;
                case COMMAND:
                    final Object objectAction = afkActionConfiguration.get(afkTime + ".action");
                    final List<String> commandList = objectAction instanceof List ? (List<String>) objectAction : ImmutableList.of((String) objectAction);

                    afkPunishment = new CommandAfkPunishment(ColorUtil.color(commandList));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + afkActionType);
            }

            afkActionTypeMap.put(time, afkPunishment);
        }

        return afkActionTypeMap;
    }

}