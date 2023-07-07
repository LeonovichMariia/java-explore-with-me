package ru.practicum.ewmmain.category.service;

import ru.practicum.ewmmain.category.dto.CategoryDto;
import ru.practicum.ewmmain.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto renewalCategory(Long catId, NewCategoryDto newCategoryDto);

    void deleteCategoryById(Long catId);

    List<CategoryDto> getCategories(Integer size, Integer from);

    CategoryDto getCategoryById(Long catId);
}
