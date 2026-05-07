package snackautomat.ui;

import snackautomat.app.SnackAutomatApp;
import snackautomat.model.Product;
import snackautomat.service.SnackInventory;

import javax.swing.*;
import java.awt.*;

public class AdminFrame {
    private static final String RESTOCK_CODE = "1234";

    private final SnackInventory inventory;
    private final SnackAutomatApp app;
    private final JTextField display;

    public AdminFrame(SnackInventory inventory, SnackAutomatApp app, JTextField display) {
        this.inventory = inventory;
        this.app = app;
        this.display = display;
    }

    public static boolean isAdminCode(String enteredCode) {
        return RESTOCK_CODE.equals(enteredCode);
    }

    public void open() {
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
            app.refreshSnackMenu();
            display.setText("RESTOCKED");
            app.setStatus("Machine inventory restocked.");
        });

        JButton showStockButton = createButton("Show current stock");
        showStockButton.addActionListener(e -> JOptionPane.showMessageDialog(
            adminFrame,
            app.buildStockOverview(),
            "Current Stock",
            JOptionPane.INFORMATION_MESSAGE
        ));

        JButton resetDisplayButton = createButton("Reset keypad display");
        resetDisplayButton.addActionListener(e -> app.resetSelection(display));

        JButton changePriceButton = createButton("Change price by ID");
        changePriceButton.addActionListener(e -> changePriceById(adminFrame));

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

    private void changePriceById(JFrame adminFrame) {
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
            app.refreshSnackMenu();

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

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBackground(new Color(53, 66, 89));
        button.setForeground(Color.WHITE);
        return button;
    }
}
