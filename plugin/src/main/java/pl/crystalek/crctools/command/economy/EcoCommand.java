package pl.crystalek.crctools.command.economy;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.command.impl.Command;
import pl.crystalek.crcapi.command.impl.MultiCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.api.economy.Economy;
import pl.crystalek.crctools.command.economy.sub.GiveSubCommand;
import pl.crystalek.crctools.command.economy.sub.ResetSubCommand;
import pl.crystalek.crctools.command.economy.sub.SetSubCommand;
import pl.crystalek.crctools.command.economy.sub.TakeSubCommand;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.UserCache;

import java.util.Map;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class EcoCommand extends MultiCommand {
    public EcoCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap, final Economy economy, final Config config, final UserCache userCache) {
        super(messageAPI, commandDataMap);

        registerSubCommand(new GiveSubCommand(messageAPI, commandDataMap, economy, config, userCache));
        registerSubCommand(new ResetSubCommand(messageAPI, commandDataMap, economy, config));
        registerSubCommand(new SetSubCommand(messageAPI, commandDataMap, economy, config));
        registerSubCommand(new TakeSubCommand(messageAPI, commandDataMap, economy, config, userCache));
    }

    @Override
    public String getPermission() {
        return "crc.tools.economy";
    }

    @Override
    public boolean isUseConsole() {
        return true;
    }

    @Override
    public String getCommandUsagePath() {
        return "economy.usage";
    }

    @Override
    public int maxArgumentLength() {
        return 3;
    }

    @Override
    public int minArgumentLength() {
        return 2;
    }
}
