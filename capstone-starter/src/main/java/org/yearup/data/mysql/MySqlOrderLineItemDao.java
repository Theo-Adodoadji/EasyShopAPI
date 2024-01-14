package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderLineItemDao;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlOrderLineItemDao extends MySqlDaoBase implements OrderLineItemDao {
    @Autowired
    public MySqlOrderLineItemDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public OrderLineItem create(OrderLineItem orderLineItem) {
        String sql = "INSERT INTO order_line_items (order_line_item_id, order_id, product_id, sales_price, quantity, discount) VALUES (?, ?,?,?,?,?);";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,orderLineItem.getOrderLineItemId());
            ps.setInt(2,orderLineItem.getOrderId());
            ps.setInt(3,orderLineItem.getProductId());
            ps.setBigDecimal(4,orderLineItem.getSalesPrice());
            ps.setInt(5,orderLineItem.getQuantity());
            ps.setBigDecimal(6,orderLineItem.getDiscount());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0 ){
                ResultSet generatedKeys = ps.getGeneratedKeys();

                if (generatedKeys.next()){
                    int orderId = generatedKeys.getInt(1);
                    return getById(orderId);
                }
            }

        }
        catch (SQLException exception){
            throw new RuntimeException(exception);
        }

        return null;
    }
    @Override
    public OrderLineItem getById(int Id) {
        String sql = "SELECT * FROM order_line_items WHERE order_line_item_id = ?";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Id);

            ResultSet row = statement.executeQuery();

            if (row.next())
            {
                return mapRow(row);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    protected static OrderLineItem mapRow(ResultSet row) throws SQLException {
        int orderLineItemId = row.getInt("order_line_item_id");
        int orderId = row.getInt("order_id");
        int productId = row.getInt("product_id");
        BigDecimal salesPrice = row.getBigDecimal("sales_price");
        int quantity = row.getInt("quantity");
        BigDecimal discount = row.getBigDecimal("discount");

        return new OrderLineItem(orderLineItemId,orderId,productId,salesPrice,quantity,discount);
    }

}
