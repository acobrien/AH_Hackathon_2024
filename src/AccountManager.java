import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AccountManager {

    private final Map<String, Account> accounts;
    HashSet<String> medIds = new HashSet<>();

    // Constructor
    public AccountManager() {
        accounts = new HashMap<>();
        createDirectories();
        loadData();
    }

    // Adds a doctor to the system if the username is not taken and the id is unique & valid
    // Returns true if the doctor was added, false otherwise
    // Writes the doctor to the doctors.txt file
    public boolean addDoctor(String username, String id) {
        if (isValidId(id) && !accounts.containsKey(username)) {
            Doctor doctor = new Doctor(username, id);
            accounts.put(username, doctor);
            writeFile(new File("data/doctors.txt"), username + "," + id + "\n");
            return true;
        }
        return false;
    }

    // Adds a patient to the system if the username is not taken
    // Returns true if the patient was added, false otherwise
    // Writes the patient to the patients.txt file
    public boolean addPatient(String username) {
        if (!accounts.containsKey(username)) {
            Patient patient = new Patient(username);
            accounts.put(username, patient);
            writeFile(new File("data/patients.txt"), username + "\n");
            return true;
        }
        return false;
    }

    // Returns the account with the given username, or null if no such account exists
    public Account getAccount(String username) {
        return accounts.get(username);
    }

    // Deletes the account with the given username
    // Returns true if the account was deleted, false otherwise
    // Also deletes the account's received messages, personal notes, and doctor notes files
    public boolean deleteAccount(String username) {
        Account account = accounts.remove(username);
        if (account != null) {
            deleteFileIfExists(Path.of("data/" + username + "ReceivedMessages.txt"));
            deleteFileIfExists(Path.of("data/" + username + "PersonalNotes.txt"));
            deleteFileIfExists(Path.of("data/" + username + "DoctorNotes.txt"));

            if (account instanceof Doctor) {
                removeAccountFromFile(new File("data/doctors.txt"), account);
            }
            else if (account instanceof Patient) {
                removeAccountFromFile(new File("data/patients.txt"), account);
            }
            return true;
        }
        return false;
    }

    // Completely wipes all data from the system, including the accounts, medIds, and files
    public void clearData() {
        for (String username : new ArrayList<>(accounts.keySet())) {
            deleteAccount(username);
        }
        deleteFileIfExists(Path.of("data/doctors.txt"));
        deleteFileIfExists(Path.of("data/patients.txt"));
        deleteFileIfExists(Path.of("resources/medIds.txt"));
    }

    // Returns true if the given id is in the medIds set
    private boolean isValidId(String id) {
        return medIds.contains(id);
    }

    // Create directories if they don't exist. Return values irrelevant.
    private void createDirectories() {
        boolean ignoredData = new File("data").mkdir();
        boolean ignoredResources = new File("resources").mkdir();
    }

    // Load data from files to current session
    private void loadData() {
        loadDoctors();
        loadPatients();
        loadMedIds();
    }

    // Load doctors from doctors.txt file
    private void loadDoctors() {
        File doctorFile = new File("data/doctors.txt");
        for (String line : readFileLines(doctorFile)) {
            String[] tokens = line.split(",");
            Doctor doctor = new Doctor(tokens[0], tokens[1]);
            accounts.put(tokens[0], doctor);
        }
    }

    // Load patients from patients.txt file
    private void loadPatients() {
        File patientFile = new File("data/patients.txt");
        for (String line : readFileLines(patientFile)) {
            Patient patient = new Patient(line);
            accounts.put(line, patient);
        }
    }

    // Load medIds from medIds.txt file into a HashSet which prevents duplicates
    private void loadMedIds() {
        File medIdFile = new File("resources/medIds.txt");
        if (!medIdFile.exists()) {
            try {
                boolean ignoredIds = medIdFile.createNewFile();
                fillMedIds(medIdFile);
            }
            catch (IOException e) {
                System.out.println("Error creating medIds file.");
            }
        }
        medIds.addAll(readFileLines(medIdFile));
    }

    // Fill the medIds file with 100 randomly generated ids
    // Each id is a 6-character string with a mix of uppercase letters and digits
    private void fillMedIds(File medIdFile) {
        try {
            PrintWriter writer = new PrintWriter(medIdFile);
            for (int i = 0; i < 100; i++) {
                writer.write(generateId() + "\n");
            }
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error writing medIds file.");
        }
    }

    // Removes the given account from the given file by making a list of
    // all accounts except the one to remove and writing the list back to the file
    private static void removeAccountFromFile(File accountFile, Account remove) {
        try {
            List<String> accountList = new ArrayList<>();
            Scanner accountScanner = new Scanner(accountFile);
            while (accountScanner.hasNextLine()) {
                String line = accountScanner.nextLine();
                String[] tokens = line.split(",");
                if (!tokens[0].trim().equals(remove.getUsername().trim())) {
                    accountList.add(line);
                }
            }
            accountScanner.close();
            PrintWriter clear = new PrintWriter(accountFile);
            clear.write("");
            clear.close();
            FileWriter accountFileWriter = new FileWriter(accountFile, true);
            PrintWriter accountWriter = new PrintWriter(accountFileWriter);
            for (String account : accountList) {
                accountWriter.println(account);
            }
            accountFileWriter.close();
            accountWriter.close();
        }
        catch (IOException e) {
            System.out.println("Error removing account from file.");
        }
    }

    // Helper method to read a file and return lines as a list
    private List<String> readFileLines(File file) {
        List<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file: " + file);
        }
        return lines;
    }

    // Helper method to generate a random id
    private String generateId() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            if (Math.random() < 0.5) {
                int digit = (int) (Math.random() * 10);
                id.append(digit);
            }
            else {
                Random r = new Random();
                char c = (char) (r.nextInt(26) + 'A');
                id.append(c);
            }
        }
        return id.toString();
    }

    // Helper method to delete a file if it exists
    private void deleteFileIfExists(Path path) {
        try {
            if (Files.exists(path)) {
                System.out.println("Deleting: " + path);
                Files.delete(path);
            }
        }
        catch (IOException _) {
            System.out.println("Failed to delete: " + path);
        }
    }

    // Helper method to write to a file
    private void writeFile(File file, String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.write(content);
        }
        catch (IOException e) {
            System.out.println("Error writing file: " + file);
        }
    }

}