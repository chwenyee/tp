package command;

import java.util.List;
import manager.Appointment;
import manager.ManagementSystem;
import miscellaneous.Ui;

/**
 * Command to find appointments for a patient by their NRIC.
 * Interacts with the {@link ManagementSystem} to retrieve
 * all appointments associated with a specific NRIC and displays them through the {@link Ui}.
 *
 * <p> This command follows the Command pattern, which encapsulates the appointment search
 * functionality to allow for consistent execution without directly manipulating the system's components.
 * </p>
 *
 */
public class FindAppointmentCommand extends Command {
    protected String nric;

    /**
     * Constructs FindAppointmentCommand with the specified NRIC.
     *
     * @param nric the NRIC of the patient whose appointments are to be found
     */
    public FindAppointmentCommand(String nric) {
        this.nric = nric;
    }

    /**
     * Executes the command to find appointments for a patient with the given NRIC.
     * It retrieves the list of appointments from ManagementSystem
     * and displays the results using Ui.
     *
     * @param manager ManagementSystem responsible for handling appointment data
     * @param ui Ui responsible for displaying the list of appointments
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) {
        List<Appointment> foundAppointments = manager.findAppointmentsByNric(nric);
        ui.showAppointmentsFound(foundAppointments, nric);
    }
}
