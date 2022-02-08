package fr.epita.assistant.jws.data.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity @Table(name = "game")
@AllArgsConstructor @NoArgsConstructor @With @ToString
public class GameModel extends PanacheEntityBase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public int id;
    public @Column(name = "start_time") Timestamp startTime;
    public String state;
    public @OneToMany List<PlayerModel> players;
    public @OneToOne @MapsId GameMapModel gameMapModel;
}
