package interview.test.task.model.assembler;

import interview.test.task.contoller.ProductController;
import interview.test.task.model.Product;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductResourceAssembler implements RepresentationModelAssembler<Product, RepresentationModel<Product>> {

    @Override
    public Product toModel(Product entity) {
        entity.add(linkTo(methodOn(ProductController.class).getById(entity.getId()))
                .withSelfRel());
        entity.add(linkTo(methodOn(ProductController.class).all())
                .withRel("products"));
        return entity;
    }

    @Override
    public CollectionModel<RepresentationModel<Product>> toCollectionModel(Iterable<? extends Product> entities) {
        List<RepresentationModel<Product>> result = new ArrayList<>();
        for (Product product: entities){
            result.add(this.toModel(product));
        }
        return CollectionModel.of(result);
    }
}
