package product_service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product_service.entity.Product;
import product_service.repository.ProductRepository;
import product_service.service.ProductService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testSomething() {
    }

    @Test
    void testSaveProduct() {

        // Arrange
        Product product = new Product();

        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(50000d);
        product.setQuantity(2);

        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product result = productService.saveProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals(50000, result.getPrice());
        assertEquals(2, result.getQuantity());

        // Verify
        verify(productRepository).save(product);
    }

    @Test
    void testGetAllProducts() {

        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Laptop");
        product1.setPrice(20000d);
        product1.setQuantity(2);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Phone");
        product2.setPrice(20000d);
        product2.setQuantity(5);

        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals("Phone", result.get(1).getName());

        // Verify
        verify(productRepository).findAll();
    }

    @Test
    void testGetProductById() {

        // Arrange
        Long id = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(20000d);
        product.setQuantity(2);

        when(productRepository.findById(id))
                .thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(id);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals(20000, result.getPrice());
        assertEquals(2, result.getQuantity());

        // Verify
        verify(productRepository).findById(id);
    }

    @Test
    void testGetProductByIdNotFound() {

        // Arrange
        Long id = 99L;

        when(productRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> {
            productService.getProductById(id);
        });

        // Verify
        verify(productRepository).findById(id);
    }

    @Test
    void testUpdateProduct() {

        Long id = 1L;

        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setName("Old Laptop");
        existingProduct.setPrice(40000d);
        existingProduct.setQuantity(1);

        Product updatedProduct = new Product();
        updatedProduct.setName("New Laptop");
        updatedProduct.setPrice(50000d);
        updatedProduct.setQuantity(2);

        when(productRepository.findById(id))
                .thenReturn(Optional.of(existingProduct));

        when(productRepository.save(existingProduct))
                .thenReturn(existingProduct);

        Product result =
                productService.updateProduct(id, updatedProduct);

        assertNotNull(result);
        assertEquals("New Laptop", result.getName());
        assertEquals(50000, result.getPrice());
        assertEquals(2, result.getQuantity());

        verify(productRepository).findById(id);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void testUpdateProductNotFound() {

        // Arrange
        Long id = 99L;

        Product updatedProduct = new Product();
        updatedProduct.setName("New Laptop");
        updatedProduct.setPrice(50000d);
        updatedProduct.setQuantity(2);

        when(productRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(id, updatedProduct);
        });

        // Verify
        verify(productRepository).findById(id);
    }

    @Test
    void testDeleteProduct() {

        // Arrange
        Long id = 1L;

        Product product = new Product();
        product.setId(id);
        product.setName("Laptop");
        product.setPrice(50000d);
        product.setQuantity(2);

        when(productRepository.findById(id))
                .thenReturn(Optional.of(product));

        // Act
        productService.deleteProduct(id);

        // Verify
        verify(productRepository).findById(id);
        verify(productRepository).delete(product);
    }
}