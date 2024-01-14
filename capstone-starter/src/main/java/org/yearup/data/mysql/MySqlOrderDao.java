package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {
    @Autowired
    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order create(Order order, Profile profile) {
        String sql = "INSERT INTO ORDERS (user_id, date, address, city, state, zip, shipping_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,order.getUserId());
            ps.setDate(2, order.getDate());
            ps.setString(3,profile.getAddress());
            ps.setString(4,profile.getCity());
            ps.setString(5,profile.getState());
            ps.setString(6,profile.getZip());
            ps.setBigDecimal(7,order.getShippingAmount());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = ps.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int orderId = generatedKeys.getInt(1);

                    // get the newly inserted category
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
    public Order getById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, orderId);

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

    protected static Order mapRow(ResultSet row) throws SQLException {
        int orderId = row.getInt("order_id");
        int userId = row.getInt("user_id");
        Date date = row.getDate("date");
        String address = row.getString("address");
        String city = row.getString("city");
        String state = row.getString("state");
        String zip = row.getString("zip");
        BigDecimal shippingAmount = row.getBigDecimal("shipping_amount");
        Profile profile = new Profile();
        profile.setUserId(userId);
        profile.setAddress(address);
        profile.setCity(city);
        profile.setState(state);
        profile.setZip(zip);

        return new Order(orderId,userId,date,profile,shippingAmount);
    }


}
