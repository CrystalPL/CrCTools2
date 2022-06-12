package pl.crystalek.crctools.afk.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.afk.IAfkPunishment;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class MessageAfkPunishment implements IAfkPunishment {
    MessageAPI messageAPI;
    String messagePath;

    @Override
    public void execute(final Player player) {
        messageAPI.sendMessage(messagePath, player);
    }
}
