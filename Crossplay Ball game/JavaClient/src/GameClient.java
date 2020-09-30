
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class GameClient extends BallApp {

    private Socket socket;
    private String server;
    private BufferedReader sInput;
    private PrintWriter sOutput;
    private int port, id, ballHolder;
    boolean ball;

    GameClient(String server, int port) {
        this.server = server;
        this.port = port;
        id = 0;
    }

    boolean start() {
        try {
            ball = false;
            connected = false;

            socket = new Socket(server, port);
            sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sOutput = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception ec) {
            feed.setText("Error connecting to server:" + ec);
            return false;
        }
        feed.setText("\nConnection accepted " + socket.getInetAddress() + " : " + socket.getPort() + "\n");

        // creates the Thread to listen from the server
        new ListenFromServer().start();
        connected = true;
        if (restart)
            id = backUp[0];
        sendMessage("ID " + id);
        return true;
    }

    void display(String msg) {
        try {
            feed.getDocument().insertString(0, "\n" + msg + "\n", null);
            feed.setCaretPosition(0);
        } catch (Exception ignored) {
        }
    }

    void sendMessage(String i) {
        sOutput.println(i);
        sOutput.flush();

        if (i.contains("BALL")) {
            ball = false;
            sendMessage("LIST " + 0);
        }

    }

    private void save() {
        backUp = new int[2];
        backUp[0] = id;
        backUp[1] = ball ? 1 : 0;
    }

    void disconnect() {
        try {
            if (sOutput != null)
                sOutput.close();
            if (socket != null)
                socket.close();

            users.setText("Active Users: \n(offline)");
            feed.setText("You have left the server");
        } catch (Exception ignored) {
        }
    }

    class ListenFromServer extends Thread {

        public synchronized void run() {

            while (true) {
                try {
                    String n = sInput.readLine();
                    if (n != null) {
                        String date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                        String type = n.replaceAll("\\D+", "");
                        int user = Integer.parseInt(type);

                        if (n.contains("ID")) {
                            id = user;
                            frame.setTitle("Ball Game: Your id is " + id);
                            frame.setSize(650, 480);
                            display("> Your ID is : " + id);
                            info.setText("Welcome to the server:" +
                                    "\nIf you have the ball, enter an Id from the list & click Send" +
                                    "\nIf not, wait for it to be sent to you" +
                                    "\n" +
                                    "\nClick Refresh to update the active user list" +
                                    "\nClick Leave or close the app to leave the server");
                        } else if (n.contains("LIST")) {
                            if (user == 0)
                                users.setText("Active Users: \n" + date);
                            else {
                                String userLine = user + "";
                                if (user == id)
                                    userLine += " (You)";
                                if (user == ballHolder)
                                    userLine += " (Ball)";
                                users.append("\n> " + userLine);
                            }
                        } else if (n.contains("BALL")) {
                            ball = true;
                            ballHolder = id;
                            if (user != id) {
                                String userName = user + "";
                                if (user == 0)
                                    userName = "Server";
                                display("> " + date + ": Ball Received from " + userName);
                            } else
                                display("> " + date + ": Ball was returned");
                            display("> " + date + ": You now have the ball");
                            sendMessage("LIST " + 0);
                        } else if (n.contains("JOINED")) {
                            if (user != id)
                                display("> " + date + ": " + user + " has joined the game");
                            sendMessage("LIST " + 0);
                        } else if (n.contains("LEFT")) {
                            display("> " + date + ": " + user + " has left the game");
                            sendMessage("LIST " + 0);
                        } else if (n.contains("WHO")) {
                            ballHolder = user;
                            display("> " + date + ": " + ballHolder + " now has the ball");
                            sendMessage("LIST " + 0);
                        } else if (n.contains("RETURN")) {
                            ball = false;
                            ballHolder = user;
                            sendMessage("LIST " + 0);
                        }
                    }

                } catch (IOException e) {
                    if (connected && !restart) {
                        save();
                        join.doClick();
                        connected = false;
                        restart = true;
                        start.doClick();
                        display("Server has restarted & Game has continued");
                        display("> Your ID is still : " + id);
                        frame.setTitle("Ball Game: Your id is " + id);
                        info.setText("Welcome to the new server:" +
                                "\nIf you have the ball, enter an Id from the list & click Send" +
                                "\nIf not, wait for it to be sent to you" +
                                "\n" +
                                "\nClick Refresh to update the active user list" +
                                "\nClick Leave or close the app to leave the server");
                        frame.repaint();
                    }
                    break;
                }
            }
        }
    }
}

