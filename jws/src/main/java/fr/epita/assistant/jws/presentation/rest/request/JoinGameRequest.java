package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.domain.entity.GameEntity;
import fr.epita.assistant.jws.domain.entity.PlayerEntity;
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
public class JoinGameRequest {

    @Inject GameService gameService;

    @POST @Path("/{gameId}")
    public GameEntity joinGame(@PathParam("gameId") final int gameId, final PlayerEntity newPlayer){
        GameEntity entity = gameService.getGame(gameId);
        if (entity == null)
            throw new NotFoundException("Game with this ID does not exist");
        else if (entity.players.size() == 4
                || !entity.state.equals("STARTING")
                || newPlayer == null
                || newPlayer.name == null)
            throw new BadRequestException("Cannot join this game");
        else
            return gameService.joinGame(gameId, newPlayer);
    }

}
