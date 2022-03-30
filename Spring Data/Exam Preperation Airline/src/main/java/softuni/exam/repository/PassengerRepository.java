package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Passenger;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Passenger findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT p from  passengers p " +
            "ORDER BY p.tickets.size DESC ,p.email")
    List<Passenger> findAllByPassengerOrderByCount();
}
