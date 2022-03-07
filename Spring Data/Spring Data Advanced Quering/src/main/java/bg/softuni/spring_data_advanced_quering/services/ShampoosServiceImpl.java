package bg.softuni.spring_data_advanced_quering.services;

import bg.softuni.spring_data_advanced_quering.entities.Shampoo;
import bg.softuni.spring_data_advanced_quering.entities.Size;
import bg.softuni.spring_data_advanced_quering.repositories.ShampoosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShampoosServiceImpl implements ShampoosService {

    @Autowired
    private ShampoosRepository shampoosRepository;


    @Override
    public List<Shampoo> selectShampoosBySize(Size size) {

        return this.shampoosRepository.findShampoosBySizeOrderByIdAsc(size);
    }

    @Override
    public List<Shampoo> selectShampoosBySizeOrId(Size size, Long id) {
        return this.shampoosRepository.findShampoosBySizeOrLabelIdOrderByPriceAsc(size, id);
    }

    @Override
    public List<Shampoo> selectShampoosWithPriceBiggerThen(BigDecimal price) {
        return this.shampoosRepository.findShampoosByPriceGreaterThanOrderByPriceDesc(price);
    }

    @Override
    public int countAllShampoosLessThen(BigDecimal price) {
        return this.shampoosRepository.countByPriceLessThan(price);
    }
}
