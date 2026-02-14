package gui;

import dao.ProductDAO;
import model.Product;

import javax.swing.*;
import java.awt.*;

public class UpdateProductDialog extends JDialog {
    private JTextField txtName, txtQuantity, txtPrice, txtSupplier, txtReorder;
    private ProductDAO productDAO;
    private Product product;

    public UpdateProductDialog(JFrame parent, ProductDAO productDAO, Product product) {
        super(parent, "Update Product", true);
        this.productDAO = productDAO;
        this.product = product;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Product ID (display only)
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1;
        JLabel lblProductId = new JLabel(product.getProductId());
        add(lblProductId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(product.getName(), 15);
        add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        txtQuantity = new JTextField(String.valueOf(product.getQuantity()), 15);
        add(txtQuantity, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        txtPrice = new JTextField(String.valueOf(product.getPrice()), 15);
        add(txtPrice, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Supplier:"), gbc);
        gbc.gridx = 1;
        txtSupplier = new JTextField(product.getSupplier(), 15);
        add(txtSupplier, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Reorder Level:"), gbc);
        gbc.gridx = 1;
        txtReorder = new JTextField(String.valueOf(product.getReorderLevel()), 15);
        add(txtReorder, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnUpdate = new JButton("Update");
        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        btnUpdate.addActionListener(e -> updateProduct());
        btnCancel.addActionListener(e -> dispose());
    }

    private void updateProduct() {
        try {
            product.setName(txtName.getText().trim());
            product.setQuantity(Integer.parseInt(txtQuantity.getText().trim()));
            product.setPrice(Double.parseDouble(txtPrice.getText().trim()));
            product.setSupplier(txtSupplier.getText().trim());
            product.setReorderLevel(Integer.parseInt(txtReorder.getText().trim()));

            if (productDAO.updateProduct(product)) {
                JOptionPane.showMessageDialog(this, "Product updated successfully.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating product.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity, Price, and Reorder Level must be numbers.");
        }
    }
}