package pl.crystalek.crctools.storage.mysql;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.provider.sql.BaseSQLProvider;
import pl.crystalek.crcapi.database.provider.sql.model.SQLFunction;
import pl.crystalek.crcapi.lib.hikari.HikariDataSource;
import pl.crystalek.crctools.storage.Provider;
import pl.crystalek.crctools.user.model.User;
import pl.crystalek.crctools.user.model.UserData;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class MySQLProvider extends BaseSQLProvider implements Provider {
    String insertUser;
    String setMoney;
    String loadUserData;
    String loadUser;
    String saveUser;

    public MySQLProvider(final DatabaseConfig databaseConfig, final HikariDataSource database) {
        super(databaseConfig, database);

        final String prefix = databaseConfig.getPrefix();
        this.insertUser = String.format("INSERT INTO %suser (nickname, uuid, money) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE nickname = ?;", prefix);
        this.setMoney = String.format("UPDATE %suser SET money = ? WHERE uuid = ?;", prefix);
        this.loadUserData = String.format("SELECT uuid, nickname, money FROM %suser;", prefix);
        this.loadUser = String.format("SELECT money FROM %suser WHERE uuid = ?;", prefix);
        this.saveUser = String.format("UPDATE %suser SET money = ? WHERE uuid = ?;", prefix);

        this.createTable();
    }

    @Override
    public void createUser(final User user) {
        executeUpdateAndOpenConnection(insertUser, user.getNickname(), user.getUuid().toString(), user.getMoney(), user.getNickname());
    }

    @Override
    public void setMoney(final UUID playerUUID, final double money) {
        executeUpdateAndOpenConnection(setMoney, money, playerUUID.toString());
    }

    @Override
    public Set<UserData> loadUserData() {
        final SQLFunction<ResultSet, Set<UserData>> function = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return new HashSet<>();
            }

            final Set<UserData> userDataSet = new HashSet<>();
            do {
                final UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                final String nickname = resultSet.getString("nickname");
                final double money = resultSet.getDouble("money");

                userDataSet.add(new UserData(nickname, uuid, money));
            } while (resultSet.next());


            return userDataSet;
        };

        return executeQueryAndOpenConnection(loadUserData, function);
    }

    @Override
    public Optional<User> loadUser(final Player player) {
        final SQLFunction<ResultSet, Optional<User>> function = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return Optional.empty();
            }

            final double money = resultSet.getDouble("money");

            return Optional.of(new User(player, money));
        };

        return executeQueryAndOpenConnection(loadUser, function, player.getUniqueId().toString());
    }

    @Override
    public void saveUser(final User user) {
        executeUpdateAndOpenConnection(saveUser, user.getMoney(), user.getUuid().toString());
    }

    @Override
    public void createTable() {
        final String userTable = "CREATE TABLE IF NOT EXISTS %suser(\n" +
                "    uuid CHAR(36) PRIMARY KEY UNIQUE NOT NULL,\n" +
                "    nickname VARCHAR(16) NOT NULL,\n" +
                "    money DOUBLE\n" +
                ");";

        executeUpdateAndOpenConnection(String.format(userTable, databaseConfig.getPrefix()));
    }
}
