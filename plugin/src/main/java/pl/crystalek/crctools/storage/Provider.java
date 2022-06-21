package pl.crystalek.crctools.storage;

import org.bukkit.entity.Player;
import pl.crystalek.crcapi.database.provider.BaseProvider;
import pl.crystalek.crctools.user.model.User;
import pl.crystalek.crctools.user.model.UserData;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Provider extends BaseProvider {

    void createUser(final User user);

    void setMoney(final UUID playerUUID, final double money);

    Set<UserData> loadUserData();

    Optional<User> loadUser(final Player player);

    void saveUser(final User user);
}
