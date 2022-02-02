package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.domain.entity.GameEntity;
import fr.epita.assistant.jws.domain.entity.PlayerEntity;
import fr.epita.assistant.jws.domain.service.GameService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@ApplicationScoped
@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CreateGameRequest {
    @Inject GameService gameService;

    @POST
    public GameEntity createGame(final PlayerEntity firstPlayer) {
        if (firstPlayer == null || firstPlayer.name == null)
            throw new BadRequestException("Bad request (request or name is null)");
        // System.out.println("Create Game with Player " + firstPlayer.name);
        return gameService.addGame(firstPlayer);
    }

}
