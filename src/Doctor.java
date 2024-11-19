public class Doctor extends Account implements Comparable<Doctor> {

    private final String id;

    // Constructor
    public Doctor(String username, String id) {
        super(username);
        this.id = id;
    }

    // Compares this doctor to another doctor
    // Returns 0 if the doctors have the same id or username
    @Override
    public int compareTo(Doctor doctor) {
        if (id.equals(doctor.id) || getUsername().equals(doctor.getUsername())) {
            return 0;
        }
        return id.compareTo(doctor.id);
    }

    // Checks if two doctors are equal
    @Override
    public boolean equals(Object o) {
        if (o instanceof Doctor doctor) {
            return this.compareTo(doctor) == 0;
        }
        return false;
    }

}