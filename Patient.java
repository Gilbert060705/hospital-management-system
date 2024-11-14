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

    public static final String SEPARATOR = ",";

    public Patient(String PatientID) {
        this.PatientID = PatientID;
        this.fileName = "patients.csv";
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

    public Object retrieveDetails(int choice) {
        ArrayList<String> stringArray = (ArrayList<String>) read(fileName);
        for (String st : stringArray) {
            StringTokenizer star = new StringTokenizer(st, SEPARATOR);
            String checkid = star.nextToken().trim();
            if (checkid == String.valueOf(this.PatientID)) {
                System.out.println(checkid + String.valueOf(this.PatientID));
                continue;
            }
            String name = star.nextToken().trim();
            String password = star.nextToken().trim();
            String dateOfBirth = star.nextToken().trim();
            String gender = star.nextToken().trim();
            String phone = star.nextToken().trim();
            String email = star.nextToken().trim();
            String bloodType = star.nextToken().trim();
            String pastDiagnoses = star.nextToken().trim();
            String treatments = star.nextToken().trim();

            if (choice == 1) {
                return name;
            } else if (choice == 2) {
                return dateOfBirth;
            } else if (choice == 3) {
                return gender;
            } else if (choice == 4) {
                return phone;
            } else if (choice == 5) {
                return email;
            } else if (choice == 6) {
                return bloodType;
            } else if (choice == 7) {
                return pastDiagnoses;
            } else if (choice == 8) {
                return treatments;
            } else if (choice == 9) {
                return password;
            }
        }
        return null; // return null if no details are found
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

    public void completeDetails() {
        this.name = (String) retrieveDetails(1);
        this.password = (String) retrieveDetails(9);
        this.DateOfBirth = (String) retrieveDetails(2);
        this.gender = (String) retrieveDetails(3);
        this.phoneNum = (String) retrieveDetails(4);
        this.emailAdress = (String) retrieveDetails(5);
        this.BloodType = (String) retrieveDetails(6);
        this.pastDiagnoses = (String) retrieveDetails(7);
        this.treatments = (String) retrieveDetails(8);

        System.out.println(this.name + this.DateOfBirth + this.BloodType + this.gender + this.phoneNum
                + this.emailAdress + this.pastDiagnoses + this.treatments);
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
        String[] listOfDiagnoses = this.pastDiagnoses.split(" ");
        for (String x : listOfDiagnoses) {
            System.out.println(start + ". " + x);
            start++;
        }

        System.out.println("List of Treatments Done : ");
        start = 1;
        String[] listOfTreatments = this.treatments.split(" ");
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
                        treatments += ":" + append;
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

}
