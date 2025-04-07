package command;

import exception.PatientNotFoundException;
import manager.ManagementSystem;
import miscellaneous.Ui;


/**
 * Represents a command to view medical history for a patient,
 * either by NRIC or by name.
 */
public class ViewMedHistoryCommand extends Command {
    protected String type;
    protected String nameOrIc;

    /**
     * Constructs a ViewMedHistoryCommand with the necessary details.
     * details[0] = "ic" or "n" (search type), details[1] = the actual NRIC or name.
     *
     * @param details String array indicating search type and the patient's NRIC or name.
     */
    public ViewMedHistoryCommand(String[] details) {
        this.type = details[0];
        this.nameOrIc = details[1];
    }

    /**
     * Executes the command to view a patient's medical history.
     * If type == "ic", it searches by NRIC; otherwise, it searches by name.
     *
     * @param manager The ManagementSystem that manages patient data.
     * @param ui      The Ui used to display output to the user.
     * @throws PatientNotFoundException if no matching patient is found when searching by NRIC.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws PatientNotFoundException {
        if (type.equals("ic")) {
            manager.viewMedicalHistoryByNric(nameOrIc);
        } else {
            manager.viewMedicalHistoryByName(nameOrIc);
        }
    }
}
