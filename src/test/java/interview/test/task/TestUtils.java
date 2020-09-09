package interview.test.task;

import interview.test.task.model.Product;

import java.util.Objects;

public class TestUtils {
    public static boolean isEquals(Product p1, Product p2){
        if (Objects.isNull(p1) || Objects.isNull(p2)) {
            return Objects.isNull(p1) && Objects.isNull(p2);
        }
        return Objects.equals(p1.getId(), p2.getId()) &&
                Objects.equals(p2.getName(), p2.getName()) &&
                Objects.equals(p1.getPrice(), p2.getPrice()) &&
                Objects.equals(p1.getDate(), p2.getDate());
    }
}
