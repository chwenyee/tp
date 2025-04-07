package command;

import manager.ManagementSystem;
import miscellaneous.Ui;

/**
 * Represents a command to exit the application.
 * This command terminates the program after displaying a goodbye message.
 */
public class ExitCommand extends Command {

    /**
     * Executes the exit command by displaying the goodbye message.
     *
     * @param manager The management system (unused in this command).
     * @param ui The user interface used to display the goodbye message.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) {
        ui.showBye();
    }

    /**
     * Indicates that this command should terminate the application.
     *
     * @return Returns true to exit the application.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
