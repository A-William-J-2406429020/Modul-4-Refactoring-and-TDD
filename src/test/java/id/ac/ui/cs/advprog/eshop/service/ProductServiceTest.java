package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.create(product)).thenReturn(product);

        Product savedProduct = productService.create(product);

        assertNotNull(savedProduct);
        assertEquals(product.getProductName(), savedProduct.getProductName());
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testFindAll() {
        List<Product> productList = Arrays.asList(product);
        Iterator<Product> iterator = productList.iterator();

        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> allProducts = productService.findAll();

        assertFalse(allProducts.isEmpty());
        assertEquals(1, allProducts.size());
        assertEquals(product.getProductName(), allProducts.get(0).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindProductById() {
        when(productRepository.findById(product.getProductId())).thenReturn(product);

        Product foundProduct = productService.findProduct(product.getProductId());

        assertNotNull(foundProduct);
        assertEquals(product.getProductId(), foundProduct.getProductId());
        verify(productRepository, times(1)).findById(product.getProductId());
    }

    @Test
    void testEditProduct() {
        when(productRepository.edit(product)).thenReturn(product);

        Product editedProduct = productService.edit(product);

        assertNotNull(editedProduct);
        assertEquals(product.getProductId(), editedProduct.getProductId());
        verify(productRepository, times(1)).edit(product);
    }

    @Test
    void testDeleteProduct() {
        productService.delete(product.getProductId());
        verify(productRepository, times(1)).delete(product.getProductId());
    }
}