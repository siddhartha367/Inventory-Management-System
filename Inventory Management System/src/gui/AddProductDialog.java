package gui;

import dao.ProductDAO;
import model.Product;

import javax.swing.*;
import java.awt.*;

public class AddProductDialog extends JDialog {
    private JTextField txtProductId, txtName, txtQuantity, txtPrice, txtSupplier, txtReorder;
    private ProductDAO productDAO;

    public AddProductDialog(JFrame parent, ProductDAO productDAO) {
        super(parent, "Add New Product", true);
        this.productDAO = productDAO;

        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and fields
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1;
        txtProductId = new JTextField(15);
        add(txtProductId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(15);
        add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        txtQuantity = new JTextField(15);
        add(txtQuantity, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        txtPrice = new JTextField(15);
        add(txtPrice, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Supplier:"), gbc);
        gbc.gridx = 1;
        txtSupplier = new JTextField(15);
        add(txtSupplier, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Reorder Level:"), gbc);
        gbc.gridx = 1;
        txtReorder = new JTextField(15);
        add(txtReorder, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // Actions
        btnSave.addActionListener(e -> saveProduct());
        btnCancel.addActionListener(e -> dispose());
    }

    private void saveProduct() {
        try {
            String productId = txtProductId.getText().trim();
            String name = txtName.getText().trim();
            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            double price = Double.parseDouble(txtPrice.getText().trim());
            String supplier = txtSupplier.getText().trim();
            int reorder = Integer.parseInt(txtReorder.getText().trim());

            if (productId.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Product ID and Name are required.");
                return;
            }

            Product product = new Product(productId, name, quantity, price, supplier, reorder);
            if (productDAO.addProduct(product)) {
                JOptionPane.showMessageDialog(this, "Product added successfully.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding product. ID may already exist.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity, Price, and Reorder Level must be numbers.");
        }
    }
}