import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Doctor {
    private String DoctorID;
    private String name;
    private String password;
    private ArrayList<Patient> patientsList = new ArrayList<>();
    private String AppointmentsCSV;
    private String fileName = "doctors.csv";
    public static final String SEPARATOR = ",";
    private ArrayList<FreeAppointment> availableSlots = new ArrayList<>();
    private List<Appointment> acceptedAppointments = new ArrayList<>();

    public Doctor(String DoctorID) {
        this.DoctorID = DoctorID;
        retrieveDetails();
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

            // Process only the line that matches this DoctorID
            if (checkid.equals(this.DoctorID)) {
                // Now process the line for this doctor

                // Ensure there are enough tokens before proceeding
                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for doctor ID: " + this.DoctorID);
                    continue;
                }
                String name = star.nextToken().trim();

                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for doctor ID: " + this.DoctorID);
                    continue;
                }
                String password = star.nextToken().trim();

                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for doctor ID: " + this.DoctorID);
                    continue;
                }
                String patients = star.nextToken().trim();

                if (!patients.isEmpty()) {
                    String[] listOfPatients = patients.split(";");
                    for (String patientIDs : listOfPatients) {
                        Patient temp = new Patient(patientIDs.trim());
                        patientsList.add(temp);
                    }
                }

                if (!star.hasMoreTokens()) {
                    System.out.println("Incomplete data for doctor ID: " + this.DoctorID);
                    continue;
                }
                String CSVApp = star.nextToken().trim();

                this.name = name;
                this.password = password;
                this.AppointmentsCSV = CSVApp;

                readAvailableSlots();

                // Since we've found and processed the doctor, we can break out of the loop
                break;
            }
        }
    }

    private void readAvailableSlots() {
        String filename = this.AppointmentsCSV;
        List<String> slotLines = read(filename);
        for (String line : slotLines) {
            StringTokenizer token = new StringTokenizer(line, SEPARATOR);
            String docID = token.nextToken().trim();
            String start = token.nextToken().trim();
            String finish = token.nextToken().trim();
            String date = token.nextToken().trim();

            FreeAppointment slot = new FreeAppointment(this, start, finish, date, this.AppointmentsCSV, false);
            availableSlots.add(slot);
        }
    }

    public void addPatient(Patient newPatient) {
        // Add the patient to the in-memory list
        patientsList.add(newPatient);

        // Update the patients list in the file
        List<String> fileContents = read(fileName);
        List<String> updatedContents = new ArrayList<>();

        for (String line : fileContents) {
            StringTokenizer star = new StringTokenizer(line, SEPARATOR);
            String checkid = star.nextToken().trim();

            // Check if the line is the current doctor's entry
            if (checkid.equals(this.DoctorID)) {
                // Reconstruct the line with the new patient ID appended
                String updatedLine = checkid + SEPARATOR + name + SEPARATOR + password + SEPARATOR;

                // Append the new patient ID to the existing patient list
                if (!patientsList.isEmpty()) {
                    for (Patient patient : patientsList) {
                        updatedLine += patient.getID() + ";";
                    }
                    updatedLine = updatedLine.substring(0, updatedLine.length() - 1); // Remove trailing ":"
                }

                // Append the AppointmentsCSV field
                updatedLine += SEPARATOR + AppointmentsCSV;
                updatedContents.add(updatedLine);
            } else {
                // Add the line as is if it's not the doctor's entry we want to update
                updatedContents.add(line);
            }
        }

        // Write the updated contents back to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (String line : updatedContents) {
                writer.println(line);
            }
            System.out.println("Patient added successfully to doctor " + this.DoctorID);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public void displayListOfPatients() {
        if (patientsList.isEmpty()) {
            System.out.println("You currently have no patients.");
        }
        Scanner s = new Scanner(System.in);
        int choose = -1;
        int quitOpt = 0;
        while (choose != quitOpt) {
            for (int i = 0; i < patientsList.size(); i++) {
                System.out.print(i + 1 + ". ");
                patientsList.get(i).displayBrief();
                if (i == patientsList.size() - 1) {
                    System.out.println(i + 2 + ". Back");
                    quitOpt = i + 2;
                }
            }

            choose = s.nextInt();
            if (choose < 0 && choose > quitOpt) {
                continue;
            } else if (choose == quitOpt) {
                break;
            }
            patientsList.get(choose - 1).displayForDoctor();
        }
    }

    public String getName() {
        return this.name;
    }

    public void updatePatientDetails() {
        // Check if there are any patients in the list
        if (patientsList.isEmpty()) {
            System.out.println("You currently have no patients to edit.");
            return;
        }

        // Display all patients
        System.out.println("Select a patient to edit:");
        for (int i = 0; i < patientsList.size(); i++) {
            System.out.print((i + 1) + ". ");
            patientsList.get(i).displayBrief(); // Assuming this shows a brief summary of the patient
        }
        System.out.println((patientsList.size() + 1) + ". Back");

        Scanner scanner = new Scanner(System.in);
        int patientChoice = -1;

        // Loop until a valid patient is chosen or the user chooses to go back
        while (patientChoice < 1 || patientChoice > patientsList.size() + 1) {
            System.out.print("Enter your choice: ");
            patientChoice = scanner.nextInt();

            if (patientChoice == patientsList.size() + 1) {
                System.out.println("Returning to the previous menu.");
                return; // Exit if the user chooses "Back"
            }

            if (patientChoice < 1 || patientChoice > patientsList.size()) {
                System.out.println("Invalid choice. Please select a valid patient.");
            }
        }

        // Retrieve the selected patient
        Patient selectedPatient = patientsList.get(patientChoice - 1);

        // Ask user which section to edit
        System.out.println("Select which section to edit for this patient:");
        System.out.println("1. Diagnoses"); // Placeholder for the actual detail, e.g., "Phone Number"
        System.out.println("2. Treatments"); // Placeholder for another detail, e.g., "Email Address"
        int sectionChoice = -1;

        while (sectionChoice < 1 || sectionChoice > 2) {
            System.out.print("Enter your choice (1 or 2): ");
            sectionChoice = scanner.nextInt();

            if (sectionChoice < 1 || sectionChoice > 2) {
                System.out.println("Invalid choice. Please select a valid section.");
            }
        }

        // Update the selected section based on user choice
        scanner.nextLine(); // Consume the newline character

        if (sectionChoice == 1) {
            System.out.print("Enter a new diagnosis : ");
            String newValue = scanner.nextLine();
            selectedPatient.updateDiagnoses(newValue); // Assuming Patient class has a setOption1 method
            System.out.println("Diagnose updated successfully.");
        } else if (sectionChoice == 2) {
            System.out.print("Enter a new treatment :  ");
            String newValue = scanner.nextLine();
            selectedPatient.updateTreatments(newValue); // Assuming Patient class has a setOption2 method
            System.out.println("Treatments updated successfully.");
        }
    }

    public String getID() {
        return this.DoctorID;
    }

    public void displayAvailableSlots() {
        if (availableSlots.size() == 0) {
            System.out.println("You have not set any availabillity schedule.");
        } else {
            int start = 1;
            for (FreeAppointment x : availableSlots) {
                System.out.print(start + ". ");
                x.displayFree();
                start++;
            }
        }
    }

    public void addAvailability() {
        Scanner s = new Scanner(System.in);
        System.out.println("Please type  in the date when you are available : ");
        String date = s.nextLine();
        System.out.println("Please insert the starting time when you are free : ");
        String start = s.nextLine();
        System.out.println("Please insert the finisihing time when you are free : ");
        String finish = s.nextLine();

        FreeAppointment setNew = new FreeAppointment(this, start, finish, date, this.AppointmentsCSV, true);
        availableSlots.add(setNew);
    }

    public void reviewPendingAppointments() {
        String filename = "Appointments.csv";
        List<String> appointmentLines = read(filename);
        List<Appointment> pendingAppointments = new ArrayList<>();

        for (String line : appointmentLines) {
            StringTokenizer token = new StringTokenizer(line, SEPARATOR);
            String docID = token.nextToken().trim();

            // Check if the appointment belongs to this doctor
            if (docID.trim().equals(this.DoctorID.trim())) {
                String patientID = token.nextToken().trim();
                String date = token.nextToken().trim();
                String startClock = token.nextToken().trim();
                String finishClock = token.nextToken().trim();
                String status = token.nextToken().trim();
                String service = token.nextToken().trim();
                String medication = token.nextToken().trim();
                String medicationStatus = token.nextToken().trim();
                String notes = token.hasMoreTokens() ? token.nextToken().trim() : "";

                // Check if the status is "pending"
                if (status.equalsIgnoreCase("pending")) {
                    // Create Patient and Appointment objects
                    Patient patient = new Patient(patientID);
                    Appointment appointment = new Appointment(patient, this, date, startClock, finishClock, false);
                    appointment.setStatus(status);
                    appointment.setService(service);
                    appointment.setMedication(medication);
                    appointment.setMedicationStatus(medicationStatus);
                    appointment.setNotes(notes);

                    pendingAppointments.add(appointment);
                }
            }
        }

        if (pendingAppointments.isEmpty()) {
            System.out.println("You have no pending appointments.");
            return;
        }

        // Display pending appointments
        System.out.println("Pending Appointments:");
        int index = 1;
        for (Appointment appointment : pendingAppointments) {
            System.out.println(index + ". " + appointment.getDetails());
            index++;
        }

        // Allow the doctor to approve or reject appointments
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of the appointment to update (or 0 to exit): ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > pendingAppointments.size()) {
            System.out.println("Exiting to the main menu.");
            return;
        }

        Appointment selectedAppointment = pendingAppointments.get(choice - 1);

        // Update appointment status
        System.out.println("1. Approve Appointment");
        System.out.println("2. Reject Appointment");
        System.out.print("Enter your choice: ");
        int action = scanner.nextInt();

        if (action == 1) {
            selectedAppointment.setStatus("approved");
            System.out.println("Appointment approved.");
        } else if (action == 2) {
            selectedAppointment.setStatus("rejected");
            System.out.println("Appointment rejected.");
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        // Update the appointment in the CSV file
        updateAppointmentInCSV(selectedAppointment);
    }

    private void updateAppointmentInCSV(Appointment updatedAppointment) {
        String filename = "Appointments.csv";
        List<String> appointmentLines = read(filename);
        List<String> updatedLines = new ArrayList<>();

        for (String line : appointmentLines) {
            StringTokenizer token = new StringTokenizer(line, SEPARATOR);
            String docID = token.nextToken().trim();
            String patientID = token.nextToken().trim();
            String date = token.nextToken().trim();
            String startClock = token.nextToken().trim();
            String finishClock = token.nextToken().trim();
            String status = token.nextToken().trim();

            // Reconstruct the unique identifier for the appointment
            if (docID.equals(updatedAppointment.getDoctor().getID()) &&
                    patientID.equals(updatedAppointment.getPatient().getID()) &&
                    date.equals(updatedAppointment.getDate()) &&
                    startClock.equals(updatedAppointment.getStartClock()) &&
                    finishClock.equals(updatedAppointment.getFinishClock())) {

                // Update the status
                StringBuilder sb = new StringBuilder();
                sb.append(docID).append(",");
                sb.append(patientID).append(",");
                sb.append(date).append(",");
                sb.append(startClock).append(",");
                sb.append(finishClock).append(",");
                sb.append(updatedAppointment.getStatus()).append(",");
                sb.append(updatedAppointment.getService()).append(",");
                sb.append(updatedAppointment.getMedication()).append(",");
                sb.append(updatedAppointment.getMedicationStatus()).append(",");
                sb.append(updatedAppointment.getNotes());

                updatedLines.add(sb.toString());
            } else {
                // Keep the line as is
                updatedLines.add(line);
            }
        }

        // Write the updated lines back to the CSV
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String line : updatedLines) {
                writer.println(line);
            }
            System.out.println("Appointment status updated successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public void viewAcceptedAppointments() {
        String filename = "Appointments.csv";
        List<String> appointmentLines = read(filename);

        for (String line : appointmentLines) {
            StringTokenizer token = new StringTokenizer(line, SEPARATOR);
            String docID = token.nextToken().trim();

            // Check if the appointment belongs to this doctor
            if (docID.equals(this.DoctorID)) {
                String patientID = token.nextToken().trim();
                String date = token.nextToken().trim();
                String startClock = token.nextToken().trim();
                String finishClock = token.nextToken().trim();
                String status = token.nextToken().trim();
                String service = token.nextToken().trim();
                String medication = token.nextToken().trim();
                String medicationStatus = token.nextToken().trim();
                String notes = token.hasMoreTokens() ? token.nextToken().trim() : "";

                // Check if the status is "pending"
                if (status.equalsIgnoreCase("approved")) {
                    // Create Patient and Appointment objects
                    Patient patient = new Patient(patientID);
                    Appointment appointment = new Appointment(patient, this, date, startClock, finishClock, false);
                    appointment.setStatus(status);
                    appointment.setService(service);
                    appointment.setMedication(medication);
                    appointment.setMedicationStatus(medicationStatus);
                    appointment.setNotes(notes);

                    acceptedAppointments.add(appointment);
                }
            }

            if (acceptedAppointments.isEmpty()) {
                System.out.println("You have no scheduled appointments.");
                return;
            }

            // Display pending appointments
            System.out.println("Accepted Appointments:");
            int index = 1;
            for (Appointment appointment : acceptedAppointments) {
                System.out.println(index + ". " + appointment.getDetails());
                index++;
            }
        }
    }

    public void updateRecords() {
        viewAcceptedAppointments();
        if (acceptedAppointments.size() == 0) {
            return;
        }
        Scanner s = new Scanner(System.in);
        int option = -1;
        while (option < 1 || option > this.acceptedAppointments.size()) {
            System.out.print("Which of these appointments that you would like to record? ");
            if (s.hasNextInt()) {
                option = s.nextInt();
                s.nextLine(); // Consume the newline character
                if (option < 1 || option > this.acceptedAppointments.size()) {
                    System.out.println("Please input a valid option! ");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                s.nextLine(); // Consume the invalid input
                option = -1; // Reset option to stay in the loop
            }
        }

        Appointment selectedAppointment = acceptedAppointments.get(option - 1);

        System.out.print("Is this appointment cancelled? (Y/N) ");
        String cancelled = s.nextLine();
        if (cancelled.equalsIgnoreCase("Y")) {
            selectedAppointment.setStatus("cancelled");
            return;
        }

        System.out.print("Please provide the type of service in the appointment: ");
        String serviceProv = s.nextLine();
        System.out.print("Please provide the medication you have prescribed for the patient: ");
        String medProv = s.nextLine();
        System.out.print("Please type in any notes for the patient: ");
        String notes = s.nextLine();

        selectedAppointment.setService(serviceProv);
        selectedAppointment.setMedication(medProv);
        selectedAppointment.setMedicationStatus("pending");
        selectedAppointment.setNotes(notes);
        selectedAppointment.setStatus("completed");

        // Optionally, update the appointment in the CSV or data storage
        updateAppointmentInCSV(selectedAppointment);
    }

}
