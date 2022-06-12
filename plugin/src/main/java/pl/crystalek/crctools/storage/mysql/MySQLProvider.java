package pl.crystalek.crctools.storage.mysql;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.provider.sql.BaseSQLProvider;
import pl.crystalek.crcapi.lib.hikari.HikariDataSource;
import pl.crystalek.crctools.storage.Provider;

import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class MySQLProvider extends BaseSQLProvider implements Provider {
    String insertUser;
    String setMoney;

    public MySQLProvider(final DatabaseConfig databaseConfig, final HikariDataSource database) {
        super(databaseConfig, database);

        this.insertUser = String.format("INSERT INTO %suser (nickname, uuid, money) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE nickname = ?;", databaseConfig.getPrefix());
        this.setMoney = String.format("UPDATE %suser SET money = ? WHERE uuid = ?;", databaseConfig.getPrefix());
        this.createTable();
    }

    @Override
    public void createUser(final Player player) {
        executeUpdateAndOpenConnection(insertUser, player.getName(), player.getUniqueId().toString(), 0, player.getName());
    }

    @Override
    public void setMoney(final UUID playerUUID, final double money) {
        executeUpdateAndOpenConnection(setMoney, money, playerUUID.toString());
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
