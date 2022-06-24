package pl.crystalek.crctools.api.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crctools.api.economy.EconomyResult;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class EconomyException extends RuntimeException {
    EconomyResult economyResult;

    public EconomyException(final String message, final EconomyResult economyResult) {
        super(message);

        this.economyResult = economyResult;
    }

    /**
     * @return Message with an error
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * @return Type of error
     * @see EconomyResult
     */
    public EconomyResult getEconomyResult() {
        return economyResult;
    }
}
