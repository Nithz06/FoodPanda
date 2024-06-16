package model.dao;

import model.entity.Order;

import java.util.List;

public interface OrderDao {
    int addNewOrder(Order order);

    int deleteOrder(Integer id);

    int updateOrder(Integer id);

    Order searchOrder(Integer id);

    List<Order> queryAllOrders();
}
