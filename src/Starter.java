import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Starter {
    private static final String DEFAULT_DISPLAY = "SELECT";
    private static final String SECRET_RESTOCK_CODE = "1234";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Starter::createAndShowGui);
    }

    private static void createAndShowGui() {
        SnackInventory inventory = new SnackInventory();
        JFrame frame = new JFrame("Snack Automat");
        frame.setSize(1280, 538);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("assets/automat.png");
        Image image = icon.getImage();

        Image scaledImage = image.getScaledInstance(312, 538, Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));

        frame.add(backgroundLabel, BorderLayout.WEST);
        frame.add(createKeypadPanel(inventory), BorderLayout.CENTER);
        frame.add(createPaymentPanel(), BorderLayout.EAST);

        frame.setVisible(true);
    }

    private static JPanel createKeypadPanel(SnackInventory inventory) {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(224, 229, 233));

        JTextField display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.CENTER);
        display.setFont(new Font("Monospaced", Font.BOLD, 28));
        display.setPreferredSize(new Dimension(260, 60));
        display.setText(DEFAULT_DISPLAY);

        JLabel promptLabel = new JLabel("Enter item code", SwingConstants.CENTER);
        promptLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);
        topPanel.add(promptLabel, BorderLayout.NORTH);
        topPanel.add(display, BorderLayout.CENTER);

        JPanel keypad = new JPanel(new GridLayout(5, 3, 12, 12));
        keypad.setOpaque(false);

        addKeypadButton(keypad, "A", display);
        addKeypadButton(keypad, "B", display);
        addKeypadButton(keypad, "C", display);
        addKeypadButton(keypad, "1", display);
        addKeypadButton(keypad, "2", display);
        addKeypadButton(keypad, "3", display);
        addKeypadButton(keypad, "4", display);
        addKeypadButton(keypad, "5", display);
        addKeypadButton(keypad, "6", display);
        addKeypadButton(keypad, "7", display);
        addKeypadButton(keypad, "8", display);
        addKeypadButton(keypad, "9", display);
        addClearButton(keypad, display);
        addKeypadButton(keypad, "0", display);
        addEnterButton(keypad, display, inventory);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(keypad, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setPreferredSize(new Dimension(380, 538));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 30));
        panel.setBackground(new Color(210, 216, 222));

        JLabel titleLabel = new JLabel("Card Payment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel infoLabel = new JLabel("Drag the card anywhere in this side panel", SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JLayeredPane paymentArea = new JLayeredPane();
        paymentArea.setOpaque(true);
        paymentArea.setBackground(new Color(239, 243, 246));
        paymentArea.setBorder(BorderFactory.createLineBorder(new Color(117, 129, 145), 2));
        paymentArea.setPreferredSize(new Dimension(330, 320));

        JLabel tapTextLabel = new JLabel("Tap To Pay", SwingConstants.CENTER);
        tapTextLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        tapTextLabel.setBounds(15, 25, 130, 30);

        JLabel tapLabel = createImageLabel("assets/TapPay.jpg", 120, 80);
        tapLabel.setBounds(20, 70, 120, 80);

        JLabel cardLabel = createImageLabel("assets/card.png", 180, 110);
        cardLabel.setBounds(135, 160, 180, 110);
        makeDraggable(cardLabel, paymentArea);

        paymentArea.add(tapTextLabel, Integer.valueOf(1));
        paymentArea.add(tapLabel, Integer.valueOf(1));
        paymentArea.add(cardLabel, Integer.valueOf(2));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(paymentArea, BorderLayout.CENTER);
        panel.add(infoLabel, BorderLayout.SOUTH);
        return panel;
    }

    private static JLabel createImageLabel(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    private static void makeDraggable(JLabel label, JComponent container) {
        MouseAdapter dragHandler = new MouseAdapter() {
            private Point dragOffset;

            @Override
            public void mousePressed(MouseEvent e) {
                Point pressedPoint = SwingUtilities.convertPoint(label, e.getPoint(), container);
                dragOffset = new Point(pressedPoint.x - label.getX(), pressedPoint.y - label.getY());
                label.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point draggedPoint = SwingUtilities.convertPoint(label, e.getPoint(), container);
                int newX = draggedPoint.x - dragOffset.x;
                int newY = draggedPoint.y - dragOffset.y;

                int maxX = Math.max(0, container.getWidth() - label.getWidth());
                int maxY = Math.max(0, container.getHeight() - label.getHeight());

                newX = Math.max(0, Math.min(newX, maxX));
                newY = Math.max(0, Math.min(newY, maxY));

                label.setLocation(newX, newY);
                container.repaint();
            }
        };

        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(dragHandler);
        label.addMouseMotionListener(dragHandler);
    }

    private static void addKeypadButton(JPanel keypad, String value, JTextField display) {
        JButton button = createButton(value);
        button.addActionListener(e -> {
            if (DEFAULT_DISPLAY.equals(display.getText())) {
                display.setText(value);
            } else {
                display.setText(display.getText() + value);
            }
        });
        keypad.add(button);
    }

    private static void addClearButton(JPanel keypad, JTextField display) {
        JButton button = createButton("CLR");
        button.addActionListener(e -> display.setText(DEFAULT_DISPLAY));
        keypad.add(button);
    }

   private static void addEnterButton(JPanel keypad, JTextField display, SnackInventory inventory) {
        JButton button = createButton("OK");
        
        button.addActionListener(e -> {
            String enteredCode = display.getText();
            
            if (!DEFAULT_DISPLAY.equals(enteredCode)) {
                if (SECRET_RESTOCK_CODE.equals(enteredCode)) {
                    inventory.restockAll();
                    display.setText("RESTOCKED");
                } else {
                    try {
                        int productId = Integer.parseInt(enteredCode);
                        Product selectedProduct = inventory.getSnackById(productId);
                        
                        if (selectedProduct != null) {
                            display.setText(selectedProduct.getName() + " $" + selectedProduct.getPrice());
                        } else {
                            display.setText("NOT FOUND");
                        }
                    } catch (NumberFormatException ex) {
                        display.setText("INVALID CODE");
                    }
                }
            }
        });
        
        keypad.add(button);
    }

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 22));
        button.setFocusPainted(false);
        button.setBackground(new Color(53, 66, 89));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(80, 65));
        return button;
    }
}
