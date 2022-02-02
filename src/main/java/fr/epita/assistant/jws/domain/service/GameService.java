package fr.epita.assistant.jws.domain.service;

import fr.epita.assistant.jws.data.model.GameMapModel;
import fr.epita.assistant.jws.data.model.GameModel;
import fr.epita.assistant.jws.data.model.PlayerModel;
import fr.epita.assistant.jws.domain.entity.GameEntity;
import fr.epita.assistant.jws.domain.entity.GameState;
import fr.epita.assistant.jws.domain.entity.PlayerEntity;
import lombok.val;
import org.eclipse.microprofile.config.inject.ConfigProperty;


import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameService {
    @ConfigProperty(name="JWS_MAP_PATH") String map_path;
    @ConfigProperty(name="JWS_TICK_DURATION") int tick_duration;
    @ConfigProperty(name="JWS_DELAY_MOVEMENT") int delay_movement;
    @ConfigProperty(name="JWS_DELAY_BOMB") int delay_bomb;
    @ConfigProperty(name="JWS_DELAY_SHRINK") int delay_shrink;
    @ConfigProperty(name="JWS_DELAY_FREE") int delay_free;

    private PlayerEntity modelToEntity(PlayerModel playerModel) {
        return new PlayerEntity(playerModel.id, playerModel.lastBomb, playerModel.lastMovement, playerModel.name, playerModel.lives
                ,playerModel.posX, playerModel.posY, playerModel.gameModel.id);
    }

    @Transactional
    public Set<GameEntity> getGames() {
        return GameModel.<GameModel>findAll()
                .stream()
                .map(gameModel -> new GameEntity(gameModel.startTime, gameModel.state,gameModel.players.stream()
                        .map(this::modelToEntity)
                        .collect(Collectors.toList()), Utils.getMapList(gameModel.gameMapModel.map), gameModel.id))
                .collect(Collectors.toSet());
    }

    public GameEntity getGame(int id){
        GameModel gameModel = GameModel.<GameModel>findById(id);
        if (gameModel == null)
            return null;
        return new GameEntity(gameModel.startTime, gameModel.state,gameModel.players.stream()
                .map(this::modelToEntity)
                .collect(Collectors.toList()), Utils.getMapList(gameModel.gameMapModel.map), gameModel.id);
    }

    @Transactional
    public GameEntity addGame(final PlayerEntity firstPlayer) {
        map_path = map_path.length() > 0 ? map_path : "src/test/resources/map1.rle";
        val mapModel = new GameMapModel()
                .withMap(Utils.decodeRle(map_path));
        val gameModel = new GameModel()
                .withStartTime(new Timestamp(System.currentTimeMillis()))
                .withState(GameState.STARTING.toString())
                .withPlayers(new ArrayList<>())
                .withGameMapModel(mapModel);
        Utils.Position position = Utils.getPos(gameModel);
        val playerModel = new PlayerModel()
                .withName(firstPlayer.name)
                .withPosX(position.posX)
                .withPosY(position.posY)
                .withLives(3)
                .withGameModel(gameModel);
        gameModel.players.add(playerModel);
        PlayerModel.persist(playerModel);
        GameModel.persist(gameModel);
        return new GameEntity(gameModel.startTime, gameModel.state, gameModel.players
                .stream()
                .map(this::modelToEntity).collect(Collectors.toList()), Utils.getMapList(gameModel.gameMapModel.map), gameModel.id);
    }

    @Transactional
    public GameEntity joinGame(int gameId, final PlayerEntity newPlayer){
        val gameModel = GameModel.<GameModel>findById(gameId);
        Utils.Position position = Utils.getPos(gameModel);
        val playerModel = new PlayerModel()
                .withName(newPlayer.name)
                .withPosX(position.posX)
                .withPosY(position.posY)
                .withLives(3)
                .withGameModel(gameModel);
        gameModel.players.add(playerModel);
        GameModel.persist(gameModel);
        PlayerModel.persist(playerModel);
        return new GameEntity(gameModel.startTime, gameModel.state, gameModel.players
                .stream()
                .map(this::modelToEntity).collect(Collectors.toList()), Utils.getMapList(gameModel.gameMapModel.map), gameModel.id);
    }

    @Transactional
    public GameEntity startGame(int gameId){
        val gameModel = GameModel.<GameModel>findById(gameId);
        gameModel.state = GameState.RUNNING.toString();
        GameModel.persist(gameModel);
        return new GameEntity(gameModel.startTime, gameModel.state, gameModel.players
                .stream()
                .map(this::modelToEntity).collect(Collectors.toList()), Utils.getMapList(gameModel.gameMapModel.map), gameModel.id);
    }

    @Transactional
    public GameEntity putBomb(int gameId, int playerId, Utils.Position position){
        val gameModel = GameModel.<GameModel>findById(gameId);
        val playerModel = PlayerModel.<PlayerModel>findById(playerId);
        if (playerModel == null)
            return null;
        if (playerModel.posX != position.posX || playerModel.posY != position.posY)
            return null;
        gameModel.gameMapModel.map = Utils.putBombAt(gameModel.gameMapModel.map, position.posX, position.posY);
        GameModel.persist(gameModel);
        return new GameEntity(gameModel.startTime, gameModel.state, gameModel.players
                .stream()
                .map(this::modelToEntity).collect(Collectors.toList()), Utils.getMapList(gameModel.gameMapModel.map), gameModel.id);
    }

    @Transactional
    public GameEntity movePlayer(int gameId, int playerId, Utils.Position position) {
        val gameModel = GameModel.<GameModel>findById(gameId);
        val playerModel = PlayerModel.<PlayerModel>findById(playerId);
        if (playerModel.lives == 0)
            throw new BadRequestException("Player already dead");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (playerModel.lastMovement != null && now.getTime() - playerModel.lastMovement.getTime() < tick_duration * delay_movement)
            throw new BadRequestException("cant move");
        if (!Utils.isValidMove(gameModel.gameMapModel.map, position.posX, position.posY))
            throw new BadRequestException("Invalid move");
        playerModel.posX = position.posX;
        playerModel.posY = position.posY;
        playerModel.lastMovement = now;
        PlayerModel.persist(playerModel);
        GameModel.persist(gameModel);
        return new GameEntity(gameModel.startTime, gameModel.state, gameModel.players
                .stream()
                .map(this::modelToEntity).collect(Collectors.toList()), Utils.getMapList(gameModel.gameMapModel.map), gameModel.id);
    }
}
