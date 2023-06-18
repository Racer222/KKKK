package ch.zhaw.prog2.ticketing.functional.handout;

import ch.zhaw.prog2.ticketing.model.PriceCalculatorFunction;

public class PriceCalculators {
    private PriceCalculators() {
        throw new IllegalStateException("static methods only");
    }

    /**
     * Wenn eine Show ab 17:00 beginnt, gilt der reguläre Preis, vor 17:00 gibt es eine Ermässigung.
     * <p>
     * Hinweis: Die Methode show.getDateTime().getHour() liefert die Stunde des Beginns einer Show
     *
     * @param regularPrice der reguläre Preis
     * @param discount die Ermässigung in Prozent (als Wert zwischen 0.0 und 1.0)
     * @return eine Lambda-Expression
     */
    public static PriceCalculatorFunction getDiscountBefore5PM (int regularPrice, double discount) {
        return null; // TODO implementieren
    }

}
