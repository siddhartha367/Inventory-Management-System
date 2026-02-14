package gui;

import dao.ProductDAO;
import dao.SaleDAO;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryGUI extends JFrame {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextArea alertArea;          // ✅ JTextArea, not JTextField
    private ProductDAO productDAO;
    private SaleDAO saleDAO;

    public InventoryGUI() {
        productDAO = new ProductDAO();
        saleDAO = new SaleDAO();

        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        initComponents();
        loadProductData();
        refreshAlerts();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Inventory Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Product Table
        String[] columns = {"Product ID", "Name", "Quantity", "Price", "Supplier", "Reorder Level"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Current Inventory"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Add Product");
        JButton btnUpdate = new JButton("Update Product");
        JButton btnDelete = new JButton("Delete Product");
        JButton btnSale = new JButton("Record Sale");
        JButton btnRefresh = new JButton("Refresh");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSale);
        buttonPanel.add(btnRefresh);

        // Alert Area
        alertArea = new JTextArea(5, 50);
        alertArea.setEditable(false);
        alertArea.setForeground(Color.RED);
        alertArea.setFont(new Font("Monospaced", Font.BOLD, 12));
        JScrollPane alertScroll = new JScrollPane(alertArea);
        alertScroll.setBorder(BorderFactory.createTitledBorder("Low Stock Alerts"));

        bottomPanel.add(buttonPanel);
        bottomPanel.add(alertScroll);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Button Actions
        btnAdd.addActionListener(e -> openAddDialog());
        btnUpdate.addActionListener(e -> openUpdateDialog());
        btnDelete.addActionListener(e -> deleteProduct());
        btnSale.addActionListener(e -> openSaleDialog());
        btnRefresh.addActionListener(e -> {
            loadProductData();
            refreshAlerts();
        });
    }

    private void loadProductData() {
        tableModel.setRowCount(0);
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            Object[] row = {
                p.getProductId(),
                p.getName(),
                p.getQuantity(),
                p.getPrice(),
                p.getSupplier(),
                p.getReorderLevel()
            };
            tableModel.addRow(row);
        }
    }

    private void refreshAlerts() {
        List<Product> lowStock = productDAO.getLowStockProducts();
        if (lowStock.isEmpty()) {
            alertArea.setText("No low stock items.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("⚠️ The following products are low in stock:\n");
            for (Product p : lowStock) {
                sb.append(" - ").append(p.getProductId()).append(" : ")
                  .append(p.getName()).append(" (Qty: ")
                  .append(p.getQuantity()).append(" / Reorder: ")
                  .append(p.getReorderLevel()).append(")\n");
            }
            alertArea.setText(sb.toString());
        }
    }

    private void openAddDialog() {
        AddProductDialog dialog = new AddProductDialog(this, productDAO);
        dialog.setVisible(true);
        loadProductData();
        refreshAlerts();
    }

    private void openUpdateDialog() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.");
            return;
        }
        String productId = tableModel.getValueAt(selectedRow, 0).toString();
        Product product = productDAO.getProductById(productId);
        if (product != null) {
            UpdateProductDialog dialog = new UpdateProductDialog(this, productDAO, product);
            dialog.setVisible(true);
            loadProductData();
            refreshAlerts();
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
            return;
        }
        String productId = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete product " + productId + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (productDAO.deleteProduct(productId)) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully.");
                loadProductData();
                refreshAlerts();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting product.");
            }
        }
    }

    private void openSaleDialog() {
        RecordSaleDialog dialog = new RecordSaleDialog(this, saleDAO, productDAO);
        dialog.setVisible(true);
        loadProductData();
        refreshAlerts();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InventoryGUI().setVisible(true);
        });
    }
}