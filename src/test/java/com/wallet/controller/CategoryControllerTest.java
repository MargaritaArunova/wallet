package com.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wallet.exception.handler.ExceptionHandlers;
import com.wallet.model.dto.category.CategoryDto;
import com.wallet.model.dto.person.PersonDto;
import com.wallet.model.dto.wallet.WalletDto;
import com.wallet.model.type.TransactionType;
import com.wallet.service.category.CategoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@AutoConfigureWebMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CategoryController.class)
public class CategoryControllerTest {

    private static ObjectMapper mapper;

    private MockMvc mockMvc;

    @Autowired
    private CategoryController categoryController;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(categoryController)
            .setControllerAdvice(new ExceptionHandlers())
            .build();

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }

    @Test
    public void getAllCategories_returnAllCategories() throws Exception {
        // given
        var person = new PersonDto()
            .setEmail("somemail@mail.ru");
        var category = new CategoryDto()
            .setName("Kek")
            .setType(TransactionType.INCOME);

        // when
        Mockito.when(categoryService.getAllCategoriesByType(TransactionType.INCOME, person.getEmail()))
            .thenReturn(Collections.singletonList(category));

        // then
        var result = mockMvc.perform(get("/category")
                .header("email", person.getEmail())
                .param("categoryType", "INCOME"))
            .andExpect(status().isOk())
            .andReturn();
        var contentAsString = result.getResponse().getContentAsString();
        var actualResult = mapper.readValue(
            contentAsString,
            mapper.getTypeFactory().constructCollectionType(List.class, CategoryDto.class)
        );
        assertThat(List.of(category)).usingRecursiveComparison().isEqualTo(actualResult);
    }

}
