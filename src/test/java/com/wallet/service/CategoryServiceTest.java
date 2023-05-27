package com.wallet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wallet.mapper.CategoryMapperImpl;
import com.wallet.model.dto.category.CategoryDto;
import com.wallet.model.entity.Category;
import com.wallet.model.entity.Person;
import com.wallet.model.type.TransactionType;
import com.wallet.repository.category.CategoryRepository;
import com.wallet.service.category.CategoryService;
import com.wallet.service.person.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(classes = CategoryService.class)
public class CategoryServiceTest {

    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryMapperImpl mapper;

    @MockBean
    private PersonService personService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(
            categoryRepository,
            mapper,
            personService
        );
    }

    @Test
    void createCategory_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var categoryDto = new CategoryDto()
            .setName("category")
            .setType(TransactionType.INCOME)
            .setColor("#ffffff");

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);

        // when
        categoryService.createCategory(categoryDto, person.getEmail());

        // then
        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    void updateCategory_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var category = new Category()
            .setId(1L)
            .setName("category")
            .setType(TransactionType.INCOME)
            .setColor("#ffffff");

        var updateCategoryDto = new CategoryDto()
            .setName("category-1")
            .setColor("#1fffff");

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);
        when(categoryRepository.findById(category.getId()))
            .thenReturn(Optional.of(category));

        // when
        categoryService.updateCategory(updateCategoryDto, category.getId(), person.getEmail());

        // then
        verify(categoryRepository, times(1)).save(
            category.setName("category-1").setColor("#1fffff")
        );
    }

    @Test
    void deleteCategory_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);

        // when
        categoryService.deleteCategory(anyLong(), person.getEmail());

        // then
        verify(categoryRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void getCategoryByIdAndType_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);

        // when
        categoryService.getCategoryByIdAndType(anyLong(), any());

        // then
        verify(categoryRepository, times(1)).findByIdAndType(anyLong(), any());
    }

    @Test
    void getCategoryById_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);
        when(categoryRepository.findById(anyLong()))
            .thenReturn(Optional.of(new Category()));

        // when
        categoryService.getCategoryById(anyLong(), person.getEmail());

        // then
        verify(categoryRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllCategoriesByType_test() {
        // given
        var person = new Person()
            .setId(1L)
            .setEmail("some-mail")
            .setBalance(BigDecimal.valueOf(10000))
            .setIncome(BigDecimal.valueOf(10000))
            .setSpendings(BigDecimal.valueOf(0));
        var category1 = new Category()
            .setId(1L)
            .setName("category1")
            .setType(TransactionType.INCOME)
            .setColor("#ffffff");
        var category2 = new Category()
            .setId(2L)
            .setName("category2")
            .setType(TransactionType.INCOME)
            .setColor("#1fffff");

        when(personService.getPersonByEmail(person.getEmail()))
            .thenReturn(person);
        when(categoryRepository.findCategoriesByPersonIdAndType(person.getId(), TransactionType.INCOME))
            .thenReturn(List.of(category1, category2));

        var categories = Stream.of(category1, category2)
            .map(mapper::map)
            .collect(Collectors.toSet());

        // when
        var result = categoryService.getAllCategoriesByType(TransactionType.INCOME, person.getEmail());

        // then
        verify(categoryRepository, times(1)).findCategoriesByPersonIdAndType(any(), any());
        assertEquals(2, result.size());
        assertThat(categories).usingRecursiveComparison().isEqualTo(new HashSet<>(result));
    }
}
