import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Scanner;

public class Appointment {
    private Patient patient;
    private Doctor doctor;
    private String date;
    private String startClock;
    private String finishClock;
    private String status;
    private String service;
    private String medication;
    private String medicationStatus;
    private String notes;
    private boolean write;
    private String filename = "Appointments.csv";

    public Appointment(Patient patient, Doctor doctor, String date, String startClock, String finishClock,
            boolean write) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.startClock = startClock;
        this.finishClock = finishClock;
        this.status = "pending";
        this.write = write;
        this.medication = "";
        this.medicationStatus = "";
        this.notes = "";

        if (write) {
            addToCSV();
        }
    }

    private void addToCSV() {
        File file = new File(filename);
        boolean fileExists = file.exists();

        try (FileWriter fw = new FileWriter(filename, true)) {
            // Append the appointment details
            StringBuilder sb = new StringBuilder();
            sb.append(doctor.getID()).append(",");
            sb.append(patient.getID()).append(",");
            sb.append(date).append(",");
            sb.append(startClock).append(",");
            sb.append(finishClock).append(",");
            sb.append(status).append(",");
            sb.append(service).append(",");
            sb.append(medication).append(",");
            sb.append(medicationStatus).append(",");
            sb.append(notes).append("\n");

            fw.write(sb.toString());
            fw.flush();
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public String getStartClock() {
        return startClock;
    }

    public String getFinishClock() {
        return finishClock;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getMedicationStatus() {
        return medicationStatus;
    }

    public void setMedicationStatus(String medicationStatus) {
        this.medicationStatus = medicationStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDetailsForDoctor() {
        return "Appointment with Patient ID: " + patient.getID() +
                " on " + date + " from " + startClock + " to " + finishClock;
    }

    public void getDetailsForPatient() {
        Doctor detailDoctor = this.doctor;
        System.out.println("Name of doctor : " + detailDoctor.getName());
        System.out.println("Date : " + this.date);
        System.out.println("From " + startClock + " to " + finishClock);
        System.out.println("Appointment Status : " + this.status);
    }

}
