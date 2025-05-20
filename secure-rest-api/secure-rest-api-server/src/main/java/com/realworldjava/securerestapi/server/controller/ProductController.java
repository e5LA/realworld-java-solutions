package com.realworldjava.securerestapi.server.controller;

import com.realworldjava.securerestapi.server.model.Product;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

  private static final Logger log = LoggerFactory.getLogger(ProductController.class);

  @GetMapping
  public List<Product> getAllProducts(Authentication auth) {
    log.info("Authenticated user: {} with authorities: {}", auth.getName(), auth.getAuthorities());
    return List.of(new Product(1L, "Zero Trust Demo"));
  }

}
