package ch.zhaw.prog2.ticketing.gui;

import ch.zhaw.prog2.ticketing.gui.controls.ShowComboBox;
import ch.zhaw.prog2.ticketing.gui.controls.TicketTableView;
import ch.zhaw.prog2.ticketing.model.Auditorium;
import ch.zhaw.prog2.ticketing.model.Show;
import ch.zhaw.prog2.ticketing.model.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class TicketMonitorController {

    @FXML private Label auditoriumNameLabel;
    /**
     * ComboBox-Control mit welchem ein Show-Objekt (value) aus einer Liste von
     * Show-Objekten ausgewählt werden kann.
     * Erweitert ComboBox für Show-Objekte, damit Show-Objekte sinnvoll angezeigt werden.
     * Mit `getValue()`, kann die selektierte Show bezogen werden.
     */
    @FXML private ShowComboBox showComboBox;

    /**
     * Erweiterte TableView, um eine Liste von Ticket-Objekten anzuzeigen.
     * Die Liste kann mit {@link TicketTableView#setTickets(Collection)}} gesetzt
     * und die TableView aktualisiert werden.
     * (siehe Methode updateTicketTableView)
     */
    @FXML private TicketTableView ticketTableView;

    /**
     * CheckBox um das Live-Update ein bzw. auszuschalten.
     */
    @FXML private CheckBox liveUpdateCheckBox;

    /**
     * Auditorium für welches die Shows und Tickets dargestellt werden.
     */
    private Auditorium auditorium;

    /**
     * Durch den Benutzer in der ComboBox selektierte Show,
     * für welche die Tickets in der TableView angezeigt werden
     */
    private Show selectedShow;

    /**
     * Called after the FXML-View and Controller is loaded and all View components are configured.
     * Allows to set additional configurations. (if required)
     * e.g. register ActionEventHandlers, bind Properties, etc.
     */
    @FXML
    private void initialize()  {

    }

    // Todo: Aufgabe a) Implementieren und registrieren Sie einen ActionEvent-Handler
    //       welcher bei der Auswahl einer Show in der ComboBox die TicketTableView aktualisiert


    /**
     * Konfiguriert Controller & View für den übergebenen Veranstaltungsort (Auditorium).
     * Diese Methode wird von der Anwendung aufgerufen, nachdem View und Controller
     * initialisiert sind.
     * - Zeigt den Namen des Veranstaltungsortes (Auditoriums)
     * - Lädt die Liste der Shows und füllt die ComboBox
     * - Selektiert die erste Show aus der Liste und zeigt aktualisiert die Ticket-Tabelle
     *
     * @param auditorium Veranstaltungsort für welchen die Shows und Tickets angezeigt werden sollen.
     */
    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
        this.auditoriumNameLabel.setText(auditorium.getName());
        // load list of shows from auditorium and populate the combo box drop down
        List<Show> shows = auditorium.getShows().stream().sorted(Comparator.comparing(Show::getDateTime)).toList();
        showComboBox.getItems().setAll(shows);
        // select first show in the combo box and populate the TicketTableView.
        if (!showComboBox.getItems().isEmpty()) {
            this.selectedShow = showComboBox.getItems().get(0);
            showComboBox.setValue(this.selectedShow);
            updateTicketTableView();
        }
    }

    /**
     * Hilfsmethode zum Aktualisieren der Ticket-Tabelle für die ausgewählte Show.
     */
    private void updateTicketTableView() {
        if (this.selectedShow != null) {
            List<Ticket> registeredTickets = auditorium.getRegisteredTicketsFor(selectedShow)
                    .sorted(Comparator.comparing(Ticket::getSeat)).toList();
            ticketTableView.setTickets(registeredTickets);
        }
    }

    // ToDo: Aufgabe b1) Implementieren des Observer-Interfaces/Objektes,
    //       welches die update-Requests von auditorium (Observable) verarbeitet.

    /**
     * ToDo: Aufgabe b2) Implementieren Sie die Methode toggleLiveUpdate
     * Ein-/Ausschalten des Live-Updates der Ticket-Tabelle.
     * Je nach Select-Status der CheckBox soll das Observer-Objekt registriert/unregistriert werden
     * welcher die update-Events vom Auditorium verarbeitet (siehe Aufgabe b1)
     * Die Methode ist im FXML-Dokument bereits als ActionHandler für die CheckBox registriert.
     *
     * @param event sent by the CheckBox
     */
    @FXML
    private void toggleLiveUpdate(ActionEvent event) {



    }
}
