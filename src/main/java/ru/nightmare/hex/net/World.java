package ru.nightmare.hex.net;

import javafx.scene.paint.Color;
import ru.nightmare.hex.model.Action;
import ru.nightmare.hex.model.GameStatus;
import ru.nightmare.hex.model.Hex;
import ru.nightmare.hex.model.Player;

import java.io.IOException;
import java.util.List;

public interface World {
    GameStatus getGameStatus()  throws IOException;
    int getPlayersCount()  throws IOException;
    int getMyId()  throws IOException;
    int getSecondsBeforeStart()  throws IOException;
    int getWorldWidth()  throws IOException;
    int getWorldHeight()  throws IOException;
    List<Player> getPlayers()  throws IOException;
    void act(Action action) throws IOException;
    Hex[][] getVision() throws IOException;
    boolean join();
    void pause() throws IOException;
    void continue_() throws IOException;
    void close() throws IOException;
    List<Action> getActions() throws IOException;
    void setColor(Color color) throws IOException;
    void setName(String name) throws IOException;
}
