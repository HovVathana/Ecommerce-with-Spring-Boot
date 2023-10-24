package com.vathana.ecommercespring.service;

import com.vathana.ecommercespring.exception.CartItemException;
import com.vathana.ecommercespring.exception.ProductException;
import com.vathana.ecommercespring.exception.UserException;
import com.vathana.ecommercespring.model.Cart;
import com.vathana.ecommercespring.model.CartItem;
import com.vathana.ecommercespring.model.Product;
import com.vathana.ecommercespring.model.User;
import com.vathana.ecommercespring.repository.CartRepository;
import com.vathana.ecommercespring.request.AddItemRequest;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImplementation implements CartService {

    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;
    private UserService userService;

    public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService, ProductService productService, UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();

        cart.setUser(user);

        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException, UserException, CartItemException {

        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null ) {

            User user = userService.findUserById(userId);

            cart = createCart(user);
        }
        Product product = productService.findProductById(req.getProductId());

        CartItem isPresent = cartItemService.isCartItemExist(cart, product, req.getSize(), userId);

        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);
            cartItem.setSize(req.getSize());
            int price = req.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);


            cart.getCartItems().add(createdCartItem);
        } else {
            isPresent.setQuantity(isPresent.getQuantity() + 1);
            isPresent.setPrice(isPresent.getPrice() * 2);
            isPresent.setDiscountedPrice(isPresent.getDiscountedPrice() * 2);

            cartItemService.updateCartItem(userId, isPresent.getId(), isPresent);
        }

        findUserCart(userId);

        return "Item add to cart";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getPrice();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setDiscount(totalPrice - totalDiscountedPrice);

        return cartRepository.save(cart);
    }
}
