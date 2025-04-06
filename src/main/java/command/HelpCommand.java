package command;

import manager.ManagementSystem;
import miscellaneous.Ui;

public class HelpCommand extends Command {
    @Override
    public void execute(ManagementSystem manager, Ui ui) {
        ui.showHelp();
    }
}
