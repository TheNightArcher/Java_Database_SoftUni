package bg.softuni.spring_data_advanced_quering.services;

import bg.softuni.spring_data_advanced_quering.entities.Shampoo;
import bg.softuni.spring_data_advanced_quering.entities.Size;
import bg.softuni.spring_data_advanced_quering.repositories.ShampoosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShampoosServiceImpl implements ShampoosService {

    @Autowired
    private ShampoosRepository shampoosRepository;


    @Override
    public List<Shampoo> selectShampoosBySize(Size size) {

        return this.shampoosRepository.findShampoosBySizeOrderByIdAsc(size); 
    }
}
