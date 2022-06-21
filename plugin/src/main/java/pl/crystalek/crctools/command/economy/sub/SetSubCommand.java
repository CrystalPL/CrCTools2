package pl.crystalek.crctools.command.economy.sub;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.command.impl.Command;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.core.util.NumberUtil;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.api.economy.Economy;
import pl.crystalek.crctools.api.exception.EconomyException;
import pl.crystalek.crctools.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class SetSubCommand extends Command {
    Economy economy;
    Config config;

    public SetSubCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap, final Economy economy, final Config config) {
        super(messageAPI, commandDataMap);

        this.economy = economy;
        this.config = config;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Optional<Double> moneyOptional = NumberUtil.getDouble(args[2]);
        if (!moneyOptional.isPresent()) {
            messageAPI.sendMessage("economy.moneyValueError", sender);
            return;
        }

        final Double money = moneyOptional.get();
        try {
            economy.setMoney(args[1], money);
        } catch (final EconomyException exception) {
            final String messageExceptionPath;

            switch (exception.getEconomyResult()) {
                case USER_NOT_FOUND:
                    messageExceptionPath = "playerNotFound";
                    break;
                case NEGATIVE_BALANCE:
                    messageExceptionPath = "economy.setNegativeBalance";
                    break;
                case MAX_MINUS_BALANCE:
                    messageExceptionPath = "economy.setMaxMinusBalance";
                    break;
                case MAX_BALANCE:
                    messageExceptionPath = "economy.setMaxBalance";
                    break;
                default:
                    messageExceptionPath = "";
            }

            messageAPI.sendMessage(messageExceptionPath, sender);
            return;
        }

        final Map<String, Object> replacements = ImmutableMap.of(
                "{PLAYER_NAME}", args[1],
                "{ADMIN_NAME}", sender.getName(),
                "{MONEY}", config.getNumberFormatter().format(money)
        );

        messageAPI.sendMessage("economy.setBalance", sender, replacements);
        messageAPI.sendMessage("economy.setBalanceToPlayer", args[1], replacements);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(player -> StringUtils.startsWithIgnoreCase(player, args[1])).collect(Collectors.toList());
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
        return 3;
    }
}
