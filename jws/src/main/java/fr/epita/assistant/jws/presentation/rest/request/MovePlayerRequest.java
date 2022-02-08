package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.domain.entity.GameEntity;
import fr.epita.assistant.jws.domain.entity.GameState;
import fr.epita.assistant.jws.domain.entity.PlayerEntity;
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
        PlayerEntity player = gameService.getPlayer(gameId, playerId);
        if (entity == null)
            throw new NotFoundException("Game with this Id does not exists");
        if (position == null || position.posX == null || position.posY == null)
            throw new BadRequestException("Null passed in position");
        if (player == null)
            throw new NotFoundException("Player with this Id does not exists");
        if (!entity.state.equals(GameState.RUNNING.toString()))
            throw new BadRequestException("Game not runnnin");
        if (!Utils.validMove(player.posX, player.posY, position))
            throw new BadRequestException("Invalid move");
        return gameService.movePlayer(entity.id, playerId, position);
    }

}
