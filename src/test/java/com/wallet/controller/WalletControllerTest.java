package com.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wallet.exception.AuthenticationException;
import com.wallet.exception.NotFoundException;
import com.wallet.exception.handler.ExceptionHandlers;
import com.wallet.model.dto.wallet.WalletDto;
import com.wallet.model.entity.Wallet;
import com.wallet.model.type.CurrencyType;
import com.wallet.service.wallet.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureWebMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WalletController.class)
public class WalletControllerTest {

    private static ObjectMapper mapper;
    private static ObjectWriter writer;

    private MockMvc mockMvc;

    @Autowired
    private WalletController walletController;

    @MockBean
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(walletController)
            .setControllerAdvice(new ExceptionHandlers())
            .build();

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        writer = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    void getWallet_whenWalletDoesNotExists() throws Exception {
        // given
        Long notFoundId = 1L;
        String notFoundEmail = "somemail@mail.ru";

        // when
        when(walletService.getWalletByIdAndPersonId(notFoundId, notFoundEmail))
            .thenThrow(new NotFoundException(Wallet.class, notFoundId));

        // then
        mockMvc.perform(get("/wallet/{not_found_id}", notFoundId)
                .header("email", notFoundEmail))
            .andExpect(status().isNotFound());
    }

    @Test
    void getWallet_whenWalletExists() throws Exception {
        // given
        Long validId = 1L;
        String validEmail = "somemail@mail.ru";
        var wallet = new WalletDto()
            .setIncome(new BigDecimal(0))
            .setName("Name")
            .setAmountLimit(new BigDecimal(1))
            .setBalance(new BigDecimal(0))
            .setSpendings(new BigDecimal(0))
            .setId(validId)
            .setCurrency(CurrencyType.EUR);

        // when
        when(walletService.getWalletByIdAndPersonId(validId, validEmail))
            .thenReturn(wallet);

        // then
        var result = mockMvc.perform(get("/wallet/{id}", validId)
                .header("email", validEmail))
            .andExpect(status().isOk())
            .andReturn();
        var contentAsString = result.getResponse().getContentAsString();
        var actualResult = mapper.readValue(contentAsString, WalletDto.class);
        assertEquals(wallet, actualResult);
    }

    @Test
    void getWallet_whenAutorizationFailed() throws Exception {
        // given
        Long validId = 1L;
        String invalidEmail = "somemail@mail.ru";
        var wallet = new WalletDto()
            .setIncome(new BigDecimal(0))
            .setName("Name")
            .setAmountLimit(new BigDecimal(1))
            .setBalance(new BigDecimal(0))
            .setSpendings(new BigDecimal(0))
            .setId(validId)
            .setCurrency(CurrencyType.EUR);

        // when
        doThrow(new AuthenticationException(""))
            .when(walletService).getWalletByIdAndPersonId(validId, invalidEmail);

        // then
        var result = mockMvc.perform(get("/wallet/{id}", validId)
                .header("email", invalidEmail))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    void updateWallet_whenWalletDoesNotExists() throws Exception {
        // given
        Long notFoundId = -1L;
        String notFoundEmail = "somemail@mail.ru";
        var wallet = new WalletDto()
            .setIncome(new BigDecimal(0))
            .setName("Name")
            .setAmountLimit(new BigDecimal(1))
            .setBalance(new BigDecimal(0))
            .setSpendings(new BigDecimal(0))
            .setId(notFoundId)
            .setCurrency(CurrencyType.EUR);
        var walletJson = writer.writeValueAsString(wallet);

        // when
        when(walletService.updateWallet(notFoundId, notFoundEmail, wallet))
            .thenThrow(new NotFoundException(Wallet.class, notFoundId));

        // then
        mockMvc.perform(put("/wallet/{not_found_id}", notFoundId)
                .header("email", notFoundEmail)
                .content(walletJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void createWallet_whenIncorrectInput() throws Exception {
        // given
        var walletWithoutName = new WalletDto()
            .setIncome(new BigDecimal(0))
            .setAmountLimit(new BigDecimal(0))
            .setBalance(new BigDecimal(0))
            .setSpendings(new BigDecimal(0))
            .setId(1L)
            .setCurrency(CurrencyType.EUR);
        long walletWithoutNamePersonId = 1L;

        var walletWithNegativeLimit = new WalletDto()
            .setIncome(new BigDecimal(0))
            .setName("Name")
            .setAmountLimit(new BigDecimal(-1))
            .setBalance(new BigDecimal(0))
            .setSpendings(new BigDecimal(0))
            .setId(1L)
            .setCurrency(CurrencyType.EUR);
        long walletWithNegativeLimitPersonId = 2L;

        var walletWithoutNameJson = writer.writeValueAsString(walletWithoutName);
        var walletWithNegativeLimitJson = writer.writeValueAsString(walletWithNegativeLimit);

        // then
        assertAll(
            () -> mockMvc.perform(post("/wallet")
                    .header("Authorization", walletWithoutNamePersonId)
                    .content(walletWithoutNameJson)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()),
            () -> mockMvc.perform(post("/wallet")
                    .header("Authorization", walletWithNegativeLimitPersonId)
                    .content(walletWithNegativeLimitJson)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        );
    }
}
