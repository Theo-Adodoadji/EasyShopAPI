package org.yearup.data;

import org.springframework.stereotype.Repository;
import org.yearup.models.OrderLineItem;

@Repository
public interface OrderLineItemDao {

    OrderLineItem create (OrderLineItem orderLineItem);

    OrderLineItem getById(int id);
}
