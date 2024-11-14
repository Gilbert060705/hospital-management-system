import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Scanner;

public class ValidateLogin {
    private String filename;
    private String id;
    private String password;
    public static final String SEPARATOR = ",";

    public ValidateLogin(String filename, String id, String password) {
        this.filename = filename;
        this.id = id;
        this.password = password;
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

    public int loginValid(String id, String password) {
        ArrayList<String> stringArray = (ArrayList<String>) read(filename);
        for (String st : stringArray) {
            StringTokenizer star = new StringTokenizer(st, SEPARATOR);
            String checkid = star.nextToken().trim();
            if (checkid != id) {
                continue;
            } else {
                String correctPass = star.nextToken().trim();
                if (correctPass == password) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        return 2;
    }
}
