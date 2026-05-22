package com.innowise.microservice.integration;

import com.innowise.microservice.dto.UserInputDto;
import com.innowise.microservice.dto.UserOutputDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserIntegrationTest extends BaseIntegrationTest{
    @Test
    @DisplayName("Full User lifecycle using WebTestClient")
    void userLifecycleFlow() {
        UserInputDto userOutputDto = new UserInputDto(
                "Dima", "Smirnov", LocalDate.of(1987, 5, 12), "smirnov.doe@mail.com", true
        );

        UserOutputDto created = webTestClient.post()
                .uri("/api/users")
                .bodyValue(userOutputDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserOutputDto.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(created);
        Long userId = created.id();

        UserOutputDto fetched = webTestClient.get()
                .uri("/api/users/" + userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserOutputDto.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(fetched);
        assertEquals("Dima", fetched.name());

        UserOutputDto cachedUser = Objects.requireNonNull(cacheManager.getCache("users"))
                .get(userId, UserOutputDto.class);

        assertNotNull(cachedUser);

        webTestClient.delete()
                .uri("/api/users/" + userId)
                .exchange()
                .expectStatus().isNoContent(); // Проверяем статус 204

        assertThat(userRepository.findById(userId)).isEmpty();
        assertThat(Objects.requireNonNull(cacheManager.getCache("users")).get(userId)).isNull();
    }
}
