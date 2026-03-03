package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.CarServiceImpl;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private String strProduct = "product";

    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute(strProduct, product);
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model) {

        if (product.getProductQuantity() < 1) {
            model.addAttribute("error", "Quantity cannot be zero or negative!");
            model.addAttribute(strProduct, product);
            return "CreateProduct";
        }
        service.create(product);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute(strProduct, allProducts);
        return "ProductList";
    }

    @GetMapping("/edit/{productId}")
    public String editProductPage(@PathVariable String productId, Model model) {
        Product product = service.findById(productId);
        model.addAttribute(strProduct, product);
        return "EditProduct";
    }

    @PostMapping("/edit")
    public String editProductPost(@ModelAttribute Product product, Model model) {
        if (product.getProductQuantity() < 1) {
            model.addAttribute("error", "Quantity cannot be zero or negative!");
            model.addAttribute(strProduct, product);
            return "EditProduct";
        }
        service.edit(product);
        return "redirect:list";
    }

    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable String productId) {
        service.delete(productId);
        return "redirect:/product/list";
    }
}