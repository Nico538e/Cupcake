package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;
import org.postgresql.util.PGmoney;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CupcakeMapper {
    public static User login(String userName, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users where user_name=? and user_password=?";

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
        String sql = "insert into users (user_name, user_password) values (?,?)";

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

    public static List<User> getAllUsers(ConnectionPool connectionPool, String role) throws DatabaseException {
        List<User> userNameList = new ArrayList<>();
        String sql = "SELECT user_id, user_name  FROM users where role = 'postgres'";


        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {



            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String userName = rs.getString("user_name");

                User users = new User(userId,userName);
                userNameList.add(users);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get all userNames", e);
        }
        return userNameList;
    }

    // works
    public static List<Topping> getAllToppingNames(ConnectionPool connectionPool) throws DatabaseException {
        List<Topping> toppingNameList = new ArrayList<>();
        String sql = "SELECT topping_name FROM topping";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String toppingName = rs.getString("topping_name");

                Topping topping = new Topping(toppingName);
                toppingNameList.add(topping);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get all toppings", e);
        }
        return toppingNameList;
    }

    // works
    public static List<Bottom> getAllBottomNames(ConnectionPool connectionPool) throws DatabaseException {
        List<Bottom> bottomNameList = new ArrayList<>();

        String sql = "SELECT bottoms_name FROM bottom";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String bottomName = rs.getString("bottoms_name");

                Bottom bottom = new Bottom(bottomName);
                bottomNameList.add(bottom);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get all bottoms", e);
        }
        return bottomNameList;
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

    // working

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

    //working
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




    public static List<ShowUserOrders> getOrdersFromAUserByUserId(ConnectionPool connectionPool, int userId) throws DatabaseException {
        List<ShowUserOrders> userOrderList = new ArrayList<>();

        String sql = "SELECT users.user_name, " +
                "orders.order_id, " +
                "order_line.order_line_id, " +
                "order_line.bottom_id, " +
                "order_line.topping_id, " +
                "bottom.bottoms_name, " +
                "topping.topping_name, " +
                "order_line.order_line_price " +
                "FROM orders join order_line on orders.order_id = order_line.order_id " +
                "join users on users.user_id = orders.user_id " +
                "join bottom on order_line.bottom_id = bottom.bottom_id " +
                "join topping on order_line.topping_id = topping.topping_id " +
                "Where users.user_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            //adding the attribute userId from parameter in the placeholder = ?
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String userName = rs.getString("user_name");
                int orderId = rs.getInt("order_id");
                int orderLineId = rs.getInt("order_line_id");
                int toppingId = rs.getInt("topping_id");
                int bottomId = rs.getInt("bottom_id");
                String toppingName = rs.getString("topping_name");
                String bottomName = rs.getString("bottoms_name");
                double orderLinePrice = rs.getDouble("order_line_price");
                ShowUserOrders usersOrderLine = new ShowUserOrders(userName, orderId, orderLineId, toppingId, bottomId,
                toppingName, bottomName, orderLinePrice);

                userOrderList.add(usersOrderLine);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userOrderList;
    }



    public static boolean insertOrderLinesIntoDb(OrderLine orderLine ,ConnectionPool connectionPool) throws DatabaseException {
        boolean success = false;
        String sql = "INSERT INTO order_line (order_id, bottom_id, topping_id, amount, order_line_price) VALUES (?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            BigDecimal moneyValue = BigDecimal.valueOf(orderLine.getOrderLinePrice());
            ps.setInt(1, orderLine.getOrderId());
            ps.setInt(2, orderLine.getBottomId());
            ps.setInt(3, orderLine.getToppingId());
            ps.setInt(4, orderLine.getAmount());
            ps.setBigDecimal(5, moneyValue);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                success = true;
            }

        } catch (SQLException e) {
            //throw new DatabaseException("Failed trying to get the order_lines", e);
            System.out.println(e.getMessage());
        }
        return success;
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

    //Get amount by userId
    public static double getAmountById(ConnectionPool connectionPool, int userID) throws DatabaseException {
        String sql = "SELECT amount FROM users where user_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                double amount = rs.getDouble("amount");

                return amount;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed trying to get the amount by the name", e);
        }
        //Den ville ikke lade os have den returne null
        return 0;
    }

    public static void updateAmountByID(int userId, double newBalance, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE users SET amount = ? WHERE user_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            PGmoney rigsdaler = new PGmoney(newBalance);

            ps.setObject(1, rigsdaler);
            ps.setInt(2, userId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved opdatering af saldo. Ingen eller flere rækker blev ændret.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved opdatering af saldo: " + e.getMessage());
        }
    }



}
