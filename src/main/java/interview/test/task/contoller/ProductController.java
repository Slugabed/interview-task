package interview.test.task.contoller;

import interview.test.task.model.assembler.ProductResourceAssembler;
import interview.test.task.repository.ProductRepository;
import interview.test.task.model.Product;
import interview.test.task.exception.ProductNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductResourceAssembler productResourceAssembler;

    public ProductController(ProductRepository productRepository, ProductResourceAssembler productResourceAssembler) {
        this.productRepository = productRepository;
        this.productResourceAssembler = productResourceAssembler;
    }

    @GetMapping
    public HttpEntity<CollectionModel<RepresentationModel<Product>>> all() {
        Iterable<Product> products = productRepository.findAll();
        return new ResponseEntity<>(productResourceAssembler.toCollectionModel(products), HttpStatus.OK);
    }

    @PostMapping
    public HttpEntity<Product> newProduct(@RequestBody Product newProduct) {
        Product saved = productRepository.save(newProduct);
        return new ResponseEntity<>(productResourceAssembler.toModel(saved), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public HttpEntity<Product> getById(@PathVariable("id") Long id) {
        Product found = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id));
        return new ResponseEntity<>(productResourceAssembler.toModel(found), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public HttpEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product newProduct) {
        Product result = productRepository.findById(id)
                .map(product -> productRepository.save(new Product(product, newProduct)))
                .orElseGet(() -> {
                    newProduct.setId(id);
                    return productRepository.save(newProduct);
                });
        return new ResponseEntity<>(productResourceAssembler.toModel(result), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        try {
            productRepository.deleteById(id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new ProductNotFoundException(id);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
