package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entities.Picture;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {

    boolean existsByPath(String path);

    Picture findByPath(String profilePicture);

    @Query("SELECT p FROM  pictures p " +
            "WHERE p.size > :size " +
            "ORDER BY p.size")
    List<Picture> findAllByGreaterThanOrderBySizeAsc(double size);
}
