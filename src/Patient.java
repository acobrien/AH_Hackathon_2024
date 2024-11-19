import java.io.*;
import java.util.*;

public class Patient extends Account implements Comparable<Patient> {

    private final File doctorNotes;

    // Constructor
    public Patient(String username) {
        super(username);
        doctorNotes = new File("data/" + username + "DoctorNotes.txt");
    }

    // Adds notes from a doctor to the patient's notes file
    // Only accessible to doctors
    public void addDoctorNotes(Doctor doctor, String notes) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(doctorNotes, true));
            writer.write(doctor.getUsername() + ": " + notes + "\n\n");
            writer.close();
            System.out.println("Notes added.");
        }
        catch (Exception _) {
            // Intentionally blank
        }
    }

    // Reads the notes from the patient's notes file
    // Only accessible to doctors
    public void readDoctorNotes() {
        try {
            if (doctorNotes.length() == 0) {
                System.out.println("No notes from doctors.");
                return;
            }
            Scanner reader = new Scanner(doctorNotes);
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("No notes from doctors.");
        }
    }

    // Deletes the notes from the patient's notes file
    // Only accessible to doctors
    public void deleteDoctorNotes() {
        try {
            PrintWriter writer = new PrintWriter(doctorNotes);
            writer.write("");
            writer.close();
            System.out.println("Notes deleted.");
        }
        catch (FileNotFoundException _) {
            // Intentionally blank
        }
    }

    // Compares patients by username
    @Override
    public int compareTo(Patient patient) {
        return getUsername().compareTo(patient.getUsername());
    }

    // Checks if two patients are equal
    @Override
    public boolean equals(Object o) {
        if (o instanceof Patient patient) {
            return this.compareTo(patient) == 0;
        }
        return false;
    }

}