package interview.test.task.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id){
        super(String.format("Product with id=%d not found", id));
    }
}
