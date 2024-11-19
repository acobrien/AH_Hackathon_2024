import java.util.*;

public class Driver {
//test
    private static boolean logout;

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        AccountManager ValdostaMedicine = new AccountManager();
        boolean exit = false;

        printWelcome();

        while (!exit) {
            logout = false;
            printOptions1();

            String choice1 = input.nextLine();
            switch (choice1) {

                case "1": // Log in
                    loginOptions(input, ValdostaMedicine);
                    break;

                case "2": // Create a Doctor account
                    createDoctorOptions(input, ValdostaMedicine);
                    break;

                case "3": // Create a Patient account
                    createPatientOptions(input, ValdostaMedicine);
                    break;

                case "4": // Delete an account
                    deleteAccountOptions(input, ValdostaMedicine);
                    break;

                case "5": // Clear all data
                    clearDataOptions(input, ValdostaMedicine);
                    break;

                case "6": // Exit
                    exit = true;
                    break;

                default: // Invalid input
                    System.out.println("Invalid input. Please try again.");
                    break;

            }// end main switch
        }// end main while
    }// end main method

    // Extracted methods to display and process user input

    private static void printWelcome() {
        System.out.println("\n------------------------------------------------------");
        System.out.println("|                                                    |");
        System.out.println("| Welcome to the Valdosta Medicine Messaging System! |");
        System.out.println("|                                                    |");
        System.out.println("------------------------------------------------------\n");
    }

    private static void printOptions1() {
        System.out.println("-----\nWhat would you like to do? (Enter the number of your choice)");
        System.out.println("1. Log in");
        System.out.println("2. Create a Doctor account");
        System.out.println("3. Create a Patient account");
        System.out.println("4. Delete an account");
        System.out.println("5. Clear all data");
        System.out.println("6. Exit");
        System.out.print(">");
    }

    private static void printOptions2() {
        System.out.println("1. Send a message");
        System.out.println("2. Read messages");
        System.out.println("3. Clear Messages");
        System.out.println("4. Make notes");
        System.out.println("5. Read notes");
        System.out.println("6. Clear notes");
        System.out.println("7. Log out");
        System.out.print(">");
    }

    public static void loginOptions(Scanner input, AccountManager ValdostaMedicine) {
        System.out.println("Enter your username:");
        System.out.print(">");
        String username = input.nextLine();
        Account account = ValdostaMedicine.getAccount(username);
        if (account == null) {
            System.out.println("Account not found. Please try again.");
            logout = true;
        }

        while (!logout) {

            System.out.println("-----\nWelcome, " + username + "! What would you like to do?");
            printOptions2();
            String choice2 = input.nextLine();

            switch (choice2) {

                case "1": // Send a message
                    sendMessageOptions(input, ValdostaMedicine, account);
                    break;

                case "2": // Read messages
                    account.readMessages();
                    break;

                case "3": // Clear messages
                    account.deleteMessages();
                    break;

                case "4": // Make notes
                    addNotesOptions(account, input, ValdostaMedicine);
                    break;

                case "5": // Read notes
                    readNotesOptions(account, input, ValdostaMedicine);
                    break;

                case "6": // Clear notes
                    deleteNotesOptions(account, input, ValdostaMedicine);
                    break;

                case "7": // Log out
                    logout = true;
                    break;

                default: // Invalid input
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }// end logged in switch
    }

    private static void createPatientOptions(Scanner input, AccountManager ValdostaMedicine) {
        String username;
        System.out.println("Enter a username for the account:");
        System.out.print(">");
        username = input.nextLine();
        if (ValdostaMedicine.addPatient(username)) {
            System.out.println("Patient account created successfully!");
        } else {
            System.out.println("Patient account creation failed.");
        }
    }

    private static void createDoctorOptions(Scanner input, AccountManager ValdostaMedicine) {
        String username;
        System.out.println("Enter a username for the account:");
        System.out.print(">");
        username = input.nextLine();
        System.out.println("Enter a valid Medical ID for the account:");
        System.out.print(">");
        String id = input.nextLine();
        if (ValdostaMedicine.addDoctor(username, id)) {
            System.out.println("Doctor account created successfully!");
        } else {
            System.out.println("Doctor account creation failed. ID may have been invalid.");
        }
    }

    private static void sendMessageOptions(Scanner input, AccountManager ValdostaMedicine,
                                           Account account) {
        System.out.println("Enter the username of the recipient:");
        System.out.print(">");
        String recipient = input.nextLine();
        if (ValdostaMedicine.getAccount(recipient) == null) {
            System.out.println("Recipient not found. Please try again.");
            return;
        }
        System.out.println("Enter the message you would like to send:");
        System.out.print(">");
        String message = input.nextLine();
        account.sendMessage(account, ValdostaMedicine.getAccount(recipient), message);
    }

    private static void addNotesOptions(Account account, Scanner input,
                                        AccountManager ValdostaMedicine) {
        if (account instanceof Doctor d) {
            doctorMakeNotes(d, input, account, ValdostaMedicine);
        }
        else {
            System.out.println("Enter the notes you would like to make:");
            System.out.print(">");
            String notes = input.nextLine();
            account.addPersonalNotes(notes);
        }
    }

    private static void doctorMakeNotes(Doctor d, Scanner input, Account account,
                                        AccountManager ValdostaMedicine) {
        System.out.println("Do you want to make personal notes or notes for a patient?");
        System.out.println("1. Personal notes");
        System.out.println("2. Patient notes");
        System.out.print(">");
        String choice3 = input.nextLine();

        if (choice3.equals("1")) {
            System.out.println("Enter the notes you would like to make:");
            System.out.print(">");
            String notes = input.nextLine();
            account.addPersonalNotes(notes);
        }

        else if (choice3.equals("2")) {
            System.out.println("Enter the username of the patient you would like to make notes for:");
            System.out.print(">");
            String patient = input.nextLine();

            if (ValdostaMedicine.getAccount(patient) == null) {
                System.out.println("Patient not found.");
                return;
            }

            System.out.println("Enter the notes you would like to make:");
            System.out.print(">");
            String notes = input.nextLine();
            ((Patient) ValdostaMedicine.getAccount(patient)).addDoctorNotes(d, notes);
        }
    }

    private static void readNotesOptions(Account account, Scanner input,
                                         AccountManager ValdostaMedicine) {
        if (account instanceof Patient p) {
            p.readPersonalNotes();
        }

        else if (account instanceof Doctor) {
            System.out.println("Read personal notes or notes for a patient?");
            System.out.println("1. Personal notes");
            System.out.println("2. Patient notes");
            System.out.print(">");
            String choice4 = input.nextLine();

            if (choice4.equals("1")) {
                account.readPersonalNotes();
            }

            else if (choice4.equals("2")) {
                System.out.println("Enter the username of the patient you would like to " +
                        "read notes for:");
                System.out.print(">");
                String patient = input.nextLine();

                if (ValdostaMedicine.getAccount(patient) == null || !(ValdostaMedicine.getAccount(patient)
                        instanceof Patient)) {
                    System.out.println("Patient not found.");
                    return;
                }

                ((Patient) ValdostaMedicine.getAccount(patient)).readDoctorNotes();
            }
        }
    }

    private static void deleteNotesOptions(Account account, Scanner input,
                                           AccountManager ValdostaMedicine) {
        if (account instanceof Patient p) {
            p.deletePersonalNotes();
            p.deleteDoctorNotes();
        }

        else if (account instanceof Doctor) {
            doctorDeleteNotesOptions(input, account, ValdostaMedicine);
        }
    }

    private static void doctorDeleteNotesOptions(Scanner input, Account account,
                                                 AccountManager ValdostaMedicine) {
        System.out.println("Delete personal notes or patient notes?");
        System.out.println("1. Personal notes");
        System.out.println("2. Patient notes");
        System.out.print(">");
        String choice5 = input.nextLine();

        if (choice5.equals("1")) {
            account.deletePersonalNotes();
        }

        else if (choice5.equals("2")) {
            System.out.println("Enter the username of the patient you would like to delete notes for:");
            System.out.print(">");
            String patient = input.nextLine();

            if (ValdostaMedicine.getAccount(patient) == null || !(ValdostaMedicine.getAccount(patient)
                    instanceof Patient)) {
                System.out.println("Patient not found.");
                return;
            }

            ((Patient) ValdostaMedicine.getAccount(patient)).deleteDoctorNotes();
        }
    }

    private static void deleteAccountOptions(Scanner input, AccountManager ValdostaMedicine) {
        System.out.println("Enter admin password (admin):");
        System.out.print(">");
        String password = input.nextLine();

        if (!password.equals("admin")) {
            System.out.println("Incorrect password. Account deletion failed.");
            return;
        }

        System.out.println("Enter the username of the account you would like to delete:");
        System.out.print(">");
        String username = input.nextLine();

        if (ValdostaMedicine.deleteAccount(username)) {
            System.out.println("Account deleted successfully!");
        }

        else {
            System.out.println("Account deletion failed.");
        }
    }

    private static void clearDataOptions(Scanner input, AccountManager ValdostaMedicine) {
        System.out.println("Enter admin password (admin):");
        System.out.print(">");
        String password = input.nextLine();

        if (!password.equals("admin")) {
            System.out.println("Incorrect password. Data wipe failed.");
            return;
        }

        System.out.println("Are you sure you want to clear all data? (y/n)");
        System.out.print(">");
        String choice5 = input.nextLine();

        if (choice5.equals("y")) {
            ValdostaMedicine.clearData();
            System.out.println("All data cleared.");
        }

        else {
            System.out.println("Data not cleared.");
        }
    }// end extracted methods

}// end Driver class