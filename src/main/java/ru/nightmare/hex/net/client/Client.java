package ru.nightmare.hex.net.client;

import javafx.scene.paint.Color;
import ru.nightmare.hex.controller.component.Util;
import ru.nightmare.hex.model.Action;
import ru.nightmare.hex.model.GameStatus;
import ru.nightmare.hex.model.Hex;
import ru.nightmare.hex.model.Player;
import ru.nightmare.hex.net.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Client implements World {
    protected String ip;
    protected Integer port;

    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    public Client(String IP, Integer PORT) {
        ip = IP;
        port = PORT;
    }

    @Override
    public GameStatus getGameStatus() throws IOException {
        out.writeUTF("status");
        return Util.gson.fromJson(in.readUTF(), GameStatus.class);
    }

    @Override
    public int getPlayersCount() throws IOException {
        out.writeUTF("playerCount");
        return in.readInt();
    }

    @Override
    public int getMyId() throws IOException {
        out.writeUTF("id");
        return in.readInt();
    }

    @Override
    public int getSecondsBeforeStart() throws IOException {
        out.writeUTF("seconds");
        return in.readInt();
    }

    @Override
    public int getWorldWidth() throws IOException {
        out.writeUTF("width");
        return in.readInt();
    }

    @Override
    public int getWorldHeight() throws IOException {
        out.writeUTF("height");
        return in.readInt();
    }

    @Override
    public List<Player> getPlayers() throws IOException {
        out.writeUTF("players");
        return Util.gson.fromJson(in.readUTF(), ArrayList.class);
    }

    @Override
    public void act(Action action) throws IOException {
        out.writeUTF(Util.gson.toJson(action));
    }

    @Override
    public Hex[][] getVision() throws IOException {
        out.writeUTF("vision");
        return Util.gson.fromJson(in.readUTF(), Hex[][].class);
    }

    @Override
    public boolean join() {
        try {
            socket = new Socket(ip, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public void pause() throws IOException {
        out.writeUTF("pause");
    }

    @Override
    public void continue_() throws IOException {
        out.writeUTF("continue");
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public List<Action> getActions() throws IOException {
        out.writeUTF("acts");
        String answer = in.readUTF();
        List<Action> acts = Util.gson.fromJson(answer, ArrayList.class);
        return acts;
    }

    @Override
    public void setColor(Color color) throws IOException {
        out.writeUTF("color:"+Util.gson.toJson(color));
    }

    @Override
    public void setName(String name) throws IOException {
        out.writeUTF("name:"+name);
    }

}
