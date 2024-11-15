import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Scanner;
import java.time.LocalTime;

public class FreeAppointment {
    private Doctor doctor;
    private String start;
    private String finish;
    private String date;
    private String filename;
    private boolean writeToCSV;

    public FreeAppointment(Doctor doctor, String start, String finish, String date, String filename, boolean write) {
        this.doctor = doctor;
        this.start = start;
        this.finish = finish;
        this.date = date;
        this.writeToCSV = write;
        int ID = Integer.parseInt(doctor.getID());
        String idS = Integer.toString(ID % 2000);
        this.filename = "doctor" + idS + "Free.csv";

        if (this.writeToCSV) {
            addToCSV();
        }
    }

    private void addToCSV() {
        try (FileWriter fw = new FileWriter(this.filename, true)) {
            StringBuilder sb = new StringBuilder();
            sb.append(doctor.getID()).append(",");
            sb.append(start).append(",");
            sb.append(finish).append(",");
            sb.append(date).append("\n");

            fw.write(sb.toString());
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }

    public ArrayList<FreeAppointment> bookFreeAppointment(String bookStart, String bookFinish, String date) {
        ArrayList<FreeAppointment> make = new ArrayList<FreeAppointment>();
        LocalTime startBook = LocalTime.parse(bookStart);
        LocalTime finishBook = LocalTime.parse(bookFinish);
        LocalTime startFree = LocalTime.parse(this.start);
        LocalTime finishFree = LocalTime.parse(this.finish);
        if (!date.equals(this.date)) {
            return make;
        } else {
            if (startBook.isBefore(startFree) || finishBook.isAfter(finishFree) || startBook.isAfter(finishBook)) {
                return make;
            }

            if (startBook.equals(startFree) && finishFree.isAfter(finishBook)) {
                FreeAppointment partition = new FreeAppointment(this.doctor, finishBook.toString(), this.finish,
                        this.date, this.filename, true);
                make.add(partition);
            } else if (startBook.isAfter(startFree) && finishFree.isAfter(finishBook)) {
                FreeAppointment partition1 = new FreeAppointment(this.doctor, this.start, startBook.toString(),
                        this.date, this.filename, true);
                make.add(partition1);
                FreeAppointment partition2 = new FreeAppointment(this.doctor, finishBook.toString(), this.finish,
                        this.date, this.filename, true);
                make.add(partition2);
            } else if (startBook.isAfter(startFree) && finishBook.equals(finishFree)) {
                FreeAppointment partition = new FreeAppointment(this.doctor, this.start, startBook.toString(),
                        this.date, this.filename, true);
                make.add(partition);
            }
        }

        return make;
    }

    public void displayFree() {
        System.out.println("Doctor : " + this.doctor.getName());
        System.out.println("Date  : " + this.date);
        System.out.print("Free from " + this.start);
        System.out.println(" to " + this.finish);
    }
}
