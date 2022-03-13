package bg.softuni.gamestore.util;

import org.hibernate.mapping.Constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidationUtil {

   <E> boolean isValid(E entity);

   <E> Set<ConstraintViolation<E>> getViolations(E entity);
}
