package pl.crystalek.crctools.api.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crctools.api.economy.EconomyResult;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class EconomyException extends RuntimeException {
    EconomyResult economyResult;

    public EconomyException(final String message, final EconomyResult economyResult) {
        super(message);

        this.economyResult = economyResult;
    }
}
