package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CupcakeMapper {
    public static User login(String userName, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users where user_name=? and password=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, userName);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String roles = rs.getString("role");
                double amount = rs.getDouble("amount");
                return new User(userId, userName, password, roles, amount);
            } else {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    public static void createuser(String userName, String password, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "insert into users (user_name, password) values (?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, userName);
            ps.setString(2, password);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    //all the elements Connection, PreparedStatement and ResultSet are closed
    // because if they are not closed the system memory could get exhausted causing memory leaks
    public static ArrayList<Topping> getAllToppings(ConnectionPool connectionPool) throws DatabaseException {
        ArrayList<Topping> toppingList = new ArrayList<>();
        String sql = "SELECT topping_name, topping_price FROM topping";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String toppingName = rs.getString("topping_name");
                double toppingPrice = rs.getDouble("topping_price");

                Topping topping = new Topping(toppingName, toppingPrice);
                toppingList.add(topping);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get the toppings", e.getMessage());
        }
        return toppingList;
    }

    public static ArrayList<Bottom> getAllBottoms(ConnectionPool connectionPool) throws DatabaseException {
        ArrayList<Bottom> bottomList = new ArrayList<>();

        String sql = "SELECT bottom_name, bottom_price FROM topping";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String bottomName = rs.getString("bottom_name");
                double bottomPrice = rs.getDouble("bottom_price");

                Bottom bottom = new Bottom(bottomName, bottomPrice);
                bottomList.add(bottom);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get the bottoms", e.getMessage());
        }
        return bottomList;
    }


    // watch the list of orderLines
    public static List<OrderLine> getOrderLinesByOrderId(ConnectionPool connectionPool, int orderId) throws DatabaseException {
        List<OrderLine> orderLineList = new ArrayList<>();

        String sql = "SELECT order_line_id, bottom_id, topping_id, order_id, amount, order_line_price  " +
                "FROM order_line Where order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
        //adding the attribute orderId from parameter in the placeholder = ?
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderLineId = rs.getInt("order_line_id");
                int bottomId = rs.getInt("bottom_id");
                int toppingId = rs.getInt("topping_id");
                // should maybe be deleted amount
                int amount = rs.getInt("amount");
                double orderLinePrice = rs.getDouble("order_line_price");

                OrderLine orderLine = new OrderLine(orderLineId, bottomId, toppingId, orderId, amount, orderLinePrice);
                orderLineList.add(orderLine);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get the order_lines", e.getMessage());
        }
        return orderLineList;
    }

}
