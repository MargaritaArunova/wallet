package com.wallet.controller;

import com.wallet.model.dto.CategoryDto;
import com.wallet.model.type.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.wallet.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/category")
@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Метод для получения категории")
    @GetMapping(value = "/{id}")
    public CategoryDto getCategory(@PathVariable("id") Long id, @RequestHeader("email") String email) {
        return categoryService.getCategoryById(id, email);
    }

    @Operation(summary = "Метод для создания категории",
            description = "Для создания категории нужно передать непустое имя и цвет")
    @PostMapping
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto,
                                      @RequestHeader("email") String email) {
        return categoryService.createCategory(email, categoryDto);
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
        return categoryService.getAllCategoriesByType(email, categoryType);
    }

}
