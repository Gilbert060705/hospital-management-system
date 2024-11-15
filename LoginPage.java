import java.util.*;

public class LoginPage {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.println("Welcome to Hospital Management System. Please login to the system!");
        boolean first = false;
        System.out.print("Are you a new user? (Y/N) ");
        char ans = s.next().charAt(0);
        if (ans == 'y' || ans == 'Y') {
            first = true;
        }

        if (first) {
            System.out.print("Please enter your full name : ");
            s.nextLine();
            String name = s.nextLine();

            System.out.print("When is your birth date : (DD/MM/YYYY) ");
            String DateofBirth = s.nextLine();

            System.out.print("What is your gender? (1 for Male, 2 for Female) ");
            int choice = s.nextInt();
            s.nextLine();

            String gender = "";
            if (choice == 1) {
                gender = "Male";
            } else {
                gender = "Female";
            }
            System.out.print("What is your phone number : ");
            String phoneNum = s.nextLine();
            System.out.print("What is your email address : ");
            String email = s.nextLine();
            System.out.print("What is your blood type : ");
            String type = s.nextLine();
            System.out.println("To ease you to log in to the system, please make a password for your account.");
            String password = s.nextLine();

            Patient newPatient = new Patient(name, DateofBirth, gender, phoneNum, email, type, "", "", password);
            newPatient.saveNewPatient();
            System.out.println("Your HospitalID is : " + (Patient.getLastID() - 1));
        }

        String role = "";
        String id = "";
        int loginValid = 0;
        while (loginValid == 0) {
            System.out.print("Enter HospitalID : ");
            s.nextLine();
            id = s.nextLine();
            System.out.print("Enter your password ");
            String password = s.nextLine();

            String filename = "";
            if (id.charAt(0) == '1') {
                filename = "patients.csv";
                role = "Patient";
            } else if (id.charAt(0) == '2') {
                filename = "doctors.csv";
                role = "Doctor";
            } else if (id.charAt(0) == '3') {
                filename = "pharmacist.csv";
                role = "Pharmacist";
            } else if (id.charAt(0) == '4') {
                filename = "admin.csv";
                role = "Administrator";
            }

            ValidateLogin check = new ValidateLogin(filename, id, password);
            loginValid = check.loginValid(id, password);
            if (loginValid == 0) {
                System.out.println("You entered a wrong username or password. Please try again. ");
            }
        }

        boolean login = true;
        if (role == "Doctor") {
            Doctor logIn = new Doctor(id);
            System.out.println("Welcome Back," + logIn.getName());
            int option = -1;
            while (login) {
                System.out.println("1. Review Patient Medical Records");
                System.out.println("2. Update Patient Medical records");
                System.out.println("3. View List of Appointments");
                System.out.println("4. Set Availability for Appointments");
                System.out.println("5. Accept or Decline Appointment Request");
                System.out.println("6. View Upcoming Appointments");
                System.out.println("7. Add Appointment Outcome Record");
                System.out.println("8. Log Out");

                option = s.nextInt();
                if (option == 1) {
                    logIn.displayListOfPatients();
                } else if (option == 2) {
                    logIn.updatePatientDetails();
                } else if (option == 3) {
                    logIn.displayAvailableSlots();
                } else if (option == 4) {
                    logIn.addAvailability();
                } else if (option == 5) {
                    logIn.reviewPendingAppointments();
                } else if (option == 6) {
                    logIn.viewAcceptedAppointments();
                } else if (option == 7) {
                    logIn.updateRecords();
                } else if (option == 8) {
                    login = false;
                }

            }
        }
    }
}
