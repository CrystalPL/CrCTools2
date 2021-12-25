package pl.crystalek.crctools.command.model;

import pl.crystalek.crcapi.command.BaseSubCommand;

public interface ICommand extends BaseSubCommand {

    boolean isUseConsole();

    String getCommandUsagePath();
}
