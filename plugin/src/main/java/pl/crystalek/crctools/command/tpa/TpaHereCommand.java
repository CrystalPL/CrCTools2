package pl.crystalek.crctools.command.tpa;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import pl.crystalek.crcapi.command.impl.Command;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.user.UserCache;

import java.util.List;
import java.util.Map;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class TpaHereCommand extends SingleCommand {
    UserCache userCache;

    public TpaHereCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap, final UserCache userCache) {
        super(messageAPI, commandDataMap);

        this.userCache = userCache;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {

    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        return null;
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean isUseConsole() {
        return false;
    }

    @Override
    public String getCommandUsagePath() {
        return null;
    }

    @Override
    public int maxArgumentLength() {
        return 0;
    }

    @Override
    public int minArgumentLength() {
        return 0;
    }
}
