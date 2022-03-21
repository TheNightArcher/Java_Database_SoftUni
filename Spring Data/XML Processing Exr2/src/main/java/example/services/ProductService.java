package example.services;

import example.models.dtos.ProductSeedDto;
import example.models.dtos.ProductsViewRootDto;

import java.util.List;

public interface ProductService {

    long getCount();

    void seedProducts(List<ProductSeedDto> products);

    ProductsViewRootDto findProductsInRangeWithOutBuyer();
}
