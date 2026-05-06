import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Starter {
    private static final String DEFAULT_DISPLAY = "SELECT";
    private static final String RESTOCK_CODE = "1234";
    private static final int WINDOW_WIDTH = 1060;
    private static final int WINDOW_HEIGHT = 620;
    private static final SnackInventory inventory = new SnackInventory();
    private static final CustomerInventory customerInventory = new CustomerInventory();
    private static final Payment payment = new Payment();

    private static Product selectedProduct;
    private static JTextArea snackMenuArea;
    private static JLabel statusLabel;
    private static JLabel balanceLabel;
    private static DefaultListModel<String> customerInventoryModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Starter::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Snack Automat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("assets/automat.png");
        Image image = icon.getImage();

        Image scaledImage = image.getScaledInstance(220, 520, Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
        backgroundLabel.setPreferredSize(new Dimension(220, 520));

        frame.add(backgroundLabel, BorderLayout.WEST);
        frame.add(createKeypadPanel(inventory), BorderLayout.CENTER);
        frame.add(createPaymentPanel(), BorderLayout.EAST);

        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setMinimumSize(new Dimension(960, 560));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createKeypadPanel(SnackInventory inventory) {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.setBackground(new Color(224, 229, 233));

        JTextField display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.CENTER);
        display.setFont(new Font("Monospaced", Font.BOLD, 22));
        display.setPreferredSize(new Dimension(220, 48));
        display.setText(DEFAULT_DISPLAY);

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
        addEnterButton(keypad, display, inventory);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(keypad, BorderLayout.CENTER);
        panel.add(createSnackMenuPanel(), BorderLayout.EAST);
        return panel;
    }

    private static JScrollPane createSnackMenuPanel() {
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

    private static JPanel createPaymentPanel() {
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

    private static JScrollPane createCustomerInventoryPanel() {
        customerInventoryModel = new DefaultListModel<>();
        JList<String> inventoryList = new JList<>(customerInventoryModel);
        inventoryList.setVisibleRowCount(3);
        inventoryList.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(inventoryList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Inventory"));
        scrollPane.setPreferredSize(new Dimension(0, 78));
        return scrollPane;
    }

    private static JLabel createImageLabel(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    private static void makeDraggable(JLabel label, JComponent container, JComponent tapTarget) {
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

    private static void addKeypadButton(JPanel keypad, String value, JTextField display) {
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

    private static void addClearButton(JPanel keypad, JTextField display) {
        JButton button = createButton("CLR");
        button.addActionListener(e -> {
            selectedProduct = null;
            display.setText(DEFAULT_DISPLAY);
            setStatus("Choose a snack code, then pay.");
        });
        keypad.add(button);
    }

    private static void addEnterButton(JPanel keypad, JTextField display, SnackInventory inventory) {
        JButton button = createButton("OK");

        button.addActionListener(e -> {
            String enteredCode = display.getText();

            if (!DEFAULT_DISPLAY.equals(enteredCode)) {
                if (RESTOCK_CODE.equals(enteredCode)) {
                    openAdminFrame(inventory, display);
                } else {
                    try {
                        int productId = Integer.parseInt(enteredCode);
                        selectedProduct = inventory.getSnackById(productId);

                        if (selectedProduct != null) {
                            if (selectedProduct.isInStock()) {
                                display.setText(String.format("%s $%.2f", selectedProduct.getName(), selectedProduct.getPrice()));
                                setStatus("Drag card to pay.");
                            } else {
                                display.setText("OUT OF STOCK");
                                setStatus(selectedProduct.getName() + " is out of stock.");
                                selectedProduct = null;
                            }
                        } else {
                            selectedProduct = null;
                            display.setText("NOT FOUND");
                            setStatus("No snack found for code " + productId + ".");
                        }
                    } catch (NumberFormatException ex) {
                        selectedProduct = null;
                        display.setText("INVALID CODE");
                        setStatus("Use a numeric snack code from the inventory list.");
                    }
                }
            }
        });

        keypad.add(button);
    }

    private static void openAdminFrame(SnackInventory inventory, JTextField display) {
        JFrame adminFrame = new JFrame("Admin Menu");
        adminFrame.setSize(420, 360);
        adminFrame.setLocationRelativeTo(null);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(224, 229, 233));

        JLabel titleLabel = new JLabel("Choose Admin Option", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        optionsPanel.setOpaque(false);

        JButton restockButton = createButton("Fill all snacks to 15");
        restockButton.addActionListener(e -> {
            inventory.restockAll();
            refreshSnackMenu();
            display.setText("RESTOCKED");
            setStatus("Machine inventory restocked.");
        });

        JButton showStockButton = createButton("Show current stock");
        showStockButton.addActionListener(e -> JOptionPane.showMessageDialog(
            adminFrame,
            buildStockOverview(inventory),
            "Current Stock",
            JOptionPane.INFORMATION_MESSAGE
        ));

        JButton resetDisplayButton = createButton("Reset keypad display");
        resetDisplayButton.addActionListener(e -> {
            selectedProduct = null;
            display.setText(DEFAULT_DISPLAY);
            setStatus("Choose a snack code, then pay.");
        });

        JButton changePriceButton = createButton("Change price by ID");
        changePriceButton.addActionListener(e -> changePriceById(inventory, display, adminFrame));

        optionsPanel.add(restockButton);
        optionsPanel.add(showStockButton);
        optionsPanel.add(resetDisplayButton);
        optionsPanel.add(changePriceButton);

        JButton closeButton = createButton("Close");
        closeButton.addActionListener(e -> adminFrame.dispose());

        JPanel actionPanel = new JPanel(new GridLayout(1, 1));
        actionPanel.setOpaque(false);
        actionPanel.add(closeButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(optionsPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        adminFrame.add(panel);
        adminFrame.setVisible(true);
    }

    private static void changePriceById(SnackInventory inventory, JTextField display, JFrame adminFrame) {
        String idInput = JOptionPane.showInputDialog(adminFrame, "Enter product ID:");
        if (idInput == null) {
            return;
        }

        try {
            int productId = Integer.parseInt(idInput.trim());
            Product product = inventory.getSnackById(productId);

            if (product == null) {
                JOptionPane.showMessageDialog(adminFrame, "Product ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String priceInput = JOptionPane.showInputDialog(
                adminFrame,
                "Enter new price for " + product.getName() + ":",
                String.format("%.2f", product.getPrice())
            );
            if (priceInput == null) {
                return;
            }

            double newPrice = Double.parseDouble(priceInput.trim());
            product.setPrice(newPrice);
            display.setText(product.getId() + " $" + String.format("%.2f", product.getPrice()));
            refreshSnackMenu();

            JOptionPane.showMessageDialog(
                adminFrame,
                product.getName() + " price updated to $" + String.format("%.2f", product.getPrice()),
                "Price Updated",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(adminFrame, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(adminFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void purchaseSelectedProduct() {
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

    private static void refreshSnackMenu() {
        if (snackMenuArea == null) {
            return;
        }

        StringBuilder menu = new StringBuilder();
        for (Product snack : inventory.getAllSnacks()) {
            menu.append(String.format("%03d  %-14s $%.2f  x%d%n", snack.getId(), snack.getName(), snack.getPrice(), snack.getStock()));
        }
        snackMenuArea.setText(menu.toString());
    }

    private static void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    private static String buildStockOverview(SnackInventory inventory) {
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

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBackground(new Color(53, 66, 89));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(70, 48));
        return button;
    }
}
