package pl.crystalek.crctools.user.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserData {
    final String nickname;
    final UUID uuid;
    double money;
}
