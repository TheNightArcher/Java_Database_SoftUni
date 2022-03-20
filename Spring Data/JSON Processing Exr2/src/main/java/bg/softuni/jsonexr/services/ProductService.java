package bg.softuni.jsonexr.services;

import bg.softuni.jsonexr.models.dtos.ProductNameAndPriceDto;
import bg.softuni.jsonexr.models.dtos.ProductsStatsDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void seedProducts() throws IOException;

    List<ProductNameAndPriceDto> findAllProductsInRangeByPriceOrderByPrice(BigDecimal lower, BigDecimal upper);

}
