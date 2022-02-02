package fr.epita.assistant.jws.data.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.*;

@Entity
@Table(name = "map")
@AllArgsConstructor @NoArgsConstructor @With @ToString
public class GameMapModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public int gameMapId;
    public String map;
    public @OneToOne @PrimaryKeyJoinColumn GameModel gameModel;
}
