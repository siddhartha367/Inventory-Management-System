# **Inventory Management System**

A **Java Swing** desktop application for managing product inventory and sales, integrated with a **MySQL** database. This system helps small businesses track stock levels, record sales, and maintain supplier information efficiently.

---

## Problem Statement and Objective

### Problem
Manual inventory tracking using spreadsheets or paper records is error‑prone, time‑consuming, and lacks real‑time visibility. Stockouts, overstocking, and inaccurate sales records can lead to lost revenue and customer dissatisfaction.

### Objective
Develop a user‑friendly, GUI‑based **Inventory Management System** that:
- Allows adding, updating, and deleting product details.
- Records sales transactions automatically.
- Provides low‑stock alerts via a reorder level.
- Maintains data integrity through a relational database.
- Offers a simple interface for day‑to‑day operations.

---

## Features

- **Product Management** – Add, edit, delete, and view products (ID, name, quantity, price, supplier, reorder level).
- **Sales Recording** – Record a sale by product ID, automatically update stock quantity, and store sale date.
- **Low Stock Alert** – Highlight products whose quantity falls below the reorder level.
- **MySQL Integration** – Persistent storage using a local MySQL database.
- **Simple GUI** – Built with Java Swing for ease of use.

---

## Technologies Used

- **Java** – Core programming language.
- **Java Swing** – For the graphical user interface.
- **MySQL** – Database for storing product and sales data.
- **JDBC (MySQL Connector/J)** – For database connectivity.
- **VS Code** – Recommended IDE (with Java extensions).
- **MySQL Workbench** – Optional, for database management and script execution.

---

## Prerequisites

Before running the application, ensure you have the following installed:

1. **Java Development Kit (JDK)** – Version 8 or higher.  
   [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)

2. **MySQL Server** – Version 5.7 or higher.  
   [Download MySQL](https://dev.mysql.com/downloads/mysql/)

3. **MySQL Connector/J** – The JDBC driver JAR file.  
   [Download Connector/J](https://dev.mysql.com/downloads/connector/j/) (Platform Independent version)

4. **VS Code** (or any Java IDE) with the **Extension Pack for Java** installed.  
   [Download VS Code](https://code.visualstudio.com/download)

---

## Setup Instructions

Follow these steps to set up the project on your local machine.

### 1. Clone or Download the Project
```
git clone https://github.com/yourusername/inventory-management-system.git
```
Or download the ZIP and extract it.

### 2. Set Up the MySQL Database (Only if you are setting up manually)

- Open **MySQL Workbench** (or the MySQL command line).
- Run the provided SQL script `database_setup.sql` (or create the database manually using the script below).  
  The script creates the `inventory_db` database and the `products` and `sales` tables.

```sql
CREATE DATABASE inventory_db;
USE inventory_db;

CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    supplier VARCHAR(100),
    reorder_level INT NOT NULL
);

CREATE TABLE sales (
    sale_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id VARCHAR(50) NOT NULL,
    quantity_sold INT NOT NULL,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);
```

### 3. Configure Database Credentials

- Navigate to `src/db/DBConnection.java`.
- Update the `USER` and `PASSWORD` constants with your MySQL username and password.
- If your MySQL runs on a non‑default port, modify the `URL` accordingly (e.g., `jdbc:mysql://localhost:3307/inventory_db`).

```java
private static final String USER = "root";        // your MySQL username
private static final String PASSWORD = "yourpassword"; // your MySQL password
```

### 4. Add MySQL Connector/J to the Project

- Copy the downloaded `mysql-connector-java-x.x.xx.jar` file into a `lib` folder at the project root (create it if it doesn't exist).
- In VS Code:
  - Press `Ctrl+Shift+P`, choose **"Java: Configure Classpath"**.
  - Under **"Referenced Libraries"**, click **"Add"** and select the JAR from the `lib` folder.

### 5. Verify the Database Connection

- Open `DBConnection.java` and run its `main` method (right‑click → **Run Java**).  
  You should see `"Connected to database successfully!"` in the terminal.  
  If you get an error, double‑check your MySQL service status and credentials.

---

## How to Run the Program

Once the setup is complete:

1. In VS Code, open the project folder.
2. Locate the main GUI class: `src/gui/MainFrame.java`.
3. Run the file (right‑click → **Run Java**).  
   The main window of the Inventory Management System will appear.

From the GUI you can:
- View all products.
- Add a new product.
- Update or delete existing products.
- Record a sale (which automatically reduces stock).
- See low‑stock warnings.

---

## Project Structure

```
InventoryProject/
├── lib/
│   └── mysql-connector-java-x.x.xx.jar     # JDBC driver
├── src/
│   ├── db/
│   │   └── DBConnection.java                # Database connection handler
│   ├── model/
│   │   ├── Product.java                      # Product POJO
│   │   └── Sale.java                          # Sale POJO
│   ├── dao/
│   │   ├── ProductDAO.java                    # CRUD operations for products
│   │   └── SaleDAO.java                        # CRUD operations for sales
│   └── gui/
│       └── MainFrame.java                      # Main application window
└── README.md                                   # This file
```

*Note: The `dao` and `gui` classes may contain more files as you expand the project.*

---

## Future Enhancements

- Add user authentication (login/logout).
- Generate sales reports with date filters.
- Export data to Excel or PDF.
- Implement a search/filter feature for products.
- Use Maven/Gradle for easier dependency management.

---

## Author

Developed by Rijwal V P and Siddhartha Sanjai – feel free to contribute or report issues on [GitHub](https://github.com/yourusername/inventory-management-system).
