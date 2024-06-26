package com.example.warehouse;

import com.example.warehouse.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.Iterator;

interface CRUD {
    void create();
    ResultSet read();
    void update();
    void delete();
}
public class BaseLogic implements CRUD {

    protected static Integer ID;
    protected static String WAREHOUSE;
    protected static String MATERIAL;
    protected static Integer COUNT;
    protected static LocalDate DATE;

    @Override
    public void create() {

        String query = """
                INSERT INTO Warehouse (id, warehouses, name, quantity, date) VALUES (DEFAULT, ?, ?, ?, ?)
                """;

        Connection conn = ConnectionManager.open();
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, this.WAREHOUSE);
            statement.setString(2, this.MATERIAL);
            statement.setInt(3, this.COUNT);
            statement.setDate(4, Date.valueOf(this.DATE));
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ConnectionManager.close(conn);

    }

    @Override
    public ResultSet read() {

        String query = """
                SELECT * FROM warehouse;
                """;
        Connection conn = ConnectionManager.open();
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update() {

        String query = """
                
                UPDATE  warehouse SET  warehouses = ?, name = ?, quantity = ?, date = ? WHERE id = ?;
                
                """;

        Connection conn = ConnectionManager.open();
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, this.WAREHOUSE);
            statement.setString(2, this.MATERIAL);
            statement.setInt(3, this.COUNT);
            statement.setDate(4, Date.valueOf(this.DATE));
            statement.setInt(5, this.ID);
            statement.execute();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete() {

        String query = """
                DELETE FROM warehouse  WHERE id = ?;
                """;

        Connection conn = ConnectionManager.open();
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, this.ID);
            System.out.println(this.ID);
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public BaseLogic() {}

    public BaseLogic(Integer id) {
        this.ID = id;
    }

    public BaseLogic(Integer id, String warehouse) {
        this.ID = id;
        this.MATERIAL = warehouse;
    }

    public BaseLogic(Integer id, String warehouse, String nameMaterials) {
        this.ID = id;
        this.WAREHOUSE = warehouse;
        this.MATERIAL = nameMaterials;
    }

    public BaseLogic(Integer id, String warehouse, String nameMaterials, Integer count) {
        this.ID = id;
        this.WAREHOUSE = warehouse;
        this.MATERIAL = nameMaterials;
        this.COUNT = count;
    }

    public BaseLogic (String warehouse, String nameMaterials, Integer count, LocalDate date) {
        this.WAREHOUSE = warehouse;
        this.MATERIAL = nameMaterials;
        this.COUNT = count;
        this.DATE = date;

    }
    public BaseLogic(Integer id, String warehouse, String nameMaterials, Integer count, LocalDate date) {
        this.ID = id;
        this.WAREHOUSE = warehouse;
        this.MATERIAL = nameMaterials;
        this.COUNT = count;
        this.DATE = date;
    }

}
