package ch.zhaw.prog2.ticketing.gui.controls;

import ch.zhaw.prog2.ticketing.model.Show;
import javafx.scene.control.ListCell;

/**
 * Extended {@link ListCell} to correctly format and display {@link Show} entries.
 * Used in {@link ShowComboBox} to display the shows to select.
 */
public class ShowListCell extends ListCell<Show> {

    @Override
    protected void updateItem(Show show, boolean empty) {
        super.updateItem(show, empty);
        if (show == null || empty) {
            setText("Select Show...");
        } else {
            // format the show like: "03. Jan.  20:15   Name"
            setText("%1$-3td. %1$-6tb %1$7tR \t %2$s".formatted(show.getDateTime(), show.getName()));
        }
    }
}
