
/* Alexandria University CC272
   Faculty of Engineering Programming II
   Instructors: Dr. Layla Abou-Hadeed, Eng. Ahmed ElSayed, Eng. Ahmed Ashraf,
   Eng. Miar Mamdouh, Eng. AbdElaziz Mohamed, Eng. Mazen Sallam, Eng. Shams Zayan,
   Eng. Abdelrahman Wael, Eng. Mohamed Zaytoon, Eng. Muhannad Bashar */

import javax.swing.SwingUtilities;
import backend.JsonDatabaseManager;
import controller.AuthService;
import frontend.LoginFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JsonDatabaseManager databaseManager = new JsonDatabaseManager("users.json", "courses.json");
            AuthService authService = new AuthService(databaseManager);
            LoginFrame loginFrame = new LoginFrame(authService, databaseManager);
            loginFrame.setVisible(true);
        });
    }
}
