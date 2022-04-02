package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Apartment;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment,Long> {

    @Query("SELECT a FROM Apartment a WHERE a.area = :area AND a.town.townName = :townName")
    Optional<Apartment> findByTownNameAndArea(String townName, Double area);

}
