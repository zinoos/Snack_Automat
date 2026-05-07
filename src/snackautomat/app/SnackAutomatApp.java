package snackautomat.app;

import snackautomat.model.PaymentResult;
import snackautomat.model.Product;
import snackautomat.service.CustomerInventory;
import snackautomat.service.Payment;
import snackautomat.service.SnackInventory;
import snackautomat.ui.AdminFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SnackAutomatApp {
    private static final String DEFAULT_DISPLAY = "SELECT";
    private static final int WINDOW_WIDTH = 1060;
    private static final int WINDOW_HEIGHT = 620;

    private final SnackInventory inventory = new SnackInventory();
    private final CustomerInventory customerInventory = new CustomerInventory();
    private final Payment payment = new Payment();

    private Product selectedProduct;
    private JTextArea snackMenuArea;
    private JLabel statusLabel;
    private JLabel balanceLabel;
    private DefaultListModel<String> customerInventoryModel;

    public void start() {
        SwingUtilities.invokeLater(this::createAndShowGui);
    }

    private void createAndShowGui() {
        JFrame frame = new JFrame("Snack Automat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel backgroundLabel = createImageLabel("assets/automat.png", 220, 520);
        backgroundLabel.setPreferredSize(new Dimension(220, 520));

        JTextField display = createDisplayField();

        frame.add(backgroundLabel, BorderLayout.WEST);
        frame.add(createKeypadPanel(display), BorderLayout.CENTER);
        frame.add(createPaymentPanel(), BorderLayout.EAST);

        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setMinimumSize(new Dimension(960, 560));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JTextField createDisplayField() {
        JTextField display = new JTextField(DEFAULT_DISPLAY);
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.CENTER);
        display.setFont(new Font("Monospaced", Font.BOLD, 22));
        display.setPreferredSize(new Dimension(220, 48));
        return display;
    }

    private JPanel createKeypadPanel(JTextField display) {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.setBackground(new Color(224, 229, 233));

        JLabel promptLabel = new JLabel("Enter item code", SwingConstants.CENTER);
        promptLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        statusLabel = new JLabel("Choose a snack code, then pay.", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel topPanel = new JPanel(new BorderLayout(0, 6));
        topPanel.setOpaque(false);
        topPanel.add(promptLabel, BorderLayout.NORTH);
        topPanel.add(display, BorderLayout.CENTER);
        topPanel.add(statusLabel, BorderLayout.SOUTH);

        JPanel keypad = new JPanel(new GridLayout(4, 3, 8, 8));
        keypad.setOpaque(false);

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
        addEnterButton(keypad, display);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(keypad, BorderLayout.CENTER);
        panel.add(createSnackMenuPanel(), BorderLayout.EAST);
        return panel;
    }

    private JScrollPane createSnackMenuPanel() {
        snackMenuArea = new JTextArea(10, 20);
        snackMenuArea.setEditable(false);
        snackMenuArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        snackMenuArea.setBackground(new Color(247, 249, 250));
        snackMenuArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        refreshSnackMenu();

        JScrollPane scrollPane = new JScrollPane(snackMenuArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Machine Inventory"));
        scrollPane.setPreferredSize(new Dimension(230, 0));
        return scrollPane;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setPreferredSize(new Dimension(320, 520));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 14, 16, 16));
        panel.setBackground(new Color(210, 216, 222));

        JLabel titleLabel = new JLabel("Card Payment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 19));

        JLabel infoLabel = new JLabel("Drag card to Tap To Pay", SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        balanceLabel = new JLabel("Ready for card payment", SwingConstants.CENTER);
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLayeredPane paymentArea = new JLayeredPane();
        paymentArea.setOpaque(true);
        paymentArea.setBackground(new Color(239, 243, 246));
        paymentArea.setBorder(BorderFactory.createLineBorder(new Color(117, 129, 145), 2));
        paymentArea.setPreferredSize(new Dimension(290, 220));

        JLabel tapTextLabel = new JLabel("Tap To Pay", SwingConstants.CENTER);
        tapTextLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        tapTextLabel.setBounds(12, 20, 110, 26);

        JLabel tapLabel = createImageLabel("assets/TapPay.jpg", 100, 65);
        tapLabel.setBounds(17, 58, 100, 65);

        JLabel cardLabel = createImageLabel("assets/card.png", 150, 92);
        cardLabel.setBounds(125, 118, 150, 92);
        makeDraggable(cardLabel, paymentArea, tapLabel);

        paymentArea.add(tapTextLabel, Integer.valueOf(1));
        paymentArea.add(tapLabel, Integer.valueOf(1));
        paymentArea.add(cardLabel, Integer.valueOf(2));

        JPanel actionsPanel = new JPanel(new BorderLayout(0, 6));
        actionsPanel.setOpaque(false);
        actionsPanel.add(infoLabel, BorderLayout.NORTH);
        actionsPanel.add(balanceLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 6));
        bottomPanel.setOpaque(false);
        bottomPanel.add(createCustomerInventoryPanel(), BorderLayout.CENTER);
        actionsPanel.add(bottomPanel, BorderLayout.SOUTH);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(paymentArea, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JScrollPane createCustomerInventoryPanel() {
        customerInventoryModel = new DefaultListModel<>();
        JList<String> inventoryList = new JList<>(customerInventoryModel);
        inventoryList.setVisibleRowCount(3);
        inventoryList.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(inventoryList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Inventory"));
        scrollPane.setPreferredSize(new Dimension(0, 78));
        return scrollPane;
    }

    private JLabel createImageLabel(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    private void makeDraggable(JLabel label, JComponent container, JComponent tapTarget) {
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
                if (label.getBounds().intersects(tapTarget.getBounds())) {
                    purchaseSelectedProduct();
                }
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

    private void addKeypadButton(JPanel keypad, String value, JTextField display) {
        JButton button = createButton(value);
        button.addActionListener(e -> {
            String currentText = display.getText();
            if (DEFAULT_DISPLAY.equals(currentText) || selectedProduct != null || !currentText.matches("[0-9]+")) {
                selectedProduct = null;
                display.setText(value);
            } else {
                display.setText(currentText + value);
            }
        });
        keypad.add(button);
    }

    private void addClearButton(JPanel keypad, JTextField display) {
        JButton button = createButton("CLR");
        button.addActionListener(e -> resetSelection(display));
        keypad.add(button);
    }

    private void addEnterButton(JPanel keypad, JTextField display) {
        JButton button = createButton("OK");
        button.addActionListener(e -> handleEnteredCode(display));
        keypad.add(button);
    }

    private void handleEnteredCode(JTextField display) {
        String enteredCode = display.getText();
        if (DEFAULT_DISPLAY.equals(enteredCode)) {
            return;
        }

        if (AdminFrame.isAdminCode(enteredCode)) {
            new AdminFrame(inventory, this, display).open();
            return;
        }

        try {
            int productId = Integer.parseInt(enteredCode);
            selectedProduct = inventory.getSnackById(productId);

            if (selectedProduct == null) {
                display.setText("NOT FOUND");
                setStatus("No snack found for code " + productId + ".");
                return;
            }

            if (!selectedProduct.isInStock()) {
                display.setText("OUT OF STOCK");
                setStatus(selectedProduct.getName() + " is out of stock.");
                selectedProduct = null;
                return;
            }

            display.setText(String.format("%s $%.2f", selectedProduct.getName(), selectedProduct.getPrice()));
            setStatus("Drag card to pay.");
        } catch (NumberFormatException ex) {
            selectedProduct = null;
            display.setText("INVALID CODE");
            setStatus("Use a numeric snack code from the inventory list.");
        }
    }

    public void resetSelection(JTextField display) {
        selectedProduct = null;
        display.setText(DEFAULT_DISPLAY);
        setStatus("Choose a snack code, then pay.");
    }

    public void refreshSnackMenu() {
        if (snackMenuArea == null) {
            return;
        }

        StringBuilder menu = new StringBuilder();
        for (Product snack : inventory.getAllSnacks()) {
            menu.append(String.format("%03d  %-14s $%.2f  x%d%n",
                snack.getId(),
                snack.getName(),
                snack.getPrice(),
                snack.getStock()));
        }
        snackMenuArea.setText(menu.toString());
    }

    public void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    public String buildStockOverview() {
        StringBuilder builder = new StringBuilder();
        for (Product snack : inventory.getAllSnacks()) {
            builder.append(snack.getId())
                .append(" - ")
                .append(snack.getName())
                .append(": ")
                .append(snack.getStock())
                .append(System.lineSeparator());
        }
        return builder.toString();
    }

    private void purchaseSelectedProduct() {
        if (selectedProduct == null) {
            setStatus("Enter a snack code and press OK before paying.");
            return;
        }

        payment.reset();
        payment.addFunds(selectedProduct.getPrice());
        PaymentResult result = payment.purchase(selectedProduct);

        if (result.isSuccessful()) {
            customerInventory.add(selectedProduct);
            customerInventoryModel.addElement(selectedProduct.getName());
            balanceLabel.setText(String.format("Paid $%.2f", selectedProduct.getPrice()));
            refreshSnackMenu();
        }

        setStatus(result.getMessage());
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBackground(new Color(53, 66, 89));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(70, 48));
        return button;
    }
}
