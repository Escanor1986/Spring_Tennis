package com.escanor1986.tennis.web;
import com.escanor1986.tennis.data.PlayerList;
import com.escanor1986.tennis.service.PlayerNotFoundException;
import com.escanor1986.tennis.service.PlayerService;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerService playerService;

    @Test
    public void shouldListAllPlayers() throws Exception {
        // Given
        Mockito.when(playerService.getAllPlayers()).thenReturn(PlayerList.ALL);

        // When / Then
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].lastName", CoreMatchers.is("Nadal")))
                .andExpect(jsonPath("$[1].lastName", CoreMatchers.is("Djokovic")))
                .andExpect(jsonPath("$[2].lastName", CoreMatchers.is("Federer")))
                .andExpect(jsonPath("$[3].lastName", CoreMatchers.is("Murray")));
    }

    @Test
    public void shouldRetrievePlayer() throws Exception {
        // Given
        String playerToRetrieve = "nadal";
        Mockito.when(playerService.getByLastName(playerToRetrieve)).thenReturn(PlayerList.RAFAEL_NADAL);

        // When / Then
        mockMvc.perform(get("/players/nadal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Nadal")))
                .andExpect(jsonPath("$.rank.position", CoreMatchers.is(1)));
    }

    @Test
    public void shouldReturn404NotFound_WhenPlayerDoesNotExist() throws Exception {
        // Given
        String playerToRetrieve = "doe";
        Mockito.when(playerService.getByLastName(playerToRetrieve)).thenThrow(new PlayerNotFoundException(playerToRetrieve));

        // When / Then
        mockMvc.perform(get("/players/doe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorDetails", CoreMatchers.is("Player with last name doe could not be found.")));
    }
}
