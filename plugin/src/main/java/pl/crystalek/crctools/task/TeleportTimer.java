package pl.crystalek.crctools.task;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crctools.tpa.TpaRequest;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class TeleportTimer implements Runnable {
    final TpaRequest tpaRequest;
    int counter;

    @Override
    public void run() {

    }
}
