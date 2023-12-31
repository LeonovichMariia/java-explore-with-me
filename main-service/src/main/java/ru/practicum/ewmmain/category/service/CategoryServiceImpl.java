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
import ru.practicum.ewmmain.event.repository.EventRepository;
import ru.practicum.ewmmain.exception.CategoryIsNotEmptyException;
import ru.practicum.ewmmain.exception.NotFoundException;
import ru.practicum.ewmmain.utils.PageSetup;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category newCategory = CategoryMapper.toCategory(newCategoryDto);
        CategoryDto savedCategory = CategoryMapper.toCategoryDto(categoryRepository.save(newCategory));
        log.info("Категория {} сохранена", savedCategory);
        return savedCategory;
    }

    @Override
    public CategoryDto renewalCategory(Long catId, NewCategoryDto newCategoryDto) {
        Category category = validateCategory(catId);
        category.setName(newCategoryDto.getName());
        CategoryDto updatedCategory = CategoryMapper.toCategoryDto(categoryRepository.save(category));
        log.info("Категория с id {} обновлена", catId);
        return updatedCategory;
    }

    @Override
    public void deleteCategoryById(Long catId) {
        validateCategory(catId);
        if (!eventRepository.findByCategoryId(catId).isEmpty()) {
            log.error("У данной категории имеются связанные с ней события");
            throw new CategoryIsNotEmptyException("У данной категории имеются связанные с ней события");
        }
        categoryRepository.deleteById(catId);
        log.info("Категория с id {} удалена", catId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(Integer size, Integer from) {
        log.info("Получение списка всех категорий");
        return categoryRepository.findAll(new PageSetup(from, size, Sort.by(Sort.Direction.DESC, "id"))).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long catId) {
        log.info("Получение информации о категории с id {}", catId);
        Category category = validateCategory(catId);
        return CategoryMapper.toCategoryDto(category);
    }

    private Category validateCategory(Long catId) {
        return categoryRepository.findCategoryById(catId).orElseThrow(() -> new NotFoundException(
                "Категория с id " + catId + " не найдена"));
    }
}
