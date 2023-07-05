package ru.practicum.ewmmain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.category.model.Category;
import ru.practicum.ewmmain.exception.NotFoundException;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    default Category validateCategory(Long catId) {
        return findById(catId).orElseThrow(() -> new NotFoundException(
                "Категория с id " + catId + " не найдена"));
    }
}
