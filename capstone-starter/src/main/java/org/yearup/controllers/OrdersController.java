package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;
import org.yearup.models.*;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrdersController {

    private OrderDao orderDao;
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProfileDao profileDao;
    private OrderLineItemDao orderLineItemDao;
    @Autowired
    public OrdersController(OrderDao orderDao, ShoppingCartDao shoppingCartDao, UserDao userDao, ProfileDao profileDao, OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
        this.orderLineItemDao = orderLineItemDao;

    }
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Order createOrder(Order order, OrderLineItem orderLineItem, Principal principal){
        try{
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            order.setUserId(userId);
            order.setDate(new Date(System.currentTimeMillis()));
            order.setProfile(profileDao.getByUserId(userId));
            order.setShippingAmount(shoppingCartDao.getByUserId(userId).getTotal());
            order = orderDao.create(order, profileDao.getByUserId(userId));
            List<OrderLineItem>  orderLineItems = new ArrayList<>();

            if(order != null){
                for(ShoppingCartItem shoppingCartItem: shoppingCartDao.getByUserId(userId).getItems().values()){
                    orderLineItem.setOrderId(order.getOrderId());
                    orderLineItem.setProductId(shoppingCartItem.getProductId());
                    orderLineItem.setSalesPrice(shoppingCartItem.getLineTotal());
                    orderLineItem.setQuantity(shoppingCartItem.getQuantity());
                    orderLineItem.setDiscount(shoppingCartItem.getDiscountPercent());


                    orderLineItems.add( orderLineItemDao.create(orderLineItem));
                }
            }
            order.setOrderLineItems(orderLineItems);

            shoppingCartDao.clearCart(userId);
            return order;
        }
        catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"my booooo");
        }


    }
}
