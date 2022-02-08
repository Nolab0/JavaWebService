package fr.epita.assistant.jws.presentation.rest.response;

import fr.epita.assistant.jws.domain.entity.GameEntity;
import fr.epita.assistant.jws.domain.service.GameService;
import lombok.Value;
import lombok.With;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameListResponse {

    @Inject GameService gameService;

    @GET
    public Set<GameSummaryDTO> findAllGames() {
        // System.out.println("Get Game list");
        return gameService.getGames().stream().map(gameEntity -> new GameSummaryDTO(gameEntity.id, gameEntity
                .players.size(), gameEntity.state))
                .collect(Collectors.toSet());
    }

    @With @Value
    private class GameSummaryDTO
    {
        public int id;
        public int players;
        public String state;
    }
}
