package org.yearup.data;

import org.springframework.stereotype.Repository;
import org.yearup.models.Order;
import org.yearup.models.Profile;

@Repository
public interface OrderDao {
    Order create(Order order, Profile profile);
    Order getById(int orderId);
}
