package ru.nightmare.hex.net.server;

import javafx.scene.paint.Color;
import ru.nightmare.hex.controller.component.Util;
import ru.nightmare.hex.model.*;
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
    protected int secs = 5;

    protected int width;
    protected int height;

    private void initWorld() {
        world = new Hex[width][height];
        for(int i = 0; i < width; i++) {
            world[i] = new Hex[height];
            for(int o = 0; o < height; o++) {
                Hex hex= new Hex();
                world[i][o] = hex;
            }
        }
        Hex[][] matrix = world; //здесь создаем пустую матрицу 10x10
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j].setBiome(Biomes.values()[random.nextInt(0, Biomes.values().length-1)]);
            }
        }

        /*world = new Hex[width][height];
        HashSet<Hex> emptyHex = new HashSet<>();
        for(int i = 0; i < width; i++) {
            world[i] = new Hex[height];
            for(int o = 0; o < height; o++) {
                Hex hex= new Hex();
                emptyHex.add(hex);
                world[i][o] = hex;
            }
        }
        HashSet<Hex> delete = new HashSet<>();
        Random random = new Random(System.currentTimeMillis());
        while (!emptyHex.isEmpty()) {
            Hex hex = (Hex) emptyHex.toArray()[random.nextInt(0, emptyHex.size()-1)];
            int x, y;
            Biomes biomes = Biomes.values()[random.nextInt(0, Biomes.values().length)];
            found: for(int i = 0; i < width; i++) {
                for(int o = 0; o < height; o++) {
                    if (world[i][o].equals(hex)) {
                        x=i;
                        y=o;
                        break found;
                    }
                }
            }


            emptyHex.remove(delete);
        }*/

    }
    public Server(int width, int height, int playerCount, int port, int resources) throws IOException, InterruptedException {
        this.width = width;
        this.height = height;
        players = new HashMap<>();
        Random rand = new Random(System.currentTimeMillis());
        players.put(new Player(resources, Color.rgb(rand.nextInt(0, 255), rand.nextInt(0, 255), rand.nextInt(0, 255))), new Socket());
        for(int i = 0; i < playerCount-1; i++) {
            players.put(new Player(resources, Color.rgb(rand.nextInt(0, 255), rand.nextInt(0, 255), rand.nextInt(0, 255))), null);
        }
        this.port = port;
        initWorld();
        receiver = new Thread(() -> {
            try {
                socket = new ServerSocket(port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
                    if (status!=GameStatus.ended)
                        throw new RuntimeException(e);
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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
                                            if(visions!=null)
                                                out.writeUTF(Util.gson.toJson(visions[player.getId()]));
                                            else
                                                out.writeUTF(Util.gson.toJson(world));
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
                //throw new RuntimeException(e);
            }
        });

        game = new Thread(new Game());

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
        return secs;
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
        if ((status.equals(GameStatus.playing)||status.equals(GameStatus.paused))) {
            if (visions!=null&&visions[0].length>0)
            return visions[0];
            else return world;
        } else return world;
    }

    @Override
    public boolean join() {
        receiver.start();
        server.start();
        game.start();
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
        if (socket!=null)
            socket.close();
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
