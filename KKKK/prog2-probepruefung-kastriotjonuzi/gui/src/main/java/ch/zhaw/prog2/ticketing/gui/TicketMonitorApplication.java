package ch.zhaw.prog2.ticketing.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Objects;

import static ch.zhaw.prog2.ticketing.gui.AuditoriumSimulation.ShowTime.*;

/**
 * TicketMonitor Application
 * The application shows all booked tickets for a selected show in a Table View.
 * The show can be selected in a DropDown, containing all booked shows for the given auditorium.
 * Tickets in the TableView are updated continuously, when they change in the auditorium.
 *
 * Additionally, an auditorium simulation is provided, which books and cancels ticket in the background.
 */

public class TicketMonitorApplication extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    private AuditoriumSimulation simulation = null;

    /**
     * JavaFX Application startup.
     * - Initializes the Auditorium.
     * - Loads the TicketMonitorWindow and initializes the controller.
     * - Starts the Auditorium simulation
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(TicketMonitorApplication.class.getResource("TicketMonitor.fxml"));
        Parent rootNode = loader.load();

        TicketMonitorController controller = loader.getController();
        simulation = AuditoriumSimulation
                .buildAuditorium("Kino am See")
                .withSize(5, 10)
                .forSeason(LocalDate.now(), 2)
                .withShowTimes(NOON, PRIMETIME, LATENIGHT)
                .generateTicketsPerRow(3)
                .build();
        controller.setAuditorium(simulation.getAuditorium());
        simulation.startSimulation(5);

        Scene scene = new Scene(rootNode);
        primaryStage.setTitle("Ticket Monitor");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(400);
        primaryStage.setMaxWidth(600);
        primaryStage.setMinHeight(300);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.show();
    }

    /**
     * Called, when the application is stopping.
     * Stops a running auditorium simulation.
     * @throws Exception if something goes wrong
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        if (Objects.nonNull(simulation)) {
            simulation.stopSimulation();
        }
    }
}
