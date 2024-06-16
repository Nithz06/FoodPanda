package model.dao;

import model.entity.Customer;
import model.entity.Order;
import model.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderDaoImpl implements OrderDao{
    @Override
    public int addNewOrder(Order order) {
        String sql = """
                INSERT INTO "order" (id,order_name, order_description, cus_id, ordered_at)
                VALUES (?,?, ?, ?, ?)
                """;
        String sql1 = """
                INSERT INTO "product_order"
                VALUES (?,?)
                """;
        try(
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/foodpanda",
                        "postgres",
                        "1234"
                );
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                PreparedStatement preparedStatement1
                        = connection.prepareStatement(sql1);

                Statement statement
                        = connection.createStatement();
                ){
            preparedStatement.setInt(1,order.getId());
            preparedStatement.setString(2,order.getOrderName());
            preparedStatement.setString(3, order.getOrderDescription());
            preparedStatement.setInt(4,order.getCustomer().getId());
            preparedStatement.setDate(5,order.getOrderedAt());
            int rowAffected = preparedStatement.executeUpdate();
            String message = rowAffected>0 ? "Insert Order successfully": "Insert Order failed";
            System.out.println(message);

            // product Order
            for(Product product: order.getProductsList()){
                preparedStatement1.setInt(1,product.getId());
                preparedStatement1.setInt(2,order.getId());
            }
            int rowAffected1 = preparedStatement1.executeUpdate();
            if(rowAffected1>0){
                System.out.println("Product has been ordered");
            }else {
                System.out.println("Product out of stock");
            }
        }catch(SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return 0;
    }

    @Override
    public int deleteOrder(Integer id) {
        String sql = """
                Delete from "order" 
                where id = ?
                """;
        try(
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/foodpanda",
                        "postgres",
                        "1234"
                );
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

        ){
            Order order = searchOrder(id);
            if (order != null) {
                preparedStatement.setInt(1, order.getId());
                int rowsAffected = preparedStatement.executeUpdate();
                String message = rowsAffected > 0 ? "Delete Successfully" : "Delete Failed";
                System.out.println(message);
                return rowsAffected;
            } else {
                System.out.println("Order Not Found");
            }


        } catch(SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return 0;
    }

    @Override
    public int updateOrder(Integer id) {
        String sql = """
                Update "order"
                Set order_name = ?, order_description = ?
                where id = ?
                """;
        try(
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/foodpanda",
                        "postgres",
                        "1234"
                );
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ){
            Order order = searchOrder(id);
            if (order != null) {
                System.out.println("[+] Insert order name: ");
                preparedStatement.setString(1, new Scanner(System.in).nextLine());
                System.out.println("[+] Insert order description: ");
                preparedStatement.setString(2, new Scanner(System.in).nextLine());
                preparedStatement.setInt(3,id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected>0){
                    System.out.println("Update Order Successfully");
                    return rowsAffected;
                } else {
                    System.out.println("Update Order Failed");
                }
            } else {
                System.out.println("Not Found order");
            }

        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return 0;
    }

    @Override
    public Order searchOrder(Integer id) {
        String sql = """
                Select * from "order"
                where id = ?
                """;
        try(
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/foodpanda",
                        "postgres",
                        "1234"
                );
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Order order = null;
            while(resultSet.next()){
                order = Order.builder()
                        .id(resultSet.getInt("id"))
                        .orderName(resultSet.getString("order_name"))
                        .orderDescription(resultSet.getString("order_description"))
                        .customer(Customer.builder()
                                .id(resultSet.getInt("cus_id"))
                                .build())
                        .orderedAt(resultSet.getDate("ordered_at"))
                        .build();
            }
            return order;

        } catch(SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    @Override
    public List<Order> queryAllOrders() {
        String sql = """
                SELECT * FROM "order"
                INNER JOIN customer c ON "order".cus_id = c.id
                """;
        try(
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/foodpanda",
                        "postgres",
                        "1234"
                );
                Statement statement = connection.createStatement();
                ){
            ResultSet resultSet  = statement.executeQuery(sql);
            List<Order> orderList = new ArrayList<>();
            while (resultSet.next()){
                orderList.add(
                        Order.builder()
                                .id(resultSet.getInt("id"))
                                .orderName(resultSet.getString("order_name"))
                                .orderDescription(resultSet.getString("order_description"))
                                .customer(Customer.builder()
                                        .id(resultSet.getInt("cus_id"))
                                        .name(resultSet.getString("name"))
                                        .email(resultSet.getString("email"))
                                        .password((resultSet.getString("password")))
                                        .isDeleted(resultSet.getBoolean("is_deleted"))
                                        .createdDate(resultSet.getDate("created_date"))
                                        .build())
                                .build()
                );
            }
            return orderList;

        }catch(SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return new ArrayList<>();
    }
}
