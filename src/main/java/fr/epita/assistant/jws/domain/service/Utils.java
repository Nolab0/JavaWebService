package fr.epita.assistant.jws.domain.service;

import fr.epita.assistant.jws.data.model.GameModel;
import fr.epita.assistant.jws.domain.entity.GameEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Utils {

    public static class Position
    {
        int posX;
        int posY;

        public Position(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
        }
    }

    private static String readFile(String path)
    {
        File file = new File(path);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null)
                builder.append(line);
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decodeRle(String path) {
        String content = readFile(path);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            if (i + 1 < content.length() && i % 2 == 0){
                for (int j = 0; j < Integer.parseInt(String.valueOf(content.charAt(i))); j++)
                    builder.append(content.charAt(i + 1));
            }
        }
        return builder.toString();
    }

    public static String encodeRle(String source){
        int i = 0;
        StringBuilder builder = new StringBuilder();
        while (i < source.length()){
            int size = 1;
            while (i + 1 < source.length() && size < 9 && source.charAt(i) == source.charAt(i + 1)){
                i++;
                size++;
            }
            builder.append(size);
            builder.append(source.charAt(i));
            i++;
        }
        return builder.toString();
    }

    public static Position getPos(GameModel model){
        switch (model.players.size()){
            case 0:
                return new Position(1,1);
            case 1:
                return new Position(15,1);
            case 2:
                return new Position(1,13);
            case 3:
                return new Position(15,13);
        }
        return new Position(1,1);
    }

    public static List<String> getMapList(String input){
        ArrayList<String> res = new ArrayList<>();
        int i = 0;
        int j = 17;
        while (j <= input.length()){
            String sub = input.substring(i, j);
            i += 17;
            j += 17;
            res.add(encodeRle(sub));
        }
        return res;
    }

    public static String putBombAt(String map, int x, int y){
        StringBuilder builder = new StringBuilder(map);
        int index = y * 17 + x;
        builder.setCharAt(index, 'B');
        return builder.toString();
    }

    public static boolean isValidMove(String map, int x, int y){
        int index = y * 17 + x;
        return map.charAt(index) == 'G';
    }
}
