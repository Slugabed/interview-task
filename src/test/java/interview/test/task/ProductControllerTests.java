package interview.test.task;

import interview.test.task.contoller.ProductController;
import interview.test.task.model.Product;
import interview.test.task.exception.ProductNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SpringBootTest
class ProductControllerTests {

    @Autowired
    private ProductController productController;
    private static final List<Product> newProducts = new ArrayList<>();

    @BeforeAll
    static void init() {
        Calendar cal = Calendar.getInstance();
        cal.clear();

        cal.set(2018, Calendar.SEPTEMBER, 15);
        newProducts.add(new Product(null, "IPhone",
                new BigDecimal("1500.00"), cal.getTime()));

        cal.set(2019, Calendar.SEPTEMBER, 15);
        newProducts.add(new Product(null, "IPhone",
                new BigDecimal("1800.00"), cal.getTime()));

        cal.set(2020, Calendar.SEPTEMBER, 15);
        newProducts.add(new Product(null, "IPhone",
                new BigDecimal("2100.00"), cal.getTime()));
    }

    @Test
    void createNewProductsTest() {
        List<Product> apiResponses = new ArrayList<>();
        for (Product newProduct : newProducts) {
            apiResponses.add(productController.newProduct(newProduct).getBody());
        }

        for (Product apiResponse : apiResponses) {
            Product savedProduct = productController.getById(apiResponse.getId()).getBody();
            Assertions.assertNotNull(savedProduct);
            Assertions.assertTrue(TestUtils.isEquals(apiResponse, savedProduct),
                    String.format("%s api response is not the same as returned %s",
                            apiResponse.toString(), savedProduct));
        }
    }

    @Test
    void findNonExistingTest() {
        Long nonExistingId = 123456L;
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productController.getById(nonExistingId));
    }

    @Test
    void updateExistingProductTest() {
        Product toBeUpdated = new Product(new Product(), newProducts.get(1));
        productController.newProduct(toBeUpdated);

        toBeUpdated.setName("Iphone 11");
        toBeUpdated.setPrice(new BigDecimal("1700.00"));

        Product updateResponse = productController.updateProduct(toBeUpdated.getId(), toBeUpdated).getBody();
        Assertions.assertNotNull(updateResponse);
        Product savedProduct = productController.getById(toBeUpdated.getId()).getBody();
        Assertions.assertNotNull(savedProduct);
        Assertions.assertTrue(TestUtils.isEquals(
                savedProduct, updateResponse),
                String.format("Updating an existing product failed. %s != %s",
                        savedProduct.toString(), updateResponse.toString()));
    }

    @Test
    void updateNewProductTest() {
        Product newProduct = new Product(new Product(), newProducts.get(0));

        productController.updateProduct(newProduct.getId(), newProduct);
        Assertions.assertTrue(TestUtils.isEquals(
                productController.getById(newProduct.getId()).getBody(), newProduct),
                "Updating a new product failed");
    }

    @Test
    void deleteExistingProductTest() {
        Product newProduct = new Product(new Product(), newProducts.get(1));
        Product apiResponse = productController.newProduct(newProduct).getBody();
        Assertions.assertNotNull(apiResponse);
        productController.deleteProduct(apiResponse.getId());
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productController.getById(apiResponse.getId()));
    }

    @Test
    void deleteNonExistingProductTest() {
        Long nonExistingId = 123456L;
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productController.deleteProduct(nonExistingId));
    }
}
