import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Starter {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Starter::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Snack Automat");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
