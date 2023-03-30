package ru.nightmare.hex.net.server;

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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements World {
    protected int port = -1;
    private ArrayList<Socket> clients = null;
    private ServerSocket socket = null;
    private Thread server = null;

    protected GameStatus status = GameStatus.awaiting;


    HashMap<Player, Socket> players = null;


    ArrayList<Action> acts = new ArrayList<>();
    protected Hex[][] world = null;
    private Hex[][][] visions = null;
    private Thread game = null;
    private Thread receiver = null;
    protected int secs = 15;

    protected int width;
    protected int height;

    private void initWorld() {

    }
    public Server(int width, int height, int playerCount, int port, int resources) throws IOException, InterruptedException {
        this.width = width;
        this.height = height;
        players = new HashMap<>();
        Random rand = new Random(System.currentTimeMillis());
        for(int i = 0; i < playerCount; i++) {
            players.put(new Player(resources, Color.rgb(rand.nextInt(0, 255), rand.nextInt(0, 255), rand.nextInt(0, 255))), null);
        }
        this.port = port;
        socket = new ServerSocket(port);
        initWorld();
        receiver = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while(game.isAlive()) {
                try {
                    Socket client = socket.accept();
                    for(Player player : players.keySet()) {
                        if(player.getId()!=0)
                        if (players.get(player)==null||players.get(player).isClosed()) {
                            players.replace(player, client);
                            break;
                        }
                    }
                    if (client!=null&&!players.containsValue(client)) client.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        receiver.start();
        server = new Thread(() -> {
            try {
                Thread.sleep(500);
                while(game.isAlive()) {
                    if (status!=GameStatus.playing) {
                        Thread.sleep(20);
                    } else {
                        Thread.sleep(1);
                    }
                    for (Player player : players.keySet()) {
                        if(player.getId()==0) continue;
                        Socket client = players.get(player);
                        if (client!=null&&!client.isClosed()) {
                            if(client.getInputStream().available()>0) {
                                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                                DataInputStream in = new DataInputStream(client.getInputStream());
                                String request = in.readUTF();
                                if (request.startsWith("name")) {
                                    player.setName(request.split(":")[1]);
                                } else if(request.startsWith("color")) {
                                    player.setColor(Util.gson.fromJson(request.split(":")[1], Color.class));
                                } else if(request.startsWith("act")) {
                                    Action action = Util.gson.fromJson(request.split(":")[1], Action.class);
                                    for (Action act : acts) {
                                        if (act.getExecutor().equals(action.getExecutor())) {
                                            acts.remove(act);
                                        }
                                    }
                                    acts.add(action);

                                } else {
                                    switch (request) {
                                        case "status":
                                            out.writeUTF(Util.gson.toJson(status));
                                            break;
                                        case "playerCount":
                                            out.writeInt(players.size());
                                            break;
                                        case "id":
                                            out.writeInt(player.getId());
                                            break;
                                        case "seconds":
                                            out.writeInt(secs);
                                            break;
                                        case "width":
                                            out.writeInt(width);
                                            break;
                                        case "height":
                                            out.writeInt(height);
                                            break;
                                        case "players":
                                            out.writeUTF(Util.gson.toJson(players));
                                            break;
                                        case "vision":
                                            out.writeUTF(Util.gson.toJson(visions[player.getId()]));
                                            break;
                                        default:
                                            out.writeUTF("wrong request");
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        server.start();
        game = new Thread(new Game());
        game.start();
    }
    @Override
    public GameStatus getGameStatus() throws IOException {
        return status;
    }

    @Override
    public int getPlayersCount() throws IOException {
        return players.size();
    }

    @Override
    public int getMyId() throws IOException {
        return 0;
    }

    @Override
    public int getSecondsBeforeStart() throws IOException {
        return 0;
    }

    @Override
    public int getWorldWidth() throws IOException {
        return width;
    }

    @Override
    public int getWorldHeight() throws IOException {
        return height;
    }

    @Override
    public List<Player> getPlayers() throws IOException {
        return players.keySet().stream().toList();
    }

    @Override
    public void act(Action action) throws IOException {
        for (Action act : acts) {
            if (act.getExecutor().equals(action.getExecutor())) {
                acts.remove(act);
            }
        }
        acts.add(action);
    }

    @Override
    public Hex[][] getVision() throws IOException {
        return new Hex[0][];
    }

    @Override
    public boolean join() {
        return true;
    }

    @Override
    public void pause() throws IOException {
        status = GameStatus.paused;
    }

    @Override
    public void continue_() throws IOException {
        status = GameStatus.playing;
    }

    @Override
    public void close() throws IOException {
        status = GameStatus.ended;
    }

    @Override
    public List<Action> getActions() throws IOException {
        return acts;
    }

    @Override
    public void setColor(Color color) throws IOException {
        for (Player player : players.keySet()) {
            player.setColor(color);
            break;
        }
    }

    @Override
    public void setName(String name) throws IOException {
        for (Player player : players.keySet()) {
            player.setName(name);
            break;
        }
    }
    class Game implements Runnable {
        long currentMillis = 0L;
        @Override
        public void run() {
            while(status!=GameStatus.ended||secs!=0) {
                if(status==GameStatus.paused) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if(status==GameStatus.starting) {
                    try {
                        Thread.sleep(1000);
                        if (secs>0) secs--;
                        else {status=GameStatus.playing;secs=10;}
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if(status==GameStatus.playing) {
                    if(System.currentTimeMillis()>currentMillis+500) {
                        currentMillis=System.currentTimeMillis();

                    } else {
                        try {
                            Thread.sleep((currentMillis+550)-System.currentTimeMillis());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else if(status==GameStatus.ended) {
                    try {
                        Thread.sleep(1000);
                        if (secs>0) secs--;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (status==GameStatus.awaiting) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    boolean ready = true;
                    for (Player player : players.keySet()) {
                        if (players.get(player)==null || players.get(player).isClosed()) {
                            ready = false;
                            break;
                        }
                    }
                    if (ready) status = GameStatus.starting;
                }
            }
        }
    }
}
