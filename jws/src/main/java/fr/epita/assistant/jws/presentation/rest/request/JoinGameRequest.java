package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.domain.entity.GameEntity;
import fr.epita.assistant.jws.domain.entity.GameState;
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
    public GameEntity joinGame(@PathParam("gameId") final Integer gameId, final PlayerEntity newPlayer){
        GameEntity entity = gameService.getGame(gameId);
        if (gameId == null)
            throw new BadRequestException("gameId is null");
        if (entity == null)
            throw new NotFoundException("Game with this ID does not exist");
        if (newPlayer == null || newPlayer.name == null)
            throw new BadRequestException("join null");
        if (!entity.state.equals(GameState.STARTING.toString()))
            throw new BadRequestException("Game invalid");
        if (entity.players.size() >= 4)
            throw new BadRequestException("Too much player");
        else
            return gameService.joinGame(gameId, newPlayer);
    }

}
