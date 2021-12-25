package pl.crystalek.crctools.command;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.message.MessageAPI;
import pl.crystalek.crcapi.util.NumberUtil;
import pl.crystalek.crctools.command.model.ICommand;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GameModeCommand implements ICommand {
    Set<String> argumentList = ImmutableSet.of("survival, creative, adventure, spectator");
    MessageAPI messageAPI;

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Optional<Integer> intGameModeOptional = NumberUtil.getInt(args[0]);

        final GameMode gameMode;
        if (intGameModeOptional.isPresent()) {
            gameMode = GameMode.getByValue(intGameModeOptional.get());
            if (gameMode == null) {
                messageAPI.sendMessage("gamemode.notFound", sender);
                return;
            }
        } else {
            try {
                gameMode = GameMode.valueOf(args[0].toUpperCase());
            } catch (final IllegalArgumentException exception) {
                messageAPI.sendMessage("gamemode.notFound", sender);
                return;
            }
        }

        if (args.length == 2) {
            final Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                messageAPI.sendMessage("playerNotFound", sender);
                return;
            }

            if (!setGameMode(player, gameMode)) {
                messageAPI.sendMessage("gamemode.playerSameGameMode", sender, ImmutableMap.of("{PLAYER_NAME}", player.getName()));
                return;
            }

            final Map<String, Object> replacements = ImmutableMap.of(
                    "{GAMEMODE}", gameMode.name().toLowerCase(),
                    "{ADMIN_NAME}", sender.getName(),
                    "{PLAYER_NAME}", player.getName()
            );

            messageAPI.sendMessage("gamemode.setPlayerGameMode", player, replacements);
            messageAPI.sendMessage("gamemode.setPlayerToAdmin", sender, replacements);
            return;
        }

        if (!(sender instanceof Player)) {
            messageAPI.sendMessage("noConsole", sender);
            return;
        }

        if (setGameMode((Player) sender, gameMode)) {
            messageAPI.sendMessage("gamemode.setGameMode", sender, ImmutableMap.of("{GAMEMODE}", gameMode.name().toLowerCase()));
        } else {
            messageAPI.sendMessage("gamemode.sameGameMode", sender);
        }

    }

    private boolean setGameMode(final Player player, final GameMode gameMode) {
        if (player.getGameMode() == gameMode) {
            return false;
        }

        player.setGameMode(gameMode);
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            return argumentList.stream().filter(argument -> StringUtils.startsWithIgnoreCase(argument, args[0])).collect(Collectors.toList());
        }

        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(player -> StringUtils.startsWithIgnoreCase(player, args[1])).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public boolean isUseConsole() {
        return true;
    }

    @Override
    public String getCommandUsagePath() {
        return "gamemode.usage";
    }

    @Override
    public int maxArgumentLength() {
        return 2;
    }

    @Override
    public int minArgumentLength() {
        return 1;
    }

    @Override
    public String getPermission() {
        return "crc.tools.gamemode";
    }

}
