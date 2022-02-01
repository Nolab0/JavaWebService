package fr.epita.assistant.jws.data.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.*;

@Entity
@Table(name = "game")
@AllArgsConstructor @NoArgsConstructor @With @ToString
public class GameMapModel {
    @Id public int gameModelId;
    public @Column(name = "map") String map;
    public @OneToOne @PrimaryKeyJoinColumn GameModel gameModel;
}
