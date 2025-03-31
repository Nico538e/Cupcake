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

    // works
    public static List<Topping> getAllToppings(ConnectionPool connectionPool) throws DatabaseException {
        List<Topping> toppingList = new ArrayList<>();
        String sql = "SELECT topping_id, topping_name, topping_price FROM topping";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int toppingId = rs.getInt("topping_id");
                String toppingName = rs.getString("topping_name");
                double toppingPrice = rs.getDouble("topping_price");

                Topping topping = new Topping(toppingId,toppingName, toppingPrice);
                toppingList.add(topping);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get all toppings", e);
        }
        return toppingList;
    }

    //works
    public static List<Bottom> getAllBottoms(ConnectionPool connectionPool) throws DatabaseException {
        List<Bottom> bottomList = new ArrayList<>();

        String sql = "SELECT bottom_id, bottoms_name, bottom_price FROM bottom";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int bottomId = rs.getInt("bottom_id");
                String bottomName = rs.getString("bottoms_name");
                double bottomPrice = rs.getDouble("bottom_price");

                Bottom bottom = new Bottom(bottomId,bottomName, bottomPrice);
                bottomList.add(bottom);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get all bottoms", e);
        }
        return bottomList;
    }

    //works
    public static Topping getOneToppingById(ConnectionPool connectionPool, int toppingId) throws DatabaseException {
        String sql = "SELECT topping_name, topping_price FROM topping where topping_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ){
            // get topping by id
            ps.setInt(1, toppingId);
            ResultSet rs = ps.executeQuery();

            // if the topping exist give me a topping object
            if (rs.next()) {
                String toppingName = rs.getString("topping_name");
                double toppingPrice = rs.getDouble("topping_price");

                return new Topping(toppingId,toppingName, toppingPrice);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get the topping by id", e);
        }
        // if the topping do not exist return null
        return null;
    }


    // works
    public static Bottom getOneBottomById(ConnectionPool connectionPool, int bottomId ) throws DatabaseException {
        String sql = "SELECT bottoms_name, bottom_price FROM bottom where bottom_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, bottomId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String bottomName = rs.getString("bottoms_name");
                double bottomPrice = rs.getDouble("bottom_price");

                return new Bottom(bottomId,bottomName, bottomPrice);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get the bottom by id", e.getMessage());
        }
        return null;
    }

    // not working
    public static Topping getOneToppingByName(ConnectionPool connectionPool, String toppingName) throws DatabaseException {
        String sql = "SELECT topping_name, topping_price FROM topping where topping_name = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ){
            // get topping by name
            ps.setString(1, toppingName);
            ResultSet rs = ps.executeQuery();

            // if the topping exist give me a topping object
            if (rs.next()) {
                double toppingPrice = rs.getDouble("topping_price");

                return new Topping(toppingName, toppingPrice);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get the topping by the name", e);
        }
        // if the topping do not exist return null
        return null;
    }

    //not working
    public static Bottom getOneBottomByName(ConnectionPool connectionPool, String bottomName) throws DatabaseException {
        String sql = "SELECT bottoms_name, bottom_price FROM bottom where bottoms_name = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setString(1, bottomName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double bottomPrice = rs.getDouble("bottom_price");

                return new Bottom(bottomName, bottomPrice);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get the bottom by the name", e);
        }
        return null;
    }

    // working but not tested so far
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
            throw new DatabaseException("Failed trying to get the order_lines", e);
        }
        return orderLineList;
    }

    // working tested but there is no orders
    // get all order for a specific user
    public static List<Orders> getOrdersByUserId(ConnectionPool connectionPool, int userId) throws DatabaseException{
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT * FROM orders Where user_id = ?";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int orderId = rs.getInt("order_id");

                Orders order = new Orders(orderId,userId);
                ordersList.add(order);
            }

        }catch(SQLException e){
            throw new DatabaseException("Failed could not get the order from the specific user: " + userId, e);
        }
        return ordersList;
    }


    //
    // save orders to the orders table with orderLines
    public static Orders addOrderWithOrderLines(ConnectionPool connectionPool, int userId, List<OrderLine> orderLines) throws DatabaseException {
        Orders newOrder = null;
        String sqlOrder = "INSERT INTO orders (user_id) Values (?)";
        String sqlOrderLines = "INSERT INTO order_line (order_line_id, order_id, bottom_id, topping_id, amount, order_line_price) VALUES (?,?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)
        ) {
            // insert new order
            ps.setInt(1, userId);
            int rowAffected = ps.executeUpdate();

            if(rowAffected == 0){
                throw new DatabaseException("Could not make a new order for the user with the id: " + userId);
            }

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    int newOrderId =  rs.getInt(1);

                    newOrder = new Orders(newOrderId,userId);

                    // insert orderLines
                    try(PreparedStatement ps2 = connection.prepareStatement(sqlOrderLines)) {
                        for(OrderLine o: orderLines) {
                            ps2.setInt(1, o.getOrderLineId());
                            ps2.setInt(2, newOrderId);
                            ps2.setInt(3, o.getBottomId());
                            ps2.setInt(4, o.getToppingId());
                            ps2.setDouble(5, o.getAmount());
                            ps2.setDouble(6, o.getOrderLinePrice());
                            ps2.executeUpdate();
                        }
                    }
                }else {
                    throw new DatabaseException("could not get orderId that belongs to the userId: " + userId);
                }
            }
        } catch(SQLException e){
            throw new DatabaseException("failed trying to make an order", e);
        }
        return newOrder;
    }


}
