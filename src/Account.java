import java.io.*;
import java.util.*;

public abstract class Account {

    private final String username;
    private final File messages;
    private final File personalNotes;

    // Only constructor
    public Account(String username) {
        this.username = username;
        messages = new File("data/" + username + "ReceivedMessages.txt");
        personalNotes = new File("data/" + username + "PersonalNotes.txt");
    }

    // Sends a message to another account
    public void sendMessage(Account sender, Account recipient, String message) {
        if (sender instanceof Doctor || (sender instanceof Patient && recipient instanceof Doctor)) {
            if (recipient.addMessage(sender.getUsername(), message)) {
                System.out.println("Message sent.");
            }
            else {
                System.out.println("Message failed to send.");
            }
        }
    }

    // Adds a message to the account's messages file
    private boolean addMessage(String senderName, String message) {
        writeFile(messages, senderName + ": " + message + "\n\n", true);
        return true;
    }

    // Reads and displays the account's messages file
    public void readMessages() {
        if (messages.length() == 0) {
            System.out.println("No messages.");
        }
        else {
            readFile(messages);
        }
    }

    // Clears the account's messages file
    public void deleteMessages() {
        writeFile(messages, "", false);
        System.out.print("Messages deleted.");
    }

    // Adds personal notes to the account's personal notes file
    public void addPersonalNotes(String notes) {
        writeFile(personalNotes, notes + "\n\n", true);
    }

    // Reads and displays the account's personal notes file
    public void readPersonalNotes() {
        if (personalNotes.length() == 0) {
            System.out.println("No personal notes.");
        }
        else {
            readFile(personalNotes);
        }
    }

    // Clears the account's personal notes file
    public void deletePersonalNotes() {
        writeFile(personalNotes, "", false);
        System.out.print("Personal notes deleted.");
    }

    // Returns the account's username
    public String getUsername() {
        return username;
    }

    // Helper method to read a file
    private void readFile(File file) {
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
        }
        catch (FileNotFoundException _) {
            System.out.println("No data.");
        }
    }

    // Helper method to write to a file
    private void writeFile(File file, String content, boolean append) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, append))) {
            writer.write(content);
        }
        catch (IOException _) {
            // Intentionally blank
        }
    }

}