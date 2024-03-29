package com.vathana.ecommercespring.service;

import com.vathana.ecommercespring.exception.ProductException;
import com.vathana.ecommercespring.model.Product;
import com.vathana.ecommercespring.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest req);

    public String deleteProduct(Long productId) throws ProductException;

    public Product updateProduct(Long productId, CreateProductRequest req) throws ProductException;

    public Product findProductById(Long productId) throws ProductException;

    public List<Product> findProductByCategory(String category) throws ProductException;

    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice,
                                       Integer minDiscountPrice, String sort, String stock, Integer pageNumber, Integer pageSize);

    public List<Product> searchProductByTitle(String search);

}
