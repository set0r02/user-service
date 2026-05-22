package com.innowise.microservice.integration;

import com.innowise.microservice.dto.PaymentCardInputDto;
import com.innowise.microservice.dto.PaymentCardOutputDto;
import com.innowise.microservice.dto.UserInputDto;
import com.innowise.microservice.dto.UserOutputDto;
import com.innowise.microservice.exceptions.handler.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentCardIntegrationTest extends BaseIntegrationTest{

    @Test
    @DisplayName("Create cards, verify max limit of 5 cards, and verify cache eviction on card deletion")
    void cardCreationFlow_andLimitVerification() {
        UserInputDto userRequest = new UserInputDto(
                "Egor", "Chaika", LocalDate.of(2000, 1, 1), "egor.card@mail.com", true
        );

        UserOutputDto userOutputDto = webTestClient.post()
                .uri("/api/users")
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserOutputDto.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(userOutputDto);
        Long userId = userOutputDto.id();
        final Long[] cardIdToDelete = new Long[1]; // Массив для обхода лямбда-ограничений scope

        for (int i = 1; i <= 5; i++) {
            PaymentCardInputDto paymentCardInputDto = new PaymentCardInputDto(
                    String.format("%016d", i), "Egor Chaika", LocalDate.now().plusYears(3), true, userId
            );

            PaymentCardOutputDto paymentCardOutputDto = webTestClient.post()
                    .uri("/api/payment-cards")
                    .bodyValue(paymentCardInputDto)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(PaymentCardOutputDto.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(paymentCardOutputDto);
            if (i == 1) {
                cardIdToDelete[0] = paymentCardOutputDto.id();
            }
        }

        PaymentCardInputDto cardRequest6 = new PaymentCardInputDto(
                "9999999999999999", "Egor Chaika", LocalDate.now().plusYears(3), true, userId
        );

        ErrorResponse error = webTestClient.post()
                .uri("/api/payment-cards")
                .bodyValue(cardRequest6)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(error);
        assertThat(error.message()).contains("User already has maximum number of cards 5");

        webTestClient.get()
                .uri("/api/users/" + userId)
                .exchange()
                .expectStatus().isOk();

        assertThat(Objects.requireNonNull(cacheManager.getCache("users")).get(userId)).isNotNull();

        webTestClient.delete()
                .uri("/api/payment-cards/" + cardIdToDelete[0])
                .exchange()
                .expectStatus().isNoContent();

        assertThat(Objects.requireNonNull(cacheManager.getCache("users")).get(userId)).isNull();
    }

}
