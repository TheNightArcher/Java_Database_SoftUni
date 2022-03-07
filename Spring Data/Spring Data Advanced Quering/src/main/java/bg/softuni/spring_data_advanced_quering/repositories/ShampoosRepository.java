package bg.softuni.spring_data_advanced_quering.repositories;

import bg.softuni.spring_data_advanced_quering.entities.Shampoo;
import bg.softuni.spring_data_advanced_quering.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShampoosRepository extends JpaRepository<Shampoo, Long> {

    List<Shampoo> findShampoosBySizeOrderByIdAsc(Size size);
}
