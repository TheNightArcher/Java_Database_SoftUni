package bg.softuni.spring_data_advanced_quering.services;

import bg.softuni.spring_data_advanced_quering.entities.Shampoo;
import bg.softuni.spring_data_advanced_quering.entities.Size;

import java.util.List;

public interface ShampoosService {

    List<Shampoo> selectShampoosBySize (Size size);
}
