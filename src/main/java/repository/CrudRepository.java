package repository;

import java.util.List;

public interface CrudRepository<T,ID> {
    T create(T entity);         // Создание сущности
    T read(ID id);              // Чтение сущности по ID
    T update(T entity);         // Обновление сущности
    boolean delete(ID id);      // Удаление сущности по ID
    List<T> findAll();          // Получение всех сущностей
}
