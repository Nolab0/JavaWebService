package fr.epita.assistant.jws.domain.entity;

import lombok.Value;
import lombok.With;

import java.sql.Timestamp;

@Value @With
public class PlayerEntity {
    public int id;
    public String name;
    public int lives;
    public int posX;
    public int posY;
}
