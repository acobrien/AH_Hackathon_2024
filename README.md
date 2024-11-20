Sample data provided for accounts "Dr. John", "Dr. Jan", "Joe", and "Jerry".
Doctor and Patient accounts can be created or removed.
To create a doctor, you must enter a valid medical id.
Medical id's will be generated in resources/medIds.txt.
Currently they are a 6-digit, alphanumeric code (only capital letters).
Doctors can message patients and other doctors.
Patients can message only doctors.
Doctors can make personal notes or notes on patients.
Patients can make only personal notes.
Personal notes can only be read by their writer.
Notes on patients can be read by any doctor, but no patients can read them.
All messages or all notes can be cleared, cannot delete an individual message/note.
Removing accounts requires the admin password: "admin".
All messages, personal notes, and notes by doctors will be deleted.
Clearing all data deletes all txt files and removes accounts from memory.
If data or resource directories are not found, they will be generated.
doctors.txt and patients.txt will be generated when a doctor/patient is created.
