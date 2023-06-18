package ch.zhaw.prog2.ticketing.gui.controls;

import ch.zhaw.prog2.ticketing.model.Show;
import javafx.scene.control.ComboBox;

/**
 * Extended ComboBox using {@link ShowListCell} to correctly display ShowObjects.
 */
public class ShowComboBox extends ComboBox<Show> {
    public ShowComboBox () {
        super();
        this.setCellFactory(listView -> new ShowListCell());
        this.setButtonCell(new ShowListCell());
    }
}
