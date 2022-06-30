package pl.crystalek.crctools.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserData {
    protected final UUID uuid;
    private final String nickname;
    private double money;
}
