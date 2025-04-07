package command;

import manager.ManagementSystem;
import manager.Patient;
import miscellaneous.Ui;

/**
 * Command to view a patient in the system by their NRIC.
 * This command interacts with the {@link ManagementSystem} to retrieve
 * a patient and displays their details through the {@link Ui}.
 *
 * <p> This command is part of the Command pattern used to encapsulate
 * the viewing functionality of a patient, allowing easy execution
 * without directly manipulating system objects.
 * </p>
 *
 * @author dylancmznus
 */
public class ViewPatientCommand extends Command {
    protected String nric;

    /**
     * Constructs ViewPatientCommand with the specified NRIC.
     *
     * @param nric the NRIC of the patient to be viewed
     */
    public ViewPatientCommand(String nric) {
        this.nric = nric;
    }

    /**
     * Executes the command to view a patient with the given NRIC.
     * Retrieves the patient from ManagementSystem and
     * displays the details using Ui.
     *
     * @param manager the {@link ManagementSystem} responsible for handling patient data
     * @param ui the {@link Ui} responsible for displaying the patient information
     * @throws AssertionError if the manager or ui instances are null
     * @throws AssertionError if no patient is found for the given NRIC
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) {
        assert manager != null : "ManagementSystem instance can't be empty";
        assert ui != null : "Ui instance can't be empty";

        Patient matchedPatient = manager.viewPatient(nric);
        assert matchedPatient != null : "No patient found for NRIC: " + nric;

        ui.showPatientViewed(matchedPatient, nric);
    }
}
