package pl.crystalek.crctools.command.economy.sub;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.command.impl.Command;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.api.economy.Economy;
import pl.crystalek.crctools.api.economy.EconomyResult;
import pl.crystalek.crctools.api.exception.EconomyException;
import pl.crystalek.crctools.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ResetSubCommand extends Command {
    Economy economy;
    Config config;

    public ResetSubCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap, final Economy economy, final Config config) {
        super(messageAPI, commandDataMap);

        this.economy = economy;
        this.config = config;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 2) {
            try {
                economy.setMoney(args[1], 0);
            } catch (final EconomyException exception) {
                final Map<String, Object> replacements = ImmutableMap.of(
                        "{PLAYER_NAME}", args[1],
                        "{ERROR}", exception.getMessage()
                );

                messageAPI.sendMessage(exception.getEconomyResult() == EconomyResult.USER_NOT_FOUND ? "playerNotFound" : "economy.resetError", sender, replacements);
                return;
            }

            messageAPI.sendMessage("economy.reset", sender, ImmutableMap.of("{PLAYER_NAME}", args[1]));
            messageAPI.sendMessage("economy.resetToPlayer", args[1], ImmutableMap.of("{ADMIN_NAME}", sender.getName()));
            return;
        }

        if (!args[2].equalsIgnoreCase("true")) {
            messageAPI.sendMessage("economy.usage", sender);
            return;
        }

        try {
            economy.setMoney(args[1], config.getStartingBalance());
        } catch (final EconomyException exception) {
            final Map<String, Object> resetReplacements = ImmutableMap.of(
                    "{PLAYER_NAME}", args[1],
                    "{ERROR}", exception.getMessage()
            );

            messageAPI.sendMessage(exception.getEconomyResult() == EconomyResult.USER_NOT_FOUND ? "playerNotFound" : "economy.resetError", sender, resetReplacements);
            return;
        }

        messageAPI.sendMessage("economy.resetDefault", sender, ImmutableMap.of("{PLAYER_NAME}", args[1]));
        messageAPI.sendMessage("economy.resetDefaultToPlayer", args[1], ImmutableMap.of("{ADMIN_NAME}", sender.getName()));
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(player -> StringUtils.startsWithIgnoreCase(player, args[1])).collect(Collectors.toList());
        }

        if (args.length == 3) {
            return ImmutableList.of("true");
        }

        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "";
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
