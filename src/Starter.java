import javax.swing.*;
import java.awt.*;

public class Starter {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Starter::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Snack Automat");
        frame.setSize(1280, 538);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        ImageIcon icon = new ImageIcon("C:\\Users\\zinob\\Documents\\Snack_Automat\\assets\\automat.png");
        Image image = icon.getImage();

        Image scaledImage = image.getScaledInstance(312, 538, Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));

        frame.add(backgroundLabel);

        frame.setVisible(true);
    }
}
