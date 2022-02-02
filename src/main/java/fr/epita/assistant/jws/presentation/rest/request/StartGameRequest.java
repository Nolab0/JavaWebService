package fr.epita.assistant.jws.presentation.rest.request;


import fr.epita.assistant.jws.domain.entity.GameEntity;
import fr.epita.assistant.jws.domain.service.GameService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StartGameRequest {

    @Inject GameService gameService;

    @PATCH
    @Path("/{gameId}/start")
    public GameEntity startGame(@PathParam("gameId") final int gameId){
        GameEntity entity = gameService.getGame(gameId);
        if (entity == null)
            throw new NotFoundException("Cannot found game with this id");
        else
            return gameService.startGame(gameId);
    }
}
