package com.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wallet.exception.NotFoundException;
import com.wallet.model.dto.WalletDto;
import com.wallet.model.entity.Wallet;
import com.wallet.model.type.CurrencyType;
import com.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    static ObjectMapper mapper;

    static ObjectWriter writer;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @BeforeAll
    public static void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        writer = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    public void postWalletWhenIncorrectWalletGivenThrowsException() throws Exception {
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

    @Test
    void getWalletReturnsNotFoundWhenWalletNotFound() throws Exception {
        // given
        Long notFoundId = 1L;
        String notFoundEmail = "somemail@mail.ru";

        // when
        when(walletService.getWalletByPersonId(notFoundId, notFoundEmail))
                .thenThrow(new NotFoundException(Wallet.class, notFoundId));

        // then
        mockMvc.perform(get("/wallet/{not_found_id}", notFoundId)
                        .header("email", notFoundEmail))
                .andExpect(status().isNotFound());
    }

    @Test
    void putWalletReturnsNotFoundWhenWalletNotFound() throws Exception {
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
    void returnsOKWHenGetWalletByIdGetsValidId() throws Exception {
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
        when(walletService.getWalletByPersonId(validId, validEmail))
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
}
