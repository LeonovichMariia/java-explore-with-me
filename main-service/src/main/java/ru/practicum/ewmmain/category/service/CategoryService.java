package ru.practicum.ewmmain.category.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewmmain.category.dto.CategoryDto;
import ru.practicum.ewmmain.category.dto.NewCategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto renewalCategory(Long catId, NewCategoryDto newCategoryDto);

    void deleteCategoryById(Long catId);

    List<CategoryDto> getCategories(Integer size, Integer from);

    CategoryDto getCategoryById(Long catId);
}
