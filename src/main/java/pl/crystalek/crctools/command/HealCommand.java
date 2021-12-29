package pl.crystalek.crctools.command;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.message.MessageAPI;
import pl.crystalek.crctools.command.model.ICommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class HealCommand implements ICommand {
    MessageAPI messageAPI;

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
        if (args.length == 1) {
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