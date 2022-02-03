package fr.epita.assistant.jws.presentation.rest.response;

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

    @GET @Path("/{gameId}")
    public GameEntity getGameDetails(@PathParam("gameId") final int gameId){
        // System.out.println("Get details of game " + gameId);
        GameEntity entity = gameService.getGame(gameId);
        if (entity == null)
            throw new NotFoundException("Cannot found game with this id");
        else {
            GameService.gameId = entity.id;
            return entity;
        }
    }
}
