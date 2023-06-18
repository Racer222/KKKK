package ch.zhaw.prog2.ticketing.gui.controls;

import ch.zhaw.prog2.ticketing.model.Guest;
import ch.zhaw.prog2.ticketing.model.Seat;
import ch.zhaw.prog2.ticketing.model.Ticket;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Collection;

/**
 * Extended {@link TableView} to display {@link Ticket} Objects.
 * It creates {@link TableColumn} entries for Ticket properties 'seat', 'owner', 'price' and 'isPayed'.
 */
public class TicketTableView extends TableView<Ticket> {

    public TicketTableView () {
        initTicketTableView();
    }

    /**
     * Sets the list of Tickets to be displayed in the TableView and refreshes the View.
     * @param tickets List of tickets to be displayed.
     */
    public void setTickets(Collection<? extends Ticket> tickets) {
        this.getItems().setAll(tickets);
        this.refresh();
    }

    private void initTicketTableView() {
        // Create Seat Column
        TableColumn<Ticket, Seat> seatTableColumn = createTicketTableColumn("Seat", "seat", 50);
        // Create Guest Column
        TableColumn<Ticket, Guest> guestTableColumn = createTicketTableColumn("Owner", "owner", 200);
        // Create Price Column
        TableColumn<Ticket,Integer> priceTableColumn = createTicketTableColumn("Price", "price", 70);
        priceTableColumn.setStyle("-fx-alignment: CENTER_RIGHT");
        priceTableColumn.setCellFactory(tableColumn -> new PriceTableCell<Ticket>());
        // Create Payed Column
        TableColumn<Ticket,Boolean> validTableColumn = createTicketTableColumn("Payed", "isPayed", 65);
        validTableColumn.setCellFactory(tableColumn -> new CheckBoxTableCell<>());
        validTableColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().getIsPaid()));
        // Create TableView
        this.setPlaceholder(new Label("No Tickets booked"));
        this.getColumns().addAll(seatTableColumn, guestTableColumn, priceTableColumn, validTableColumn);
    }

    private <T,C> TableColumn<T,C> createTicketTableColumn(String title, String propertyName, int preferredWith) {
        TableColumn<T,C> tableColumn = new TableColumn<>(title);
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        tableColumn.setPrefWidth(preferredWith);
        return tableColumn;
    }
}
