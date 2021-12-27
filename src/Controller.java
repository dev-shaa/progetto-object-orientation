import javax.swing.*;
import com.formdev.flatlaf.*;

public class Controller {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Impossibile inizializzare FlatLightLaf");
        }

        MainWindow mainWindow = new MainWindow(new User("Utente Placeholder"));
        mainWindow.setVisible(true);
    }
}
