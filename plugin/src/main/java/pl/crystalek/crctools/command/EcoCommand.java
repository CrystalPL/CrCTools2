package pl.crystalek.crctools.command;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.core.util.NumberUtil;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.api.economy.Economy;
import pl.crystalek.crctools.api.economy.EconomyResult;
import pl.crystalek.crctools.api.exception.EconomyException;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.UserCache;

import java.util.*;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class EcoCommand extends SingleCommand {
    Economy economy;
    Config config;
    UserCache userCache;
    Set<String> argumentList = ImmutableSet.of("set", "give", "take", "reset");

    public EcoCommand(final MessageAPI messageAPI, final Map<Class<? extends SingleCommand>, CommandData> commandDataMap, final Economy economy, final Config config, final UserCache userCache) {
        super(messageAPI, commandDataMap);

        this.economy = economy;
        this.config = config;
        this.userCache = userCache;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (!argumentList.contains(args[0].toLowerCase())) {
            messageAPI.sendMessage("economy.usage", sender);
            return;
        }

        if (args.length == 2) {
            if (!args[0].equalsIgnoreCase("reset")) {
                messageAPI.sendMessage("economy.usage", sender);
                return;
            }

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

        final Optional<Double> moneyOptional = NumberUtil.getDouble(args[2]);
        if (!moneyOptional.isPresent()) {
            messageAPI.sendMessage("economy.moneyValueError", sender);
            return;
        }
        final Double money = moneyOptional.get();

        switch (args[0].toLowerCase()) {
            case "set":
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

                final Map<String, Object> setReplacements = ImmutableMap.of(
                        "{PLAYER_NAME}", args[1],
                        "{ADMIN_NAME}", sender.getName(),
                        "{MONEY}", args[2]
                );

                messageAPI.sendMessage("economy.setBalance", sender, setReplacements);
                messageAPI.sendMessage("economy.setBalanceToPlayer", args[1], setReplacements);
                break;
            case "give":
                try {
                    economy.giveMoney(args[1], money);
                } catch (final EconomyException exception) {
                    final String messageExceptionPath;

                    Map<String, Object> errorReplacements = new HashMap<>();

                    switch (exception.getEconomyResult()) {
                        case USER_NOT_FOUND:
                            messageExceptionPath = "playerNotFound";
                            break;
                        case GIVE_NEGATIVE_AMOUNT:
                            messageExceptionPath = "economy.giveNegativeMoney";
                            break;
                        case MIN_TRANSFER_BALANCE:
                            messageExceptionPath = "economy.giveMinTransferBalance";
                            errorReplacements.put("{MIN_TRANSFER_BALANCE}", money);
                            break;
                        case MAX_TRANSFER_BALANCE:
                            messageExceptionPath = "economy.giveMaxTransferBalance";
                            errorReplacements.put("{MAX_TRANSFER_BALANCE}", money);
                            break;
                        case MAX_BALANCE:
                            messageExceptionPath = "economy.maxBalance";
                            errorReplacements.put("{MAX_GIVE_BALANCE}", config.getMaxBalance() - userCache.getUserData(args[1]).get().getMoney()); //TODO
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
                        "{MONEY}", args[2]
                );

                messageAPI.sendMessage("economy.give", sender, giveReplacements);
                messageAPI.sendMessage("economy.giveToPlayer", args[1], giveReplacements);
                break;
            case "take":
                try {
                    economy.takeMoney(args[1], money);
                } catch (final EconomyException exception) {
                    final String messageExceptionPath;

                    Map<String, Object> errorReplacements = new HashMap<>();

                    switch (exception.getEconomyResult()) {
                        case USER_NOT_FOUND:
                            messageExceptionPath = "playerNotFound";
                            break;
                        case TAKE_NEGATIVE_AMOUNT:
                            messageExceptionPath = "economy.takeNegativeMoney";
                            break;
                        case MIN_TRANSFER_BALANCE:
                            messageExceptionPath = "economy.takeMinTransferBalance";
                            errorReplacements.put("{MIN_TRANSFER_BALANCE}", money);
                            break;
                        case MAX_TRANSFER_BALANCE:
                            messageExceptionPath = "economy.takeMaxTransferBalance";
                            errorReplacements.put("{MAX_TRANSFER_BALANCE}", money);
                            break;
                        case NEGATIVE_BALANCE:
                            messageExceptionPath = "economy.takeNegativeBalance";
                            break;
                        case MAX_MINUS_BALANCE:
                            messageExceptionPath = "economy.takeMaxMinusBalanceError";
                            errorReplacements.put("{MAX_TAKE_BALANCE}", -config.getMaxMinusBalance() - userCache.getUserData(args[1]).get().getMoney()); //TODO
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
                        "{MONEY}", args[2]
                );

                messageAPI.sendMessage("economy.take", sender, takeReplacements);
                messageAPI.sendMessage("economy.takeToPlayer", args[1], takeReplacements);
                break;
            case "reset":
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
                break;
        }
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        return null;
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
