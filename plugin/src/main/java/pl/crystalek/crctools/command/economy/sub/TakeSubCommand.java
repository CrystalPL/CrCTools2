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
import pl.crystalek.crctools.user.UserCache;

import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class TakeSubCommand extends Command {
    Economy economy;
    Config config;
    UserCache userCache;

    public TakeSubCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap, final Economy economy, final Config config, final UserCache userCache) {
        super(messageAPI, commandDataMap);

        this.economy = economy;
        this.config = config;
        this.userCache = userCache;
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
            economy.takeMoney(args[1], money);
        } catch (final EconomyException exception) {
            final String messageExceptionPath;

            final Map<String, Object> errorReplacements = new HashMap<>();

            switch (exception.getEconomyResult()) {
                case USER_NOT_FOUND:
                    messageExceptionPath = "playerNotFound";
                    break;
                case TAKE_NEGATIVE_AMOUNT:
                    messageExceptionPath = "economy.takeNegativeMoney";
                    break;
                case MIN_TRANSFER_BALANCE:
                    messageExceptionPath = "economy.takeMinTransferBalance";
                    errorReplacements.put("{MIN_TRANSFER_BALANCE}", config.getNumberFormatter().format(money));
                    break;
                case MAX_TRANSFER_BALANCE:
                    messageExceptionPath = "economy.takeMaxTransferBalance";
                    errorReplacements.put("{MAX_TRANSFER_BALANCE}", config.getNumberFormatter().format(money));
                    break;
                case NEGATIVE_BALANCE:
                    messageExceptionPath = "economy.takeNegativeBalance";
                    break;
                case MAX_MINUS_BALANCE:
                    messageExceptionPath = "economy.takeMaxMinusBalanceError";
                    errorReplacements.put("{MAX_TAKE_BALANCE}", config.getNumberFormatter().format(Math.abs(-config.getMaxMinusBalance() - userCache.getUserData(args[1]).get().getMoney())));
                    break;
                default:
                    messageExceptionPath = "";
            }

            messageAPI.sendMessage(messageExceptionPath, sender, errorReplacements);
            return;
        }

        final Map<String, Object> takeReplacements = ImmutableMap.of(
                "{PLAYER_NAME}", args[1],
                "{ADMIN_NAME}", sender.getName(),
                "{MONEY}", config.getNumberFormatter().format(money)
        );

        messageAPI.sendMessage("economy.take", sender, takeReplacements);
        messageAPI.sendMessage("economy.takeToPlayer", args[1], takeReplacements);
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
