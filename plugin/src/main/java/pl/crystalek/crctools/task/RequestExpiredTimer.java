package pl.crystalek.crctools.task;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class RequestExpiredTimer implements Runnable {

    @Override
    public void run() {

    }
}
