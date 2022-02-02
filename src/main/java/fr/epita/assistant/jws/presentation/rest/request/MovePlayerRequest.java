package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.domain.entity.GameEntity;
import fr.epita.assistant.jws.domain.service.GameService;
import fr.epita.assistant.jws.domain.service.Utils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovePlayerRequest {

    @Inject
    GameService gameService;
    @POST
    @Path("/{gameId}/players/{playerId}/move")
    public GameEntity movePlayer(@PathParam("gameId") final int gameId,
                                 @PathParam("playerId") final int playerId,
                                 Utils.Position position)
    {
        GameEntity entity = gameService.getGame(gameId);
        return gameService.movePlayer(entity.id, playerId, position);
    }

}
