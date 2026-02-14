package model;

import java.util.Date;

public class Sale {
    private int saleId;
    private String productId;
    private int quantitySold;
    private Date saleDate;

    public Sale() {}

    public Sale(String productId, int quantitySold) {
        this.productId = productId;
        this.quantitySold = quantitySold;
    }

    // getters and setters
    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public int getQuantitySold() { return quantitySold; }
    public void setQuantitySold(int quantitySold) { this.quantitySold = quantitySold; }

    public Date getSaleDate() { return saleDate; }
    public void setSaleDate(Date saleDate) { this.saleDate = saleDate; }
}