package com.wallet.controller;

import com.wallet.model.dto.CategoryDto;
import com.wallet.model.dto.PersonDto;
import com.wallet.model.type.TransactionType;
import com.wallet.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@AutoConfigureMockMvc
@AutoConfigureWebMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CategoryController.class)
public class CategoryControllerTest {
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getAllIncomeCategoriesForPerson() throws Exception {
        // given
        var person = new PersonDto()
                .setEmail("somemail@mail.ru");
        var category = new CategoryDto()
                .setName("Kek")
                .setType(TransactionType.INCOME);
        categoryService.createCategory(person.getEmail(), category);

        // when
        Mockito.when(categoryService.getAllCategoriesByType(person.getEmail(), TransactionType.INCOME))
                .thenReturn(Collections.singletonList(category));

        // then
        mockMvc.perform(get("/category"))
                .andDo(print());
    }

}
