package com.vathana.ecommercespring.service;

import com.vathana.ecommercespring.exception.CartItemException;
import com.vathana.ecommercespring.exception.ProductException;
import com.vathana.ecommercespring.exception.UserException;
import com.vathana.ecommercespring.model.Cart;
import com.vathana.ecommercespring.model.User;
import com.vathana.ecommercespring.request.AddItemRequest;

public interface CartService {

    public Cart createCart(User user);

    public String addCartItem(Long userId, AddItemRequest req) throws ProductException, UserException, CartItemException;

    public Cart findUserCart(Long userId);

}
