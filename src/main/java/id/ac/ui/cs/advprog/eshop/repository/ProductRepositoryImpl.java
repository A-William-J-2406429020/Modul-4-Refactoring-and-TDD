package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository{
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        if (product.getProductId() == null) {
            product.setProductId(java.util.UUID.randomUUID().toString());
        }
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(String productId){
        for (Product product : productData) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public Product edit(Product product){
        for (int i = 0; i < productData.size(); i++) {
            if (product.getProductId().equals(productData.get(i).getProductId())) {
                if(product.getProductQuantity() <= 0){
                    product.setProductQuantity(productData.get(i).getProductQuantity());
                }
                productData.set(i, product);
                return product;
            }
        }
        return null;
    }

    public void delete(String productId) {
        productData.removeIf(product -> product.getProductId().equals(productId));
    }
}
