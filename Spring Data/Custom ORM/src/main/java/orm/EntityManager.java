package orm;

import annotations.Id;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class EntityManager <E> implements DBContext<E>{

    private Collection collection;

    public EntityManager(Collection collection) {
        this.collection = collection;
    }

    @Override
    public boolean persist(E entity) {
        return false;
    }

    @Override
    public Iterable<E> find(Class<E> table) {
        return null;
    }

    @Override
    public Iterable<E> find(Class<E> table, String where) {
        return null;
    }

    @Override
    public E findFirst(Class<E> table) {
        return null;
    }

    @Override
    public E findFirst(Class<E> table, String where) {
        return null;
    }


    private Field getIdColumn(Class<E> clazz){
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Entity does not have primary key"));


    }
}
