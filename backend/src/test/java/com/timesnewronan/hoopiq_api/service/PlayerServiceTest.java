package com.timesnewronan.hoopiq_api.service;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.timesnewronan.hoopiq_api.repository.PlayerRepository;
import com.timesnewronan.hoopiq_api.repository.PlayerSeasonStatRepository;
import com.timesnewronan.hoopiq_api.repository.PlayerGameStatRepository;
import com.timesnewronan.hoopiq_api.entity.Player;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {
    // declare the mocks for the PlayerService
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerGameStatRepository playerGameStatRepository;

    @Mock
    private PlayerSeasonStatRepository playerSeasonStatRepository;

    @InjectMocks
    private PlayerService playerService;

    // Arrange - set up the data and tell the mock what to return
    // Act - call the method you're testing
    // Assert - verify the result is what you expected

    @Test
    void getPlayerById_playerExists_returnPlayer() {
        // 1. Create a fake player object with id 25
        Player player = new Player();
        player.setId(25L);

        // 2. Tell mock repository "when someone calls findById(25), return this
        // player"
        when(playerRepository.findById(25L)).thenReturn(Optional.of(player));

        // 3. Call playerService.getPlayerById(25L)
        Player result = playerService.getPlayerById(25L);

        // 4. Assert the returned player is not null and has id 25
        assertNotNull(result);
        assertEquals(25L, result.getId());

    }

    @Test
    void getPlayerById_playerNotFound_throwsNotFoundException() {
        // 1. Tell mock repository "When someone calls getByPlayerId(999999), return the
        // player"
        when(playerRepository.findById(999999L)).thenReturn(Optional.empty());

        // 2. Assert the exception is thrown
        assertThrows(ResponseStatusException.class, () -> {
            playerService.getPlayerById(999999L);
        });

    }

}
