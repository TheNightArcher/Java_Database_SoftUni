package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Offer;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    boolean existsByAgent(Agent agent);

    @Query("SELECT o FROM offers o " +
            "ORDER BY o.apartment.area DESC , o.price")
    List<Offer> findAllOffersOfThreeRoomsApartments();
}
