package gui;

import dao.ProductDAO;
import dao.SaleDAO;
import model.Product;
import model.Sale;

import javax.swing.*;
import java.awt.*;

public class RecordSaleDialog extends JDialog {
    private JTextField txtProductId, txtQuantity;
    private SaleDAO saleDAO;
    private ProductDAO productDAO;

    public RecordSaleDialog(JFrame parent, SaleDAO saleDAO, ProductDAO productDAO) {
        super(parent, "Record Sale", true);
        this.saleDAO = saleDAO;
        this.productDAO = productDAO;

        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1;
        txtProductId = new JTextField(15);
        add(txtProductId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Quantity Sold:"), gbc);
        gbc.gridx = 1;
        txtQuantity = new JTextField(15);
        add(txtQuantity, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnRecord = new JButton("Record Sale");
        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnRecord);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        btnRecord.addActionListener(e -> recordSale());
        btnCancel.addActionListener(e -> dispose());
    }

    private void recordSale() {
        String productId = txtProductId.getText().trim();
        String qtyStr = txtQuantity.getText().trim();

        if (productId.isEmpty() || qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Product ID and quantity.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be positive.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity must be a valid number.");
            return;
        }

        // ✅ Now check product existence and stock (outside the try-catch)
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Product ID not found.");
            return;
        }
        if (product.getQuantity() < qty) {
            JOptionPane.showMessageDialog(this, "Insufficient stock! Available: " + product.getQuantity());
            return;
        }

        // All good – record the sale
        Sale sale = new Sale(productId, qty);
        if (saleDAO.recordSale(sale)) {
            JOptionPane.showMessageDialog(this, "Sale recorded successfully.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error recording sale. Possibly due to database issue.");
        }
    }
}