package miscellaneous;

import command.AddAppointmentCommand;
import command.Command;
import command.DeleteAppointmentCommand;
import command.ExitCommand;
import command.HelpCommand;
import command.ListAppointmentCommand;
import command.SortAppointmentCommand;
import exception.InvalidInputFormatException;
import exception.UnknownCommandException;
import manager.Appointment;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static manager.Appointment.INPUT_FORMAT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

    private Appointment extractAppointment(String input) throws InvalidInputFormatException,
            UnknownCommandException {
        Command command = Parser.parse(input);
        assertInstanceOf(AddAppointmentCommand.class, command);
        return ((AddAppointmentCommand) command).getAppointment();
    }

    private String extractAppointmentId(String input) throws InvalidInputFormatException,
            UnknownCommandException {
        Command command = Parser.parse(input);
        assertInstanceOf(DeleteAppointmentCommand.class, command);
        return ((DeleteAppointmentCommand) command).getApptId();
    }

    @Test
    void parse_validInputToAddAppointment_returnCorrectAddAppointmentCommand() throws Exception {
        String input = "add-appointment ic/S1234567D dt/2025-09-20 t/1430 dsc/Checkup";

        Appointment appointment = extractAppointment(input);

        assertNotNull(appointment);
        assertEquals("S1234567D", appointment.getNric());
        assertEquals(LocalDateTime.parse("2025-09-20 1430", INPUT_FORMAT), appointment.getDateTime());
        assertEquals("Checkup", appointment.getDescription());
    }

    @Test
    void parse_dateTimeInThePastOfAddAppointment_expectException() {
        String input = "add-appointment ic/S1234567D dt/2025-03-20 t/1300 dsc/Checkup";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(input));
    }

    @Test
    void parse_invalidIcFormatToAddAppointment_expectException() {
        String input1 = "add-appointment ic/S1234567 dt/2025-03-20 t/1300 dsc/Checkup";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(input1));

        String input2 = "add-appointment ic/S123467D dt/2025-03-20 t/1300 dsc/Checkup";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(input2));

        String input3 = "add-appointment ic/123467D dt/2025-03-20 t/1300 dsc/Checkup";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(input3));

        String input4 = "add-appointment ic/123467 dt/2025-03-20 t/1300 dsc/Checkup";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(input4));
    }

    @Test
    void parse_invalidDateTimeFormat_expectException() {
        String input1 = "add-appointment ic/S1234567D dt/03-19 t/1900 dsc/Checkup";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(input1));

        String input2 = "add-appointment ic/S1234567D dt/2025-03-20 t/7:00PM dsc/Checkup";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(input2));
    }

    @Test
    void parse_validInputToDeleteAppointment_expectSuccess() throws InvalidInputFormatException,
            UnknownCommandException {
        String input = "delete-appointment A100";
        String result = extractAppointmentId(input);

        assertEquals("A100", result, "Appointment ID does not match");
    }

    @Test
    void parse_extraSpacesToDeleteAppointment_expectSuccess() throws InvalidInputFormatException,
            UnknownCommandException {
        String input = "delete-appointment    A100";
        String result = extractAppointmentId(input);

        assertEquals("A100", result, "Appointment ID does not match");
    }

    @Test
    void parse_invalidAppointmentIdToDeleteAppointment_expectException() {
        String input = "delete-appointment 100";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(input));
    }

    @Test
    void parse_lowercaseInputToDeleteAppointment_expectSuccess() throws InvalidInputFormatException,
            UnknownCommandException {
        String input = "delete-appointment a100";
        String result = extractAppointmentId(input);

        assertEquals("a100", result, "Appointment ID does not match");
    }

    @Test
    void parseAddPatient_invalidInputFormat_expectException() {
        String input = "add-patient n/John Doe ic/ dob/1999-12-12 g/M "
                + "p/98765432 a/123 Main Street h/Diabetes, Hypertension";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(input));
    }

    @Test
    void parseViewHistory_validNric() throws InvalidInputFormatException {
        String[] result = Parser.parseViewHistory("view-history S1234567D");
        assertEquals("ic", result[0]);
        assertEquals("S1234567D", result[1]);
    }

    @Test
    void parseViewHistory_validName() throws InvalidInputFormatException {
        String[] result = Parser.parseViewHistory("view-history John Doe");
        assertEquals("n", result[0]);
        assertEquals("John Doe", result[1]);
    }

    @Test
    void parseViewHistory_explicitNricPrefix() throws InvalidInputFormatException {
        String[] result = Parser.parseViewHistory("view-history ic/S1234567A");
        assertEquals("ic", result[0]);
        assertEquals("S1234567A", result[1]);
    }

    @Test
    void parseViewHistory_invalidInput_expectException() {
        assertThrows(InvalidInputFormatException.class,
                () -> Parser.parseViewHistory("view-history"));
        assertThrows(InvalidInputFormatException.class,
                () -> Parser.parseViewHistory("view-history  "));
    }

    @Test
    void parseStoreHistory_validInput_expectSuccess() throws InvalidInputFormatException {
        String[] result = Parser.parseStoreHistory("store-history ic/S1234567D h/Allergic to nuts");
        assertEquals("S1234567D", result[0]);
        assertEquals("Allergic to nuts", result[1]);
    }

    @Test
    void parseStoreHistory_missingFields_expectException() {
        assertThrows(InvalidInputFormatException.class,
                () -> Parser.parseStoreHistory("store-history ic/S1234567D"));
        assertThrows(InvalidInputFormatException.class,
                () -> Parser.parseStoreHistory("store-history h/Allergic to nuts"));
    }

    @Test
    void parse_addAppointmentCommand_expectAddAppointmentCommand() throws InvalidInputFormatException,
            UnknownCommandException {
        Command command = Parser.parse("add-appointment ic/S1234567D dt/2025-09-20 t/1900 dsc/Checkup");
        assertInstanceOf(AddAppointmentCommand.class, command);
    }

    @Test
    void parse_wrongFormatAddAppointment_expectInvalidInputFormatException() {
        String unfilledParams = "add-appointment ic/ dt/ t/ dsc/";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(unfilledParams));

        String missingIc = "add-appointment ic/ dt/2025-03-19 t/1200 dsc/medical check-up";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(missingIc));

        String missingDate = "add-appointment ic/S1234567D dt/ t/1200 dsc/medical check-up";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(missingDate));

        String missingTime = "add-appointment ic/S1234567D dt/2025-03-19 t/ dsc/medical check-up";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(missingTime));

        String missingDescription = "add-appointment ic/S1234567D dt/2025-03-19 t/1200 dsc/";
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(missingDescription));
    }

    @Test
    void parse_deleteAppointmentCommand_expectDeleteAppointmentCommand() throws InvalidInputFormatException,
            UnknownCommandException {
        Command command = Parser.parse("delete-appointment A100");
        assertInstanceOf(DeleteAppointmentCommand.class, command);
    }

    @Test
    void parse_listAppointmentCommand_expectListAppointmentCommand() throws Exception {
        Command command = Parser.parse("list-appointment");
        assertInstanceOf(ListAppointmentCommand.class, command);
    }

    @Test
    void parse_unknownCommand_expectUnknownCommandException() {
        String userInput = "bee-boo";
        assertThrows(UnknownCommandException.class, () -> Parser.parse(userInput));
    }

    @Test
    void parse_sortAppointmentCommand_expectSortAppointmentCommand() throws InvalidInputFormatException,
            UnknownCommandException {
        Command byDate = Parser.parse("sort-appointment byDate");
        assertInstanceOf(SortAppointmentCommand.class, byDate);

        Command byId = Parser.parse("sort-appointment byId");
        assertInstanceOf(SortAppointmentCommand.class, byId);
    }

    @Test
    void parse_invalidSortAppointmentCommand_expectException() {
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse("sort-appointment"));
    }

    @Test
    void parse_byeCommand_expectByeCommand() throws InvalidInputFormatException,
            UnknownCommandException {
        Command command = Parser.parse("bye");
        assertInstanceOf(ExitCommand.class, command);

    }

    @Test
    void parse_helpCommand_expectHelpCommand() throws InvalidInputFormatException,
            UnknownCommandException {
        Command command = Parser.parse("help");
        assertInstanceOf(HelpCommand.class, command);
    }

    @Test
    void parse_nullInput_expectException() {
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(null));
    }

    @Test
    void parse_emptyInput_expectException() {
        assertThrows(InvalidInputFormatException.class, () -> Parser.parse(""));
    }

}
