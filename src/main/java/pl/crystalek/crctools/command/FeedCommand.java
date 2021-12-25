package pl.crystalek.crctools.command;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
public final class FeedCommand implements ICommand {
    MessageAPI messageAPI;

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            if (!sender.hasPermission("crc.tools.feed.player")) {
                messageAPI.sendMessage("noPermission", sender, ImmutableMap.of("{PERMISSION}", "crc.tools.feed.player"));
                return;
            }

            final Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                messageAPI.sendMessage("playerNotFound", sender);
                return;
            }

            if (player.getFoodLevel() == 20D) {
                messageAPI.sendMessage("feed.playerFullError", sender, ImmutableMap.of("{PLAYER_NAME}", player.getName()));
                return;
            }

            player.setFoodLevel(20);

            messageAPI.sendMessage("feed.player", player, ImmutableMap.of("{ADMIN_NAME}", sender.getName()));
            messageAPI.sendMessage("feed.playerToAdmin", sender, ImmutableMap.of("{PLAYER_NAME}", player.getName()));
            return;
        }

        if (!(sender instanceof Player)) {
            messageAPI.sendMessage("noConsole", sender);
            return;
        }

        final Player playerSender = (Player) sender;
        if (playerSender.getFoodLevel() == 20D) {
            messageAPI.sendMessage("feed.yourselfFullError", sender);
            return;
        }

        playerSender.setFoodLevel(20);

        messageAPI.sendMessage("feed.yourself", sender);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(player -> player.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public boolean isUseConsole() {
        return true;
    }

    @Override
    public String getCommandUsagePath() {
        return "feed.usage";
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
        return "crc.tools.feed";
    }
}
