import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    static class Room {
        int roomNumber;
        String roomType;
        double pricePerNight;
        boolean isAvailable;
        String guestName;

        public Room(int roomNumber, String roomType, double pricePerNight) {
            this.roomNumber = roomNumber;
            this.roomType = roomType;
            this.pricePerNight = pricePerNight;
            this.isAvailable = true;
            this.guestName = null;
        }

        public String toString() {
            String status = isAvailable ? "Available" : "Booked by " + guestName;
            return "Room " + roomNumber + " (" + roomType + ") - $" + pricePerNight + "/night - " + status;
        }
    }

    static class Reservation {
        String reservationId;
        int roomNumber;
        String guestName;
        String guestEmail;
        String checkInDate;
        String checkOutDate;
        int numberOfNights;
        double totalPrice;

        public Reservation(String reservationId, int roomNumber, String guestName,
                           String guestEmail, String checkInDate, String checkOutDate,
                           int numberOfNights, double totalPrice) {
            this.reservationId = reservationId;
            this.roomNumber = roomNumber;
            this.guestName = guestName;
            this.guestEmail = guestEmail;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.numberOfNights = numberOfNights;
            this.totalPrice = totalPrice;
        }

        public String toString() {
            return "Reservation ID: " + reservationId + "\n"
                 + "Guest: " + guestName + " (" + guestEmail + ")\n"
                 + "Room " + roomNumber + "\n"
                 + "Check-in: " + checkInDate + " | Check-out: " + checkOutDate + "\n"
                 + "Nights: " + numberOfNights + " | Total: $" + totalPrice;
        }
    }

    static class Hotel {
        String hotelName;
        ArrayList<Room> rooms;
        ArrayList<Reservation> reservations;
        Map<Integer, Double> roomPrices;

        public Hotel(String hotelName) {
            this.hotelName = hotelName;
            this.rooms = new ArrayList<>();
            this.reservations = new ArrayList<>();
            this.roomPrices = new HashMap<>();
            initializeRooms();
        }

        private void initializeRooms() {
            rooms.add(new Room(101, "Standard", 100.0));
            rooms.add(new Room(102, "Standard", 100.0));
            rooms.add(new Room(201, "Deluxe", 150.0));
            rooms.add(new Room(202, "Deluxe", 150.0));
            rooms.add(new Room(301, "Suite", 250.0));
            rooms.add(new Room(302, "Suite", 250.0));

            for (Room room : rooms) {
                roomPrices.put(room.roomNumber, room.pricePerNight);
            }
        }

        public void displayAvailableRooms() {
            System.out.println("\n=== Available Rooms at " + hotelName + " ===");
            boolean found = false;
            for (Room room : rooms) {
                if (room.isAvailable) {
                    System.out.println(room);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No rooms available!");
            }
        }

        public void displayAllRooms() {
            System.out.println("\n=== All Rooms at " + hotelName + " ===");
            for (Room room : rooms) {
                System.out.println(room);
            }
        }

        public Room findAvailableRoom(String roomType) {
            for (Room room : rooms) {
                if (room.isAvailable && room.roomType.equalsIgnoreCase(roomType)) {
                    return room;
                }
            }
            return null;
        }

        public Room findRoom(int roomNumber) {
            for (Room room : rooms) {
                if (room.roomNumber == roomNumber) {
                    return room;
                }
            }
            return null;
        }

        public Reservation makeReservation(String guestName, String guestEmail,
                                           String roomType, String checkInDate,
                                           String checkOutDate, int numberOfNights) {
            Room room = findAvailableRoom(roomType);
            if (room == null) {
                System.out.println("Sorry, no " + roomType + " rooms available!");
                return null;
            }

            double totalPrice = room.pricePerNight * numberOfNights;
            String reservationId = "RES" + (reservations.size() + 1);

            Reservation reservation = new Reservation(reservationId, room.roomNumber,
                                                     guestName, guestEmail, checkInDate,
                                                     checkOutDate, numberOfNights, totalPrice);

            room.isAvailable = false;
            room.guestName = guestName;

            reservations.add(reservation);

            System.out.println("\nBooking Confirmed!");
            System.out.println(reservation);

            return reservation;
        }

        public boolean cancelReservation(String reservationId) {
            for (int i = 0; i < reservations.size(); i++) {
                Reservation reservation = reservations.get(i);
                if (reservation.reservationId.equals(reservationId)) {
                    Room room = findRoom(reservation.roomNumber);
                    if (room != null) {
                        room.isAvailable = true;
                        room.guestName = null;
                    }

                    reservations.remove(i);
                    System.out.println("\nReservation " + reservationId + " has been cancelled successfully!");
                    return true;
                }
            }
            System.out.println("\nReservation ID " + reservationId + " not found!");
            return false;
        }

        public void viewReservations() {
            System.out.println("\n=== All Reservations ===");
            if (reservations.isEmpty()) {
                System.out.println("No reservations found.");
                return;
            }
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
                System.out.println("---");
            }
        }

        public void viewReservation(String reservationId) {
            for (Reservation reservation : reservations) {
                if (reservation.reservationId.equals(reservationId)) {
                    System.out.println("\n=== Reservation Details ===");
                    System.out.println(reservation);
                    return;
                }
            }
            System.out.println("\nReservation ID " + reservationId + " not found!");
        }
    }

    static class ReservationSystem {
        Hotel hotel;
        Scanner scanner;

        public ReservationSystem() {
            hotel = new Hotel("Grand Palace Hotel");
            scanner = new Scanner(System.in);
        }

        public void start() {
            System.out.println("Welcome to the Hotel Reservation System!");
            System.out.println("==========================================");

            boolean running = true;
            while (running) {
                displayMainMenu();
                int choice = getUserChoice();

                switch (choice) {
                    case 1:
                        viewAvailableRooms();
                        break;
                    case 2:
                        makeReservation();
                        break;
                    case 3:
                        cancelReservation();
                        break;
                    case 4:
                        viewAllReservations();
                        break;
                    case 5:
                        viewReservationById();
                        break;
                    case 6:
                        viewAllRooms();
                        break;
                    case 7:
                        System.out.println("\nThank you for using Hotel Reservation System!");
                        System.out.println("Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("\nInvalid choice! Please try again.");
                }
            }
        }

        private void displayMainMenu() {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Make a Reservation");
            System.out.println("3. Cancel a Reservation");
            System.out.println("4. View All Reservations");
            System.out.println("5. View Reservation by ID");
            System.out.println("6. View All Rooms");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");
        }

        private int getUserChoice() {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        private void viewAvailableRooms() {
            hotel.displayAvailableRooms();
        }

        private void viewAllRooms() {
            hotel.displayAllRooms();
        }

        private void makeReservation() {
            System.out.println("\n--- Make a Reservation ---");
            hotel.displayAvailableRooms();

            System.out.print("\nEnter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter guest email: ");
            String guestEmail = scanner.nextLine();

            System.out.print("Enter room type (Standard/Deluxe/Suite): ");
            String roomType = scanner.nextLine();

            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            String checkInDate = scanner.nextLine();

            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            String checkOutDate = scanner.nextLine();

            System.out.print("Enter number of nights: ");
            int numberOfNights;
            try {
                numberOfNights = Integer.parseInt(scanner.nextLine());
                if (numberOfNights <= 0) {
                    System.out.println("Number of nights must be positive!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of nights!");
                return;
            }

            hotel.makeReservation(guestName, guestEmail, roomType,
                                  checkInDate, checkOutDate, numberOfNights);
        }

        private void cancelReservation() {
            System.out.println("\n--- Cancel a Reservation ---");
            System.out.print("Enter reservation ID to cancel: ");
            String reservationId = scanner.nextLine();
            hotel.cancelReservation(reservationId);
        }

        private void viewAllReservations() {
            hotel.viewReservations();
        }

        private void viewReservationById() {
            System.out.print("\nEnter reservation ID: ");
            String reservationId = scanner.nextLine();
            hotel.viewReservation(reservationId);
        }
    }

    public static void main(String[] args) {
        ReservationSystem system = new ReservationSystem();
        system.start();
    }
}
