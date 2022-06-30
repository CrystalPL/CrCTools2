package pl.crystalek.crctools.hook;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VaultHook {
    @Getter
    Permission permission;
    @Getter
    boolean enableVault;

    public void init(final JavaPlugin plugin) {
        try {
            final RegisteredServiceProvider<Permission> registration = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
            if (registration == null) {
                enableVault = false;
                throw new ClassNotFoundException();
            }

            permission = registration.getProvider();
            enableVault = true;

            plugin.getLogger().info("Vault został poprawnie załadowany");
        } catch (final NoClassDefFoundError | ClassNotFoundException exception) {
            plugin.getLogger().severe("Nie odnaleziono pluginu Vault lub pluginu implementującego klasę Permission!");
        }
    }
}