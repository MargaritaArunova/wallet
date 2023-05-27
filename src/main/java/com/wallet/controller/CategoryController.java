package com.wallet.controller;

import com.wallet.model.dto.category.CategoryDto;
import com.wallet.model.type.TransactionType;
import com.wallet.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;

@RequestMapping("/category")
@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Метод для получения категории")
    @GetMapping(value = "/{id}")
    public CategoryDto getCategory(
        @PathVariable("id") Long id,
        @RequestHeader("email") String email
    ) {
        return categoryService.getCategoryById(id, email);
    }

    @Operation(summary = "Метод для создания категории",
        description = "Для создания категории нужно передать непустое имя и цвет")
    @PostMapping
    public CategoryDto createCategory(
        @Valid @RequestBody CategoryDto categoryDto,
        @RequestHeader("email") String email
    ) {
        return categoryService.createCategory(categoryDto, email);
    }

    @Operation(summary = "Метод для удаления категории")
    @DeleteMapping(value = "/{id}")
    public void deleteCategory(
        @PathVariable("id") Long id,
        @RequestHeader("email") String email
    ) {
        categoryService.deleteCategory(id, email);
    }

    @Operation(summary = "Метод для обновления категории")
    @PutMapping(value = "/{id}")
    public CategoryDto updateCategory(
        @Valid @RequestBody CategoryDto categoryDto,
        @PathVariable("id") Long categoryId,
        @RequestHeader("email") String email
    ) {
        return categoryService.updateCategory(categoryDto, categoryId, email);
    }

    @Operation(summary = "Метод для получения категорий по типу")
    @GetMapping
    public List<CategoryDto> getCategoryByType(
        @RequestHeader("email") String email,
        @RequestParam("categoryType") TransactionType categoryType
    ) {
        return categoryService.getAllCategoriesByType(categoryType, email);
    }

}
