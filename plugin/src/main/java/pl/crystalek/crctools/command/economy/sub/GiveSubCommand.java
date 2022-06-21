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
public final class GiveSubCommand extends Command {
    Economy economy;
    Config config;
    UserCache userCache;

    public GiveSubCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap, final Economy economy, final Config config, final UserCache userCache) {
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
            economy.giveMoney(args[1], money);
        } catch (final EconomyException exception) {
            final String messageExceptionPath;

            final Map<String, Object> errorReplacements = new HashMap<>();

            switch (exception.getEconomyResult()) {
                case USER_NOT_FOUND:
                    messageExceptionPath = "playerNotFound";
                    break;
                case GIVE_NEGATIVE_AMOUNT:
                    messageExceptionPath = "economy.giveNegativeMoney";
                    break;
                case MIN_TRANSFER_BALANCE:
                    messageExceptionPath = "economy.giveMinTransferBalance";
                    errorReplacements.put("{MIN_TRANSFER_BALANCE}", config.getNumberFormatter().format(config.getMinTransferBalance()));
                    break;
                case MAX_TRANSFER_BALANCE:
                    messageExceptionPath = "economy.giveMaxTransferBalance";
                    errorReplacements.put("{MAX_TRANSFER_BALANCE}", config.getNumberFormatter().format(config.getMaxTransferBalance()));
                    break;
                case MAX_BALANCE:
                    messageExceptionPath = "economy.maxBalance";
                    errorReplacements.put("{MAX_GIVE_BALANCE}", config.getNumberFormatter().format(config.getMaxBalance() - userCache.getUserData(args[1]).get().getMoney()));
                    break;
                default:
                    messageExceptionPath = "";
            }

            messageAPI.sendMessage(messageExceptionPath, sender, errorReplacements);
            return;
        }

        final Map<String, Object> giveReplacements = ImmutableMap.of(
                "{PLAYER_NAME}", args[1],
                "{ADMIN_NAME}", sender.getName(),
                "{MONEY}", config.getNumberFormatter().format(money)
        );

        messageAPI.sendMessage("economy.give", sender, giveReplacements);
        messageAPI.sendMessage("economy.giveToPlayer", args[1], giveReplacements);
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
