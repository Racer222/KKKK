package ch.zhaw.prog2.ticketing.io.handout;

import ch.zhaw.prog2.ticketing.model.Show;
import ch.zhaw.prog2.ticketing.model.Ticket;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Helper-Class with static methods to produce CSV-Exports.
 * <p>
 * Every CSV-File has a first line with column-headers,
 * uses the LINE_SEPARATOR {@link Io#LINE_SEPARATOR} as line ending
 * and DELIMITER {@value Io#DELIMITER} to separate columns.
 */
public class Io {
    // Todo: a) Ergänzen Sie den Exportpfad so, dass die exportierten Dateien im Unterverzeichnis "Export" im User-Home erstellt werden
    public static final String EXPORT_PATH = "";
    public static final String DELIMITER = ";";
    public static final String FIRST_ROW_TICKET_EXPORT = "ID;Row;Column;Owner;Paid";
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String FILE_TYPE = ".csv";
    public static final String DATE_PATTERN = "yyyyMMdd-HHmm";

    private Io() {
        // no instances allowed
    }

    /**
     * Composes a line containing the data of a ticket in csv format.
     * <p>
     * Fields are delimited with DELIMITER {@value Io#DELIMITER}.
     * <p>
     * The String does not end with a line end character.
     * <p>
     * Binary values are written as "Ja" for true and "Nein" for false
     *
     * @param ticket The ticket, whose information is used to build the line
     * @return String that represents the ticket in csv format
     */
    private static String composeTicketCsvLine(Ticket ticket) {
        // Todo: b) Erstellen Sie einen CSV-String der Felder des übergebenen Tickets.
        // Verwenden Sie den DELIMITER als Trennzeichen; kein Zeilenendezeichen.
        // Reihenfolge der Felder entsprechend der vorgegebenen Titelzeile (FIRST_ROW_TICKET_EXPORT)
        return "aafea30b-4499-4544-8f8f-d032f170ab10;D;1;Vreni Meier;Ja";
    }

     /**
     * Export the booked tickets to a specific {@link Show} to a csv formatted file using UTF-8 encoding.
     * <p>
     * The format of the csv rows is given by the title row {@value Io#FIRST_ROW_TICKET_EXPORT}.
     * This title row is always the first line of the file.
     *
     * @param show The show that contains the tickets
     * @return true, if the file is created
     * @throws IOException is thrown if an error occurred during the use of a file
     */
    public static boolean exportTicketsAsCsv(Show show) throws IOException {
        Objects.requireNonNull(show);
        checkAndCreatePath();
        String targetPathName = composePathTicketExport(show);
        File csvOutputFile = new File(targetPathName);
        List<Ticket> registeredTickets = sortedTicketList(show.getAuditorium().getRegisteredTickets());
        // Todo: c) Schreiben Sie den Export in die CSV-Datei gemäss Methodenbeschreibung
        // Beachten Sie, dass Sie die Datei am Ende wieder schliessen.
        // Vorgabe: Verwenden Sie eine Klasse mit Buffer und kodieren Sie den Text als UTF-8!

        // Ende c)
        return csvOutputFile.exists();
    }

     /**
     * Composes the CSV filepath (including filename).
     * <p>
     * The filename consists of the name of the show, followed by the DateTime
     * of the show, formatted with DATE_PATTERN {@value #DATE_PATTERN}
     * <p>
     * The Filetype is given by FILE_TYPE {@value #FILE_TYPE}.
     *
     * @param show The show that contains the tickets
     * @return String that represents the path with filename
     */
    private static String composePathTicketExport(Show show) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        sb.append(EXPORT_PATH).append(File.separator).append(show.getName());
        sb.append(show.getDateTime().format(formatter)).append(FILE_TYPE);
        return sb.toString();
    }

     /**
     * Sorts a collection of Tickets by seat numbers.
     *
     * @param tickets Collection of tickets to sort.
     * @return List of tickets sorted by seat number
     */
    private static List<Ticket> sortedTicketList(Collection<Ticket> tickets) {
        return tickets.stream().sorted(Comparator.comparing(Ticket::getSeat)).toList();
    }

     /**
     * Check the path in {@link #EXPORT_PATH}.
     * <p>
     * If the path does not exist, create them.
     */
    private static void checkAndCreatePath() {
        // Todo: d) Sorgen Sie dafür, dass der Pfad in EXPORT_PATH tatsächlich existiert


    }
}