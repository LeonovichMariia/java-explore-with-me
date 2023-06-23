package ru.practicum.ewmmain.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmain.category.dto.CategoryDto;
import ru.practicum.ewmmain.category.dto.NewCategoryDto;
import ru.practicum.ewmmain.category.mapper.CategoryMapper;
import ru.practicum.ewmmain.category.model.Category;
import ru.practicum.ewmmain.category.repository.CategoryRepository;
import ru.practicum.ewmmain.exception.AlreadyExistException;
import ru.practicum.ewmmain.utils.PageSetup;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category newCategory = CategoryMapper.toCategory(newCategoryDto);
        if (categoryRepository.checkIfAlreadyExist(newCategory.getName()).isPresent()) {
            throw new AlreadyExistException("Данная категория уже существует");
        }
        CategoryDto savedCategory = CategoryMapper.toCategoryDto(categoryRepository.save(newCategory));
        log.info("Категория {} сохранена", savedCategory);
        return savedCategory;
    }

    @Override
    @Transactional
    public CategoryDto renewalCategory(Long catId, NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.validateCategory(catId);
        if (category.getName().equals(newCategoryDto.getName())) {
            throw new AlreadyExistException("Данная категория уже существует");
        }
        category.setName(newCategoryDto.getName());
        CategoryDto updatedCategory = CategoryMapper.toCategoryDto(categoryRepository.save(category));
        log.info("Категория с id {} обновлена", catId);
        return updatedCategory;
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long catId) {
        categoryRepository.validateCategory(catId);
        categoryRepository.deleteById(catId);
        log.info("Категория с id {} удалена", catId);
    }

    @Override
    public List<CategoryDto> getCategories(Integer size, Integer from) {
        log.info("Получение списка всех категорий");
        return categoryRepository.findAll(new PageSetup(from, size, Sort.by(Sort.Direction.DESC, "id"))).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        log.info("Получение информации о категории с id {}", catId);
        Category category = categoryRepository.validateCategory(catId);
        return CategoryMapper.toCategoryDto(category);
    }
}
