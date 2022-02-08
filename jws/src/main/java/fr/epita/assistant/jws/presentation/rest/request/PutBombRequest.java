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
public class PutBombRequest {

    @Inject GameService gameService;
    @POST @Path("/{gameId}/players/{playerId}/bomb")
    public GameEntity putBomb(@PathParam("gameId") final int gameId,
                              @PathParam("playerId") final int playerId,
                              Utils.Position position)
    {
        GameEntity entity = gameService.getGame(gameId);
        PlayerEntity player = gameService.getPlayer(gameId, playerId);
        if (entity == null)
            throw new NotFoundException("Game with this Id does not exists");
        if (player == null)
            throw new NotFoundException("Player with this Id does not exists");
        if (!entity.state.equals(GameState.STARTING.toString()))
            throw new BadRequestException("Game finished");
        if (position.posX != player.posX || position.posY != player.posY)
            throw new BadRequestException("Wrong bomb position");
        return gameService.putBomb(gameId, playerId, position);
    }
}