import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GameServer {

    private static int uniqueId;
    private ArrayList<Client> players;
    private int port;
    private int from = 0;
    private int to = 0;
    private String date;
    private boolean running;

    private GameServer(int port) {
        this.port = port;
        players = new ArrayList<>();
    }

    public static void main(String[] args) {
        GameServer server = new GameServer(1500);
        server.start();
    }

    private void start() {
        running = true;
        Timer timer = new Timer();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (running) {
                display("GameServer waiting for players on port " + port);
                if (!running)
                    break;
                Socket socket = serverSocket.accept();

                Client ct = new Client(socket);
                players.add(ct);
                ct.start();



                if (players.size() == 1) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (who() == 0) {
                                ct.sendMessage("BALL " + 0);
                                ct.ball = true;
                                display("The Ball Has been sent from the Server to " + ct.id);
                            }
                        }
                    }, 2000);
                }
            }

            try {
                serverSocket.close();
                for (Client ct : players) {
                    try {
                        ct.sInput.close();
                        ct.sOutput.close();
                        ct.socket.close();
                    } catch (IOException ignored) {
                    }
                }
            } catch (Exception e) {
                display("Exception closing the server and clients: " + e);
            }
        } catch (IOException e) {
            display(" Exception on new ServerSocket: " + e);
        }
    }
    public void stop(){
        running = false;
    }

    //Display server events
    private void display(String msg) {
        date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println(date + ": " + msg + "\n");
    }

    //Send messages to users
    private synchronized boolean broadcast(String w) {
        date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        String type = w.replaceAll("\\D+", "");
        int user = Integer.parseInt(type);

        if (w.contains("BALL")) {

            if (user == 0) { //Check if there is a user left to send the ball to
                lastPlayer(from);
                return true;
            } else if (user == -1) //if no users left
                return true;
            boolean found = false;

            for (Client ct : players)
                if (user == ct.id) {
                    display("The ball is being sent from " + from + " to " + to);
                    if (!ct.sendMessage("BALL " + from)) {
                        players.remove(ct);
                        display("A disconnected client was removed from the game");
                        break;
                    }
                    found = true;
                    ct.ball = true;
                }
            return found;
        } else if (w.contains("JOINED")) // joined notification
        {
            display(user + " has joined");

            for (Client ct : players)
                if (!ct.sendMessage(w)) {
                    players.remove(ct);
                    display("A disconnected client was removed from the game");
                    break;
                }
            displayUsers();
        } else if (w.contains("LEFT")) // joined notification
        {
            display(user + " has left");
            for (Client ct : players)
                if (!ct.sendMessage(w)) {
                    players.remove(ct);
                    display("A disconnected client was removed from the game");
                    break;
                }
            displayUsers();

        } else if (w.contains("WHO")) // who has the ball notification
        {
            display(user + " has the ball");
            for (Client ct : players)
                if (user != ct.id)
                    if (!ct.sendMessage(w)) {
                        players.remove(ct);
                        display("A disconnected client was removed from the game");
                        break;
                    }
        }
        return false;
    }


    //Removes client from list
    private synchronized void remove(int id) {
        for (Client ct : players)
            if (ct.id == id) {
                if (ct.ball) lastPlayer(id);
                players.remove(ct);
                broadcast("LEFT " + ct.id);
                break;
            }
    }

    private void displayUsers() {
        int[] dis = new int[players.size()];
        int n = 0;
        for (Client ct : players) {
            dis[n] = ct.id;
            n++;
        }
        display("Active Users : " + Arrays.toString(dis));
    }

    //Check for last player
    private synchronized void lastPlayer(int id) {
        int lastClient = -1;
        for (Client ct : players)
            if (id != ct.id) {
                lastClient = ct.id;
                break;
            }
        broadcast("BALL " + lastClient);
    }

    //Check if user exists
    private synchronized boolean exist(int id) {
        for (Client ct : players)
            if (id == ct.id)
                return true;
        return false;
    }

    //Who has the ball
    private int who() {
        for (Client ct : players)
            if (ct.ball)
                return ct.id;
        return 0;
    }


    // One instance of this thread will run for each client
    class Client extends Thread {
        private Socket socket;
        private BufferedReader sInput;
        private PrintWriter sOutput;
        private int id;
        private boolean ball;

        Client(Socket socket) {
            this.socket = socket;
            try {
                sOutput = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                display("Exception creating new Input/output Streams: " + e);
            }
        }

        public synchronized void run() {

            while (true) {
                try {
                    String n = sInput.readLine();
                    String type = n.replaceAll("\\D+", "");
                    int user = Integer.parseInt(type);

                    if (n.contains("BALL")) {
                        to = user;
                        from = id;
                        boolean confirmation = broadcast("BALL " + to);
                        if (from != to)
                            ball = false;
                        if (!confirmation) {
                            ball = true;
                            sendMessage("BALL " + id);
                            display("Ball was returned to " + id);
                        }
                    } else if (n.contains("LIST")) {
                        sendMessage("LIST " + 0);
                        for (Client ct : players)
                            sendMessage("LIST " + ct.id);
                    } else if (n.contains("ID")) {
                        if (user == 0) {
                            while (exist(uniqueId + 1)) {
                                ++uniqueId;
                            }
                            id = ++uniqueId;
                            sendMessage("ID " + id);
                            if (who() != 0)
                                sendMessage("WHO " + who());
                            broadcast("JOINED " + id);
                        } else {
                            id = user;
                            if (uniqueId < id)
                                uniqueId = id;
                            broadcast("JOINED " + id);
                            if (who() != 0)
                                sendMessage("WHO " + who());
                        }
                    } else if (n.contains("RETURN")) {
                        if (who() != 0) {
                            Client ct = players.get(who());
                            ct.ball = false;
                            ct.sendMessage("Return" + user);
                        }
                        ball = true;
                        sendMessage("BALL " + user);
                    }
                } catch (Exception e) {
                    break;
                }
            }
            if (id != 0)
                remove(id);
            close();
        }

        private synchronized void close() {
            try {
                if (sOutput != null) sOutput.close();
                if (sInput != null) sInput.close();
                if (socket != null) socket.close();
            } catch (Exception ignored) {
            }
        }

        private boolean sendMessage(String i) {
            if (!socket.isConnected()) {
                close();
                return false;
            }
            sOutput.println(i);
            sOutput.flush();
            if (i.contains("BALL"))
                broadcast("WHO " + id);
            return true;
        }
    }
}

