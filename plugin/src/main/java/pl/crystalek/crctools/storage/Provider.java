package pl.crystalek.crctools.storage;

import org.bukkit.entity.Player;
import pl.crystalek.crcapi.database.provider.BaseProvider;

import java.util.UUID;

public interface Provider extends BaseProvider {

    void createUser(final Player player);

    void setMoney(final UUID playerUUID, final double money);
}
