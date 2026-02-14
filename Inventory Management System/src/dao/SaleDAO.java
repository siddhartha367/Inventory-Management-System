package dao;

import model.Sale;
import db.DBConnection;

import java.sql.*;

public class SaleDAO {

    // Record a sale and update stock
    public boolean recordSale(Sale sale) {
        Connection conn = null;
        PreparedStatement pstmtSale = null;
        PreparedStatement pstmtUpdate = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert sale record
            String insertSale = "INSERT INTO sales (product_id, quantity_sold) VALUES (?, ?)";
            pstmtSale = conn.prepareStatement(insertSale);
            pstmtSale.setString(1, sale.getProductId());
            pstmtSale.setInt(2, sale.getQuantitySold());
            pstmtSale.executeUpdate();

            // 2. Reduce product quantity
            String updateProduct = "UPDATE products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
            pstmtUpdate = conn.prepareStatement(updateProduct);
            pstmtUpdate.setInt(1, sale.getQuantitySold());
            pstmtUpdate.setString(2, sale.getProductId());
            pstmtUpdate.setInt(3, sale.getQuantitySold());

            int affected = pstmtUpdate.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                return false; // Not enough stock
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmtSale != null) pstmtSale.close();
                if (pstmtUpdate != null) pstmtUpdate.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}