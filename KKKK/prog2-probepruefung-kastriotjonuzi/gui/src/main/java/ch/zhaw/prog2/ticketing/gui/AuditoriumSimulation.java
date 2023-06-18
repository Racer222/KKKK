package ch.zhaw.prog2.ticketing.gui;

import ch.zhaw.prog2.ticketing.model.*;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class to set up an Auditorium with seats and shows and simulate ticket operations.
 * <p>
 * Because setting up an {@link Auditorium} requires many parameters, an {@link AuditoriumSimulationBuilder} is provided.
 * This enables fluent definition of AuditoriumSimulation instances:
 * <pre>
 * AuditoriumSimulation simulation =
 *     AuditoriumSimulation
 *         .buildAuditorium("Kino am See")
 *         .withSize(5, 10)
 *         .forSeason(LocalDate.now(), 2)
 *         .withShowTimes(NOON, PRIMETIME, LATENIGHT)
 *         .generateTicketsPerRow(3)
 *         .build();
 * </pre>
 *
 */
public class AuditoriumSimulation {
    private static final Random random = new Random(System.currentTimeMillis());
    // Default ZoneId used for the Show playtimes
    private static final ZoneId zurichZoneId = ZoneId.of("Europe/Zurich");
    // PriceCalculatorFunction for standard tickets
    private static final PriceCalculatorFunction normalPriceCalculator =
            (show, seat) -> "ABC".indexOf(seat.getRow()) >= 0 ? 1850 : 925;
    // PriceCalculatorFunction for premium premium tickets (e.g. PRIMETIME shows)
    private static final PriceCalculatorFunction premiumPriceCalculator =
            (show, seat) -> normalPriceCalculator.calculatePriceInRappen(show, seat) * 2;

    // Auditorium the simulation is running on
    private final Auditorium auditorium;

    // number of rows in the Auditorium
    private final int rows;
    // number of seats per row (rectangular layout assumed)
    private final int seatsPerRow;

    // haw many tickets should be initially booked per row
    private final int ticketsPerRow;

    // set of show titles to choose from
    private final String[] showTitles = {
            "The Avengers",
            "Age of Ultron",
            "Captain America - CivilWar",
            "Infinity War",
            "Captain Marvel",
            "Endgame",
            "Doctor Strange in the Multiverse of Madness"
    };

    // set of guests booking tickets
    private final Guest[] guests = {
            new Guest("Nick Fury"),
            new Guest("Tony Stark"),
            new Guest("Steve Rogers"),
            new Guest("Bruce Banner"),
            new Guest("Natasha Romanoff"),
            new Guest("Thor Odinson")
    };

    /**
     * Use {@link AuditoriumSimulationBuilder} to create Instances.
     * @param auditoriumName    Name of the Auditorium
     * @param auditoriumRows    Number of rows in the Auditorium
     * @param auditoriumSeatsPerRow Number of seats per row (rectangular layout assumed)
     * @param seasonStartDate   First date of season
     * @param seasonDurationDays    Number of days to create shows for
     * @param showTimes         Set of ShowTimes to generate every day of season
     * @param ticketsPerRow     Number of tickets created per row
     */
    private AuditoriumSimulation(
            String auditoriumName, int auditoriumRows, int auditoriumSeatsPerRow,
            LocalDate seasonStartDate, int seasonDurationDays, ShowTime[] showTimes,
            int ticketsPerRow)
    {
        this.rows = auditoriumRows;
        this.seatsPerRow = auditoriumSeatsPerRow;
        this.ticketsPerRow = ticketsPerRow;
        this.auditorium = new Auditorium(auditoriumName, SeatGenerator.rectangle(auditoriumRows, auditoriumSeatsPerRow));
        this.generateShows(seasonStartDate, seasonDurationDays, showTimes);
        if (ticketsPerRow > 0) {
            auditorium.getShows().forEach(this::generateTicketsForShow);
        }
    }

    /**
     * Auditorium the simulation is running for
     * @return Instance of Auditorium
     */
    public Auditorium getAuditorium() {
        return this.auditorium;
    }

    /**
     * Standardized ShowTimes the shows starting.
     */
    public enum ShowTime {
        MORNING(LocalTime.of(9, 0)),
        NOON(LocalTime.of(12, 0)),
        AFTERNOON(LocalTime.of(16, 30)),
        PRIMETIME(LocalTime.of(19, 30)),
        LATENIGHT(LocalTime.of(22,15));

        private final LocalTime time;

        ShowTime(LocalTime time) {
            this.time = time;
        }
        public LocalTime getTime() {
            return this.time;
        }
    }

    /**
     * Helper Method to create a series of random shows for a season.
     * For each showTime for every day of the season starting at the given start date and duration.
     * a random show from the {@link #showTitles} set is created and booked for the {@link #auditorium}.
     * @param seasonStartDate       Start date of the seasson
     * @param seasonDurationDays    Duration of the season in days
     * @param showTimes             ShowTimes the shows are running every day in season
     * @return  Returns a list of shows already booked in the auditorium.
     */
    private List<Show> generateShows(LocalDate seasonStartDate, int seasonDurationDays, ShowTime[] showTimes) {
        Objects.requireNonNull(auditorium);
        Objects.requireNonNull(seasonStartDate);
        Objects.requireNonNull(showTimes);
        if (seasonDurationDays <= 0) throw new IllegalArgumentException("seasonDurationDays must be >0");

        return seasonStartDate
                // for given number of seasonDurationDays from seasonStartDate
                .datesUntil(seasonStartDate.plusDays(seasonDurationDays))
                .<Show>mapMulti((seasonDate, consumer) ->
                        // create show for given showTimes per day
                        Stream.of(showTimes)
                                .map(showTime -> generateShow(seasonDate, showTime))
                                .filter(Optional::isPresent)  // filter duplicates
                                .map(Optional::get)
                                .forEach(consumer))
                .toList();
    }

    /**
     * Creates a random show from the {@link #showTitles} set for the given date and time.
     * The show will be booked for the {@link #auditorium}.
     * @param showDate  date of the show to create
     * @param showTime  time of the show to create
     * @return  Optional with generated show for given date and time, empty if given date is already occupied.
     */
    private Optional<Show> generateShow(LocalDate showDate, ShowTime showTime) {
        Objects.requireNonNull(auditorium);
        Objects.requireNonNull(showDate);
        Objects.requireNonNull(showTime);
        try {
            return Optional.of(new Show(
                    auditorium,
                    showTitles[random.nextInt(showTitles.length-1)],
                    ZonedDateTime.of(showDate, showTime.getTime(), zurichZoneId),
                    (showTime == ShowTime.PRIMETIME)? premiumPriceCalculator : normalPriceCalculator
            ));
        } catch (AuditoriumAlreadyOccupiedException e) {
            System.err.printf("Auditorium %s already occupied by %s!%n%s%n",
                    auditorium.getName(), e.getBookedShow().getName(), e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Generates a set of random ticket for the given show.
     * For each row {@link #ticketsPerRow} tickets will be booked.
     * @param show show to book tickets for
     */
    private void generateTicketsForShow(Show show) {
        TicketFactory ticketFactory = new TicketFactory(show);
        IntStream.range(1, rows).map(row -> 'A' + row - 1)
                .forEach(rowChar -> random.ints(ticketsPerRow, 1, seatsPerRow)
                            .forEach(seatInRow -> createTicket(ticketFactory,
                                    new Seat((char) rowChar, seatInRow),
                                    guests[random.nextInt(guests.length-1)],
                                    random.nextBoolean())
                            )
                );
    }


    /**
     * Creates and books a ticket using the given ticket factory, for the given seat, and guest.
     * Half of the tickets will be payed.
     * @param factory ticket factory of a show
     * @param seat  seat to book for the show
     * @param owner guest booking the ticket
     * @param payed indicate if the ticket already payed
     * @return Optional ticket if the booking was successful, empty when seat was already occupied
     */
    private Optional<Ticket> createTicket(TicketFactory factory, Seat seat, Guest owner, boolean payed) {
        Optional<Ticket> ticketCandidate = factory.createTicket(seat, owner);
        ticketCandidate.ifPresentOrElse(ticket -> {
            if (payed) ticket.pay();
            System.out.print("Booked: ");
        }, () -> System.out.print("Invalid: "));
        Show show = factory.getShow();
        System.out.printf("%tc - %s, %s%n", show.getDateTime(), show.getName(), seat.toString());
        return ticketCandidate;
    }

    /**
     * Helper function to generate a random seat for the given auditorium.
     * @return randam seat in the range of {@link #rows} and {@link #seatsPerRow} of the auditorium
     */
    private Seat randomSeat() {
        char row = (char)('A' + random.nextInt(0, rows-1));
        int seatInRow = random.nextInt(1, seatsPerRow);
        return new Seat(row, seatInRow);
    }

    /**
     * Helper method to choose a random guest from the set of {@link #guests}
     * @return random instance of guest
     */
    private Guest randomGuest() {
        return guests[random.nextInt(guests.length)];
    }

    /**
     * Helper method to book a random ticket for the given show
     * @param show to book a ticket for
     * @return Optiona ticket booked if successful, empty if not successful
     */
    private Optional<Ticket> bookRandomTicket(Show show) {
        return createTicket(new TicketFactory(show), randomSeat(), randomGuest(), random.nextBoolean());
    }

    /**
     * Helper method to cancel a random ticket for the given show.
     *
     * @param show to cancel a ticket for
     * @return Optional ticket canceled if successful, empty if not successful
     */
    private Optional<Ticket> cancelRandomTicket(Show show) {
        List<Ticket> registeredTickets = show.getAuditorium().getRegisteredTicketsFor(show).toList();
        if (registeredTickets.isEmpty()) return Optional.empty();
        Ticket randomRegisteredTicket = registeredTickets.get(random.nextInt(registeredTickets.size()-1));
        show.getAuditorium().cancelTicket(randomRegisteredTicket);
        System.out.printf("Canceled: %tc - %s, %s%n", show.getDateTime(), show.getName(), randomRegisteredTicket.getSeat().toString());
        return Optional.of(randomRegisteredTicket);
    }


    /**
     * Creating a {@link ScheduledService} running periodically a task,
     * which executes the given ticketFunction for each show booked at the auditorium.
     * This means for each show in the season the given function is periodically called
     * (e.g. booking a ticket, canceling a ticket).
     * @param ticketFunction function to execute for each show.
     * @return ScheduledService instance which controls the execution of the task.
     */
    private ScheduledService<List<Ticket>> createTicketService(Function<Show, Optional<Ticket>> ticketFunction) {
        return new ScheduledService<>() {
            @Override
            protected Task<List<Ticket>> createTask() {
                return new Task<>() {
                    @Override
                    protected List<Ticket> call() {
                        return auditorium.getShows().stream()
                                .map(ticketFunction)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .toList();
                    }
                };
            }
        };
    }

    /**
     * {@link ScheduledService} to periodically book random tickets for each show.
     */
    ScheduledService<List<Ticket>> ticketBookingService =  createTicketService(this::bookRandomTicket);

    /**
     * {@link ScheduledService} to periodically cancel random tickets for each show.
     */
    ScheduledService<List<Ticket>> ticketCancelService = createTicketService(this::cancelRandomTicket);

    /**
     * Starts the simulation for the auditorium.
     * This will start a {@link #ticketBookingService} booking tickets in the given intervall and
     * a {@link #ticketCancelService} running twice as slow.
     * Reason is, that the fuller the show gets, the more unsuccessful bookings will be happening, because seats are
     * already occupied. Canceling is usually successful, because the ticket to cancel will be chosen from the list
     * of booked tickets.
     * @param intervallSeconds intervall in seconds to send ticket book/cancel requests to each show.
     */
    public void startSimulation(int intervallSeconds) {
        ticketBookingService.setPeriod(Duration.seconds(intervallSeconds));
        ticketBookingService.start();
        ticketCancelService.setPeriod(Duration.seconds(intervallSeconds*2));
        ticketCancelService.start();
        System.out.println("Simulation started with intervall " + intervallSeconds + "s");
    }

    /**
     * Stopping the simulation for the auditorium
     * This will stop the {@link #ticketBookingService} and {@link #ticketCancelService}.
     */
    public void stopSimulation() {
        ticketBookingService.cancel();
        ticketCancelService.cancel();
        System.out.println("Simulation stopped");
    }

    /**
     * Factory method to create an Instance of {@link AuditoriumSimulationBuilder}
     * to build an instance of {@link AuditoriumSimulation}
     * @param auditoriumName name of the auditorium to build the simulation for.
     * @return new {@link AuditoriumSimulationBuilder} instance.
     */
    public static AuditoriumSimulationBuilder buildAuditorium(String auditoriumName) {
        return new AuditoriumSimulationBuilder(auditoriumName);
    }
    /**
     * Builder class to create an instance of Auditorium using the BuilderPattern.<br>
     * Example of usage:
     * <pre>
     * AuditoriumSimulation simulation =
     *     AuditoriumSimulation
     *         .buildAuditorium("Kino am See")
     *         .withSize(5, 10)
     *         .forSeason(LocalDate.now(), 2)
     *         .withShowTimes(NOON, PRIMETIME, LATENIGHT)
     *         .generateTicketsPerRow(3)
     *         .build();
     * </pre>
     */
     static class AuditoriumSimulationBuilder {
        private final String auditoriumName;
        private int rows=5;
        private int seatsPerRow=10;
        private int ticketsPerRow=seatsPerRow/3;
        private LocalDate seasonStartDate = LocalDate.now(zurichZoneId);
        private int seasonDurationDays = 3;
        private ShowTime[] seasonShowTimes = ShowTime.values();

        /**
         * Constructor for AuditoriumBuilder requiring the mandatory values.
         * The constructor is private and can not be called directly use the factory method
         * {@link AuditoriumSimulation#buildAuditorium(String)} to create an instance.
         *
         * @param auditoriumName name of the auditorium to create (mandatory)
         */
        private AuditoriumSimulationBuilder (String auditoriumName) {
            this.auditoriumName = auditoriumName;
        }

        /**
         * Defines the size of the auditorium. Default is 5 rows and 10 seatsPerRow.
         * @param rows  number of rows
         * @param seatsPerRow number of seats in each row
         * @return current instance of AuditoriumSimulationBuilder
         */
        public AuditoriumSimulationBuilder withSize(int rows, int seatsPerRow) {
            this.rows=rows;
            this.seatsPerRow=seatsPerRow;
            return this;
        }

        /**
         * Defines the season to book shows for. Default start date is today, duration 3 days.
         * @param startDate start date of the season
         * @param durationInDays duration of the season in days
         * @return current instance of AuditoriumSimulationBuilder
         */
        public AuditoriumSimulationBuilder forSeason(LocalDate startDate, int durationInDays) {
            this.seasonStartDate = startDate;
            this.seasonDurationDays = durationInDays;
            return this;
        }

        /**
         * Configures the array of ShowTime values to book shows every day. Default is all seasons defined in {@link ShowTime}.
         * @param showTimes vararg of ShowTimes to book for the show.
         *@return current instance of AuditoriumSimulationBuilder
         */
        public AuditoriumSimulationBuilder withShowTimes(ShowTime... showTimes) {
            this.seasonShowTimes = showTimes;
            return this;
        }

        /**
         * Sets the number ofTicketsPerRow. Default 1/3 of the seatsPerRow.
         * @param ticketsPerRow number of tickets to create for each row
         * @return current instance of AuditoriumSimulationBuilder
         */
        public AuditoriumSimulationBuilder generateTicketsPerRow(int ticketsPerRow) {
            this.ticketsPerRow = ticketsPerRow;
            return this;
        }

        /**
         * Terminal operation creating and returning the AuditoriumSimulation instance, based on the set or default values.
         * @return  new Instance of an {@link AuditoriumSimulation}
         */
        public AuditoriumSimulation build() {
            return new AuditoriumSimulation(auditoriumName,
                    this.rows, this.seatsPerRow,
                    this.seasonStartDate, this.seasonDurationDays,
                    this.seasonShowTimes,
                    this.ticketsPerRow);
        }

    }

}
