package command;

import manager.ManagementSystem;
import miscellaneous.Ui;

/**
 * Represents a command to display all available commands and their usage formats.
 */
public class HelpCommand extends Command {
    /**
     * Executes the help command by displaying the help message.
     *
     * @param manager The management system (unused in this command).
     * @param ui      The user interface used to display all available commands of the application.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) {
        ui.showHelp();
    }
}
