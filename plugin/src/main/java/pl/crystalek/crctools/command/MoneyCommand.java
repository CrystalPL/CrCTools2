package pl.crystalek.crctools.command;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.command.impl.Command;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.api.economy.Economy;
import pl.crystalek.crctools.api.exception.EconomyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class MoneyCommand extends SingleCommand {
    Economy economy;

    public MoneyCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap, final Economy economy) {
        super(messageAPI, commandDataMap);

        this.economy = economy;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            if (!sender.hasPermission("crc.tools.money.player")) {
                messageAPI.sendMessage("noPermission", sender, ImmutableMap.of("{PERMISSION}", "crc.tools.money.player"));
                return;
            }

            final double balance;
            try {
                balance = economy.getBalance(args[0]);
            } catch (final EconomyException exception) {
                messageAPI.sendMessage("playerNotFound", sender);
                return;
            }

            final Map<String, Object> replacements = ImmutableMap.of(
                    "{PLAYER_NAME}", args[0],
                    "{BALANCE}", balance
            );

            messageAPI.sendMessage("money.playerMoney", sender, replacements);
            return;
        }

        if (!(sender instanceof Player)) {
            messageAPI.sendMessage("noConsole", sender);
            return;
        }

        final double balance = economy.getBalance((Player) sender);
        messageAPI.sendMessage("money.money", sender, ImmutableMap.of("{BALANCE}", balance));
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 1 && sender.hasPermission("crc.tools.money.player")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(player -> StringUtils.startsWithIgnoreCase(player, args[0])).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "crc.tools.money";
    }

    @Override
    public boolean isUseConsole() {
        return true;
    }

    @Override
    public String getCommandUsagePath() {
        return "money.usage";
    }

    @Override
    public int maxArgumentLength() {
        return 1;
    }

    @Override
    public int minArgumentLength() {
        return 0;
    }
}
