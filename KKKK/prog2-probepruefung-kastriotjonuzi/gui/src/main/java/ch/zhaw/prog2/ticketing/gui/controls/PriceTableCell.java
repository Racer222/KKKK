package ch.zhaw.prog2.ticketing.gui.controls;

import javafx.scene.control.TableCell;

/**
 * TableCell to correctly format and display price represented as in integer in cent/Rappen.
 * @param <S> class of the objects displayed in the TableView/TableCell
 */
public class PriceTableCell<S> extends TableCell<S, Integer> {
    @Override
    protected void updateItem(Integer price, boolean empty) {
        super.updateItem(price, empty);
        if (price == null || empty) {
//            setText("0.00");
            setGraphic(null);
        } else {
            // format the price to floating number with two decimals (e.g. "18.58")
            setText("%.2f".formatted(price.doubleValue()/100));
        }
    }
}
