package pl.crystalek.crctools.command;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.command.impl.Command;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class HealCommand extends SingleCommand {

    public HealCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap) {
        super(messageAPI, commandDataMap);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            if (!sender.hasPermission("crc.tools.heal.player")) {
                messageAPI.sendMessage("noPermission", sender, ImmutableMap.of("{PERMISSION}", "crc.tools.heal.player"));
                return;
            }

            final Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                messageAPI.sendMessage("playerNotFound", sender);
                return;
            }

            if (player.getHealth() == 20D) {
                messageAPI.sendMessage("heal.playerFullError", sender, ImmutableMap.of("{PLAYER_NAME}", player.getName()));
                return;
            }

            player.setHealth(20D);

            messageAPI.sendMessage("heal.player", player, ImmutableMap.of("{ADMIN_NAME}", sender.getName()));
            messageAPI.sendMessage("heal.playerToAdmin", sender, ImmutableMap.of("{PLAYER_NAME}", player.getName()));
            return;
        }

        if (!(sender instanceof Player)) {
            messageAPI.sendMessage("noConsole", sender);
            return;
        }

        final Player playerSender = (Player) sender;
        if (playerSender.getHealth() == 20D) {
            messageAPI.sendMessage("heal.yourselfFullError", sender);
            return;
        }

        playerSender.setHealth(20D);

        messageAPI.sendMessage("heal.yourself", sender);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 1 && sender.hasPermission("crc.tools.heal.player")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(player -> StringUtils.startsWithIgnoreCase(player, args[0])).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public boolean isUseConsole() {
        return true;
    }

    @Override
    public String getCommandUsagePath() {
        return "heal.usage";
    }

    @Override
    public int maxArgumentLength() {
        return 1;
    }

    @Override
    public int minArgumentLength() {
        return 0;
    }

    @Override
    public String getPermission() {
        return "crc.tools.heal";
    }

}
