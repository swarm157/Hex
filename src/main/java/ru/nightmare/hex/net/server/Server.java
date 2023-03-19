package ru.nightmare.hex.net.server;

import javafx.scene.paint.Color;
import ru.nightmare.hex.model.Action;
import ru.nightmare.hex.model.GameStatus;
import ru.nightmare.hex.model.Hex;
import ru.nightmare.hex.model.Player;
import ru.nightmare.hex.net.World;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server implements World {
    protected int port = -1;
    private ArrayList<Socket> clients = null;
    private ServerSocket socket = null;
    private Thread server = null;

    protected GameStatus status = GameStatus.awaiting;


    protected ArrayList<Player> players = null;
    protected Hex[][] world = null;
    private Hex[][][] visions = null;
    private Thread game = null;
    private Thread receiver = null;
    protected int secs = 15;

    private void initWorld() {

    }
    public Server(int width, int height, int playerCount, int port, int resources) throws IOException, InterruptedException {
        players = new ArrayList<>();
        Random rand = new Random(System.currentTimeMillis());
        for(int i = 0; i < playerCount; i++) {
            players.add(new Player(resources, Color.rgb(rand.nextInt(0, 255), rand.nextInt(0, 255), rand.nextInt(0, 255))));
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

            }
        });
        receiver.start();
        server = new Thread(() -> {
            try {
                Thread.sleep(500);
                while(game.isAlive()) {

                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        server.start();
        game = new Thread(new Game());
        game.start();
    }
    @Override
    public GameStatus getGameStatus() throws IOException {
        return null;
    }

    @Override
    public int getPlayersCount() throws IOException {
        return 0;
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
        return 0;
    }

    @Override
    public int getWorldHeight() throws IOException {
        return 0;
    }

    @Override
    public List<Player> getPlayers() throws IOException {
        return null;
    }

    @Override
    public void act(Action action) throws IOException {

    }

    @Override
    public Hex[][] getVision() throws IOException {
        return new Hex[0][];
    }

    @Override
    public boolean join() {
        return false;
    }

    @Override
    public void pause() throws IOException {

    }

    @Override
    public void continue_() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public List<Action> getActions() throws IOException {
        return null;
    }

    @Override
    public void setColor(Color color) throws IOException {

    }

    @Override
    public void setName(String name) throws IOException {

    }
    class Game implements Runnable {
        long currentMillis = 0L;
        @Override
        public void run() {
            while(status!=GameStatus.ended||secs!=0) {
                if(status==GameStatus.paused||status==GameStatus.awaiting) {
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
                }
            }
        }
    }
}
