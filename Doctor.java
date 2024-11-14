import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Doctor {
    private String DoctorID;
    private String name;
    private String password;
    private ArrayList<Patient> patientsList;
    private String AppointmentsCSV;
    private String fileName = "doctors.csv";
    public static final String SEPARATOR = ",";

    public Doctor(String DoctorID) {
        this.DoctorID = DoctorID;
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

    public Object retrieveDetails(int choice) {
        ArrayList<String> stringArray = (ArrayList<String>) read(fileName);
        for (String st : stringArray) {
            StringTokenizer star = new StringTokenizer(st, SEPARATOR);
            String checkid = star.nextToken().trim();
            if (checkid == String.valueOf(this.DoctorID)) {
                System.out.println(checkid + String.valueOf(this.DoctorID));
                continue;
            }
            String name = star.nextToken().trim();
            String password = star.nextToken().trim();
            String patients = star.nextToken().trim();
            if (patients != "") {
                String[] listOfPatients = patients.split(";");
                for (String patientIDs : listOfPatients) {
                    Patient temp = new Patient(patientIDs.trim());
                    patientsList.add(temp);
                }
            }
            String CSVApp = star.nextToken().trim();

            this.name = name;
            this.password = password;
            this.AppointmentsCSV = CSVApp;
        }
        return null; // return null if no details are found
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
                System.out.print(i + 1);
                patientsList.get(i).displayBrief();
                if (i == patientsList.size() - 1) {
                    System.out.println(i + 2 + ". Back");
                    quitOpt = i + 2;
                }
            }

            choose = s.nextInt();
            if (choose < 0 && choose > quitOpt) {
                continue;
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
}
