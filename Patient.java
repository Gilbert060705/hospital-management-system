import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Scanner;

public class Patient {
    private String PatientID;
    private String name;
    private String DateOfBirth;
    private String gender;
    private String phoneNum;
    private String emailAdress;
    private String BloodType;
    private String pastDiagnoses;
    private String treatments;
    private String fileName;
    private static int lastID = 1003;
    private String password;
    private ArrayList<Appointment> appointmentLists = new ArrayList<>();

    public static final String SEPARATOR = ",";

    public Patient(String PatientID) {
        this.PatientID = PatientID;
        this.fileName = "patients.csv";
        retrieveDetails();
        retrieveAppointments();
    }

    public Patient(String name, String DateOfBirth, String gender, String phoneNum, String email,
            String BloodType, String pastDiagnoses, String treatments, String password) {
        this.PatientID = String.valueOf(lastID);
        this.password = password;
        this.name = name;
        this.DateOfBirth = DateOfBirth;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.emailAdress = email;
        this.BloodType = BloodType;
        this.pastDiagnoses = pastDiagnoses;
        this.treatments = treatments;
        this.fileName = "patients.csv";
        lastID++;
    }

    public List<String> read(String fileName) {
        List<String> stringArray = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                stringArray.add(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return stringArray;
    }

    public void retrieveDetails() {
        ArrayList<String> stringArray = (ArrayList<String>) read(fileName);
        for (String st : stringArray) {
            StringTokenizer star = new StringTokenizer(st, SEPARATOR);

            // Skip empty lines
            if (!star.hasMoreTokens()) {
                continue;
            }

            String checkid = star.nextToken().trim();

            // Process only the line that matches this PatientID
            if (checkid.equals(this.PatientID)) {
                // Ensure there are enough tokens before proceeding
                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for patient ID: " + this.PatientID);
                    continue;
                }
                String password = star.nextToken().trim();

                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for patient ID: " + this.PatientID);
                    continue;
                }
                String name = star.nextToken().trim();

                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for patient ID: " + this.PatientID);
                    continue;
                }
                String dateOfBirth = star.nextToken().trim();

                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for patient ID: " + this.PatientID);
                    continue;
                }
                String gender = star.nextToken().trim();

                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for patient ID: " + this.PatientID);
                    continue;
                }
                String phone = star.nextToken().trim();

                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for patient ID: " + this.PatientID);
                    continue;
                }
                String email = star.nextToken().trim();

                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for patient ID: " + this.PatientID);
                    continue;
                }
                String bloodType = star.nextToken().trim();

                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for patient ID: " + this.PatientID);
                    continue;
                }
                String pastDiagnoses = star.nextToken().trim();

                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for patient ID: " + this.PatientID);
                    continue;
                }
                String treatments = star.nextToken().trim();

                // Assign the retrieved values to the object's fields
                this.name = name;
                this.password = password;
                this.DateOfBirth = dateOfBirth;
                this.gender = gender;
                this.phoneNum = phone;
                this.emailAdress = email;
                this.BloodType = bloodType;
                this.pastDiagnoses = pastDiagnoses;
                this.treatments = treatments;

                // Since we've found and processed the patient, we can break out of the loop
                break;
            }
        }
    }

    public void retrieveAppointments() {
        String filename = "Appointments.csv";
        List<String> appointmentLines = read(filename);

        for (String line : appointmentLines) {
            StringTokenizer token = new StringTokenizer(line, SEPARATOR);
            if (!token.hasMoreTokens())
                continue; // skip empty lines

            String docID = token.nextToken().trim();
            if (!token.hasMoreTokens())
                continue;

            String patientID = token.nextToken().trim();

            // Check if the appointment belongs to this patient
            if (patientID.equals(this.PatientID)) {
                String date = token.nextToken().trim();
                String startClock = token.nextToken().trim();
                String finishClock = token.nextToken().trim();
                String status = token.nextToken().trim();
                String service = token.nextToken().trim();
                String medication = token.nextToken().trim();
                String medicationStatus = token.nextToken().trim();
                String notes = token.hasMoreTokens() ? token.nextToken().trim() : "";

                // Create Doctor object
                Doctor doctor = new Doctor(docID);

                // Create Appointment object
                Appointment appointment = new Appointment(this, doctor, date, startClock, finishClock, false);
                appointment.setStatus(status);
                appointment.setService(service);
                appointment.setMedication(medication);
                appointment.setMedicationStatus(medicationStatus);
                appointment.setNotes(notes);

                // Add to appointmentLists
                appointmentLists.add(appointment);
            }
        }
    }

    public static void write(String fileName, List data) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(fileName));

        try {
            for (int i = 0; i < data.size(); i++) {
                out.println((String) data.get(i));
            }
        } finally {
            out.close();
        }
    }

    public void saveNewPatient() {
        String patientData = this.PatientID + SEPARATOR +
                this.password + SEPARATOR +
                this.name + SEPARATOR +
                this.DateOfBirth + SEPARATOR +
                this.gender + SEPARATOR +
                this.phoneNum + SEPARATOR +
                this.emailAdress + SEPARATOR +
                this.BloodType + SEPARATOR +
                this.pastDiagnoses + SEPARATOR +
                this.treatments;

        try {
            // Use the appendToFile method to write data as a new line in the file
            appendToFile(this.fileName, patientData);
            System.out.println("Patient details saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving patient details: " + e.getMessage());
        }
    }

    // Helper method to append data as a new line to the file instead of overwriting
    public static void appendToFile(String fileName, String data) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) { // true for append mode
            out.println(data); // Write each entry on a new line
        }
    }

    public void updateDetails() {
        System.out.println("You can only change one of these 2 fields : ");
        System.out.println("1. Phone Number");
        System.out.println("2. Email");

        Scanner s = new Scanner(System.in);
        int option = -1;
        while (option != 1 && option != 2) {
            System.out.print("Which of the fields do you want to edit : ");
            option = s.nextInt();
            System.out.println("Please insert a valid input!");
        }

        if (option == 1) {
            System.out.println("Please insert your new phone number : ");
            String newPhoneNum = s.nextLine();
            updatePatientPhoneNumber(this.PatientID, newPhoneNum);
        } else if (option == 2) {
            System.out.println("Please insert your new email : ");
            String newEmail = s.nextLine();
            updatePatientEmailAddress(this.PatientID, newEmail);
        }
    }

    public void updatePatientPhoneNumber(String PatientID, String newPhoneNumber) {
        List<String> updatedData = new ArrayList<>();
        boolean recordFound = false;

        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(line, SEPARATOR);

                // Get the PatientID from the line
                String currentID = tokenizer.nextToken().trim();

                // If the current line is the one we want to update
                if (currentID.equals(PatientID)) {
                    // Reconstruct the line with the updated phone number
                    String name = tokenizer.nextToken().trim();
                    String dateOfBirth = tokenizer.nextToken().trim();
                    String gender = tokenizer.nextToken().trim();
                    tokenizer.nextToken(); // Skip the old phone number
                    String email = tokenizer.nextToken().trim();
                    String bloodType = tokenizer.nextToken().trim();
                    String pastDiagnoses = tokenizer.nextToken().trim();
                    String treatments = tokenizer.nextToken().trim();

                    // Create the updated line
                    String updatedLine = PatientID + SEPARATOR + name + SEPARATOR +
                            dateOfBirth + SEPARATOR + gender + SEPARATOR +
                            newPhoneNumber + SEPARATOR + email + SEPARATOR +
                            bloodType + SEPARATOR + pastDiagnoses + SEPARATOR +
                            treatments;

                    updatedData.add(updatedLine);
                    recordFound = true;
                } else {
                    // Add the original line if it doesn't match the PatientID
                    updatedData.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        if (recordFound) {
            // Write the updated data back to the file
            try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
                for (String line : updatedData) {
                    out.println(line);
                }
                System.out.println("Patient email address updated successfully.");
            } catch (IOException e) {
                System.out.println("Error writing file: " + e.getMessage());
            }
        } else {
            System.out.println("PatientID not found.");
        }
    }

    public void updatePatientEmailAddress(String PatientID, String newEmail) {
        List<String> updatedData = new ArrayList<>();
        boolean recordFound = false;

        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(line, SEPARATOR);

                // Get the PatientID from the line
                String currentID = tokenizer.nextToken().trim();

                // If the current line is the one we want to update
                if (currentID.equals(PatientID)) {
                    // Reconstruct the line with the updated phone number
                    String name = tokenizer.nextToken().trim();
                    String dateOfBirth = tokenizer.nextToken().trim();
                    String gender = tokenizer.nextToken().trim();
                    String phoneNum = tokenizer.nextToken().trim();
                    tokenizer.nextToken();
                    String bloodType = tokenizer.nextToken().trim();
                    String pastDiagnoses = tokenizer.nextToken().trim();
                    String treatments = tokenizer.nextToken().trim();

                    // Create the updated line
                    String updatedLine = PatientID + SEPARATOR + name + SEPARATOR +
                            dateOfBirth + SEPARATOR + gender + SEPARATOR +
                            phoneNum + SEPARATOR + newEmail + SEPARATOR +
                            bloodType + SEPARATOR + pastDiagnoses + SEPARATOR +
                            treatments;

                    updatedData.add(updatedLine);
                    recordFound = true;
                } else {
                    // Add the original line if it doesn't match the PatientID
                    updatedData.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        if (recordFound) {
            // Write the updated data back to the file
            try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
                for (String line : updatedData) {
                    out.println(line);
                }
                System.out.println("Patient phone number updated successfully.");
            } catch (IOException e) {
                System.out.println("Error writing file: " + e.getMessage());
            }
        } else {
            System.out.println("PatientID not found.");
        }
    }

    public static int getLastID() {
        return lastID;
    }

    public String getID() {
        return PatientID;
    }

    public void displayForDoctor() {
        System.out.println("Full name : " + this.name);

        System.out.println("Date of Birth : " + this.DateOfBirth);

        System.out.println("Gender : " + this.gender);

        System.out.println("Blood Type : " + this.BloodType);

        System.out.println("List of Past Diagnoses : ");
        int start = 1;
        String[] listOfDiagnoses = this.pastDiagnoses.split(";");
        for (String x : listOfDiagnoses) {
            System.out.println(start + ". " + x);
            start++;
        }

        System.out.println("List of Treatments Done : ");
        start = 1;
        String[] listOfTreatments = this.treatments.split(";");
        for (String x : listOfTreatments) {
            System.out.println(start + ". " + x);
            start++;
        }
    }

    public void displayForPatient() {
        System.out.println("Patient ID : " + PatientID);

        System.out.println("Full name : " + this.name);

        System.out.println("Date of Birth : " + this.DateOfBirth);

        System.out.println("Gender : " + this.gender);

        System.out.println("Phone Number : " + phoneNum);

        System.out.println("Email Address : " + emailAdress);

        System.out.println("Blood Type : " + this.BloodType);

        System.out.println("List of Past Diagnoses : ");
        int start = 1;
        String[] listOfDiagnoses = this.pastDiagnoses.split(";");
        for (String x : listOfDiagnoses) {
            System.out.println(start + ". " + x);
            start++;
        }

        System.out.println("List of Treatments Done : ");
        start = 1;
        String[] listOfTreatments = this.treatments.split(";");
        for (String x : listOfTreatments) {
            System.out.println(start + ". " + x);
            start++;
        }
    }

    public void displayBrief() {
        System.out.println(this.PatientID + " : " + this.name);
    }

    public void updateTreatments(String append) {
        List<String> updatedData = new ArrayList<>();
        boolean recordFound = false;

        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(line, SEPARATOR);

                // Get the PatientID from the line
                String currentID = tokenizer.nextToken().trim();

                // If the current line is the one we want to update
                if (currentID.equals(this.PatientID)) {
                    // Retrieve all fields
                    String password = tokenizer.nextToken().trim();
                    String name = tokenizer.nextToken().trim();
                    String dateOfBirth = tokenizer.nextToken().trim();
                    String gender = tokenizer.nextToken().trim();
                    String phoneNum = tokenizer.nextToken().trim();
                    String email = tokenizer.nextToken().trim();
                    String bloodType = tokenizer.nextToken().trim();
                    String pastDiagnoses = tokenizer.nextToken().trim();
                    String treatments = tokenizer.nextToken().trim();

                    // Append the new treatment
                    if (!treatments.isEmpty()) {
                        treatments += ";" + append;
                    } else {
                        treatments = append;
                    }

                    // Update the object's treatments field
                    this.treatments = treatments;

                    // Reconstruct the updated line
                    String updatedLine = currentID + SEPARATOR + password + SEPARATOR + name + SEPARATOR +
                            dateOfBirth + SEPARATOR + gender + SEPARATOR + phoneNum + SEPARATOR +
                            email + SEPARATOR + bloodType + SEPARATOR + pastDiagnoses + SEPARATOR +
                            treatments;

                    updatedData.add(updatedLine);
                    recordFound = true;
                } else {
                    // Add the original line if it doesn't match the PatientID
                    updatedData.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        if (recordFound) {
            // Write the updated data back to the file
            try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
                for (String line : updatedData) {
                    out.println(line);
                }
                System.out.println("Treatments updated successfully for patient ID: " + this.PatientID);
            } catch (IOException e) {
                System.out.println("Error writing file: " + e.getMessage());
            }
        } else {
            System.out.println("PatientID not found.");
        }
    }

    public void updateDiagnoses(String append) {
        List<String> updatedData = new ArrayList<>();
        boolean recordFound = false;

        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(line, SEPARATOR);

                // Get the PatientID from the line
                String currentID = tokenizer.nextToken().trim();

                // If the current line is the one we want to update
                if (currentID.equals(this.PatientID)) {
                    // Retrieve all fields
                    String password = tokenizer.nextToken().trim();
                    String name = tokenizer.nextToken().trim();
                    String dateOfBirth = tokenizer.nextToken().trim();
                    String gender = tokenizer.nextToken().trim();
                    String phoneNum = tokenizer.nextToken().trim();
                    String email = tokenizer.nextToken().trim();
                    String bloodType = tokenizer.nextToken().trim();
                    String pastDiagnoses = tokenizer.nextToken().trim();
                    String treatments = tokenizer.nextToken().trim();

                    // Append the new treatment
                    if (!pastDiagnoses.isEmpty()) {
                        pastDiagnoses += ";" + append;
                    } else {
                        pastDiagnoses = append;
                    }

                    // Update the object's treatments field
                    this.pastDiagnoses = pastDiagnoses;

                    // Reconstruct the updated line
                    String updatedLine = currentID + SEPARATOR + password + SEPARATOR + name + SEPARATOR +
                            dateOfBirth + SEPARATOR + gender + SEPARATOR + phoneNum + SEPARATOR +
                            email + SEPARATOR + bloodType + SEPARATOR + pastDiagnoses + SEPARATOR +
                            treatments;

                    updatedData.add(updatedLine);
                    recordFound = true;
                } else {
                    // Add the original line if it doesn't match the PatientID
                    updatedData.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        if (recordFound) {
            // Write the updated data back to the file
            try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
                for (String line : updatedData) {
                    out.println(line);
                }
                System.out.println("Treatments updated successfully for patient ID: " + this.PatientID);
            } catch (IOException e) {
                System.out.println("Error writing file: " + e.getMessage());
            }
        } else {
            System.out.println("PatientID not found.");
        }
    }

    public String getName() {
        return this.name;
    }

    public List<Doctor> getAllDoctors() {
        String filename = "doctors.csv";
        List<String> doctorLines = read(filename);
        List<Doctor> doctors = new ArrayList<>();

        for (String line : doctorLines) {
            StringTokenizer token = new StringTokenizer(line, ",");
            if (!token.hasMoreTokens()) {
                continue; // Skip empty lines
            }

            String doctorID = token.nextToken().trim();

            Doctor doctor = new Doctor(doctorID);
            doctors.add(doctor);
        }

        return doctors;
    }

    public void viewAvailableSlots() {
        Scanner s = new Scanner(System.in);
        List<Doctor> doctors = getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors are available at the moment.");
            return;
        }
        int index = 1;
        System.out.println("Please pick any of the doctors that you want to schedule an appointment with.");
        for (Doctor x : doctors) {
            System.out.println(index + ". " + x.getName());
            index++;
        }

        int choice = -1;
        while (choice < 1 || choice > doctors.size()) {
            System.out.print("Choice of doctor : ");
            choice = s.nextInt();
            System.out.println("Please eneter a valid input!");
        }

        doctors.get(choice - 1).displayAvailableSlots();
    }

    public void scheduleAppointment() {
        List<Doctor> doctors = getAllDoctors();
        Scanner s = new Scanner(System.in);
        int choice = -1;
        System.out.println("Choose one of these options: ");
        System.out.println("1. See available schedules from different doctors");
        System.out.println("2. Schedule an appointment with the selected doctor");
        choice = s.nextInt();
        s.nextLine(); // Consume newline
        while (choice != 1 && choice != 2) {
            System.out.println("Please input a valid option!");
            System.out.println("Choose one of these options: ");
            System.out.println("1. See available schedules from different doctors");
            System.out.println("2. Schedule an appointment with the selected doctor");
            choice = s.nextInt();
            s.nextLine(); // Consume newline
        }

        if (choice == 1) {
            viewAvailableSlots();
        } else if (choice == 2) {
            // Display list of doctors
            int index = 1;
            System.out.println("Please pick any of the doctors that you want to schedule an appointment with.");
            for (Doctor x : doctors) {
                System.out.println(index + ". " + x.getName());
                index++;
            }

            int option = -1;
            while (option < 1 || option > doctors.size()) {
                System.out.print("Please choose one specific doctor (number): ");
                if (s.hasNextInt()) {
                    option = s.nextInt();
                    s.nextLine(); // Consume newline
                    if (option < 1 || option > doctors.size()) {
                        System.out.println("Please input a valid option!");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    s.nextLine(); // Consume invalid input
                }
            }

            Doctor selectedDoctor = doctors.get(option - 1);

            System.out.print("Please input the date of your appointment (DD/MM/YYYY): ");
            String dateBook = s.nextLine();
            System.out.print("Please input your starting booking time (HH:MM): ");
            String startBook = s.nextLine();
            System.out.print("Please input your finishing booking time (HH:MM): ");
            String finishBook = s.nextLine();

            ArrayList<FreeAppointment> availSlots = selectedDoctor.getAvailableSlots();

            boolean appointmentScheduled = false;
            ArrayList<FreeAppointment> updatedFreeSlots = new ArrayList<>();

            for (int i = 0; i < availSlots.size(); i++) {
                FreeAppointment slot = availSlots.get(i);
                ArrayList<FreeAppointment> newSlots = slot.bookFreeAppointment(startBook, finishBook, dateBook);
                if (!newSlots.isEmpty()) {
                    appointmentScheduled = true;
                    selectedDoctor.updateFreeAppointments(i);
                    Appointment bookedAppointment = new Appointment(this, selectedDoctor, dateBook,
                            startBook, finishBook, true);
                    System.out.println(
                            "Your appointment has been scheduled successfully. It is now on pending to wait for the approval from the corresponding doctor");
                    appointmentLists.add(bookedAppointment);
                    break;
                }
            }

            if (!appointmentScheduled) {
                System.out.println("Sorry, no available slots can accommodate your requested schedule.");
            }
        }
    }

    public void displayAppointments() {
        int start = 1;
        for (Appointment x : this.appointmentLists) {
            System.out.println(start + ". \n");
            x.getDetailsForPatient();
            System.out.println("");
        }
    }

    public void reScheduleAppointment() {
        displayAppointments();
        int option = -1;
        Scanner s = new Scanner(System.in);
        while (option < 1 || option > appointmentLists.size()) {
            System.out.print("Please pick the appointment that you want to re-schedule : ");
            option = s.nextInt();
            s.nextLine();
        }

    }

}
