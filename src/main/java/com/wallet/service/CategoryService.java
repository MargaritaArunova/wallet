package com.wallet.service;

import com.wallet.exception.NotFoundException;
import com.wallet.mapper.CategoryMapper;
import com.wallet.model.dto.CategoryDto;
import com.wallet.model.entity.Category;
import com.wallet.model.type.TransactionType;
import com.wallet.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;
    private final PersonService personService;

    @Transactional(readOnly = true)
    public Optional<Category> getCategoryByIdAndType(Long id, TransactionType type) {
        return categoryRepository.findByIdAndType(id, type);
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id, String email) {
        validatePerson(email);

        var categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new NotFoundException(Category.class, id);
        }
        return mapper.map(categoryOpt.get());
    }

    @Transactional
    public CategoryDto createCategory(String email, CategoryDto categoryDto) {
        var person = personService.getPersonByEmail(email);

        var category = mapper.map(categoryDto).setPersonId(person.getId());
        return mapper.map(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId, String email) {
        validatePerson(email);

        var categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            throw new NotFoundException(Category.class, categoryId);
        }
        var category = categoryOpt.get();
        category.setColor(categoryDto.getColor())
                .setName(categoryDto.getName())
                .setType(categoryDto.getType());
        return mapper.map(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id, String email) {
        validatePerson(email);

        categoryRepository.deleteById(id);
    }

    public List<CategoryDto> getAllCategoriesByType(String email, TransactionType categoryType) {
        var person = personService.getPersonByEmail(email);

        return categoryRepository.findCategoriesByPersonIdAndType(person.getId(), categoryType)
                .stream()
                .map(mapper::map)
                .toList();
    }

    private void validatePerson(String email) {
        personService.checkPersonByEmail(email);
    }
}
