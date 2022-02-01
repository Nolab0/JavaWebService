package fr.epita.assistant.jws.data.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity @Table(name = "player")
@AllArgsConstructor @NoArgsConstructor @With @ToString
public class PlayerModel extends PanacheEntityBase{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public int id;
    public @Column(name = "last_bomb") Timestamp lastBomb;
    public @Column(name = "last_movement") Timestamp lastMovement;
    public int lives;
    public String name;
    public @Column(name = "pos_x") int posX;
    public @Column(name = "pos_y") int posY;
    public @ManyToOne GameModel gameModel;
}
