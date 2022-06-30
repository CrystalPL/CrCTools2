package pl.crystalek.crctools.tpa;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import pl.crystalek.crctools.user.model.User;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class TpaRequest {
    User userSendingRequest;
    User userReceivingRequest;
    Location teleportLocation;
    long requestExpiredTime;
    Runnable requestExpiredTimer;
    Runnable teleportTimer;
}
