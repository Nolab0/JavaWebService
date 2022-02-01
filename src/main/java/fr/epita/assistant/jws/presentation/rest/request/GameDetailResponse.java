package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.domain.entity.GameEntity;
import fr.epita.assistant.jws.domain.service.GameService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Objects;

@ApplicationScoped
@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameDetailResponse {

    @Inject GameService gameService;

    @GET
    @Path("/{gameId}")
    public GameEntity getGameDetails(@PathParam("gameId") final int gameId){
        return gameService.getGames()
                .stream()
                .filter(gameEntity -> Objects.equals(gameEntity.id, gameId))
                .findFirst()
                .orElse(null);
    }
}
