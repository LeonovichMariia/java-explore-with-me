package ru.practicum.ewmmain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.category.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findCategoryById(Long catId);
}
