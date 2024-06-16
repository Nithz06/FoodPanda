import model.dao.CustomerDaoImpl;
import model.dao.OrderDaoImpl;
import model.entity.Customer;
import model.entity.Order;
import model.entity.Product;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static <String> void main(String[] args) {

        java.lang.Object LocalDate;
        new OrderDaoImpl().addNewOrder(Order.builder()
                .id(4)
                .orderName("BBB")
                .orderDescription("dddddd")
                .orderedAt(Date.valueOf(LocalDate.now()))
                .customer(Customer.builder()
                                .id(2)
                                .build())
                .productsList(new ArrayList<>(
                        List.of(Product.builder()
                                .id(1)
                                .build())
                        ))
                .build());
    }
}