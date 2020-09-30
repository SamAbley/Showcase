import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ButtonHandler extends BallApp implements ActionListener {
    private int type;
    private String date;

    ButtonHandler(int type) {
        this.type = type;
        date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public void actionPerformed(ActionEvent e) {
        if (type == 1) {
            if (connected)
                if (client.ball) {
                    try {
                        int msg = Integer.parseInt(input.getText());
                        if (msg > 0) {
                            client.sendMessage("BALL " + msg);
                            client.ball = false;
                            client.display("> " + date + " Ball sent to " + msg);
                        } else
                            client.display("> " + date + " Cannot send " + msg);
                    } catch (Exception r) {
                        client.display("> " + date + " Cannot send to " + input.getText());
                    }
                } else
                    client.display("> " + date + " Cannot send as you do not have the ball");
            else
                JOptionPane.showMessageDialog(null, "Please login first", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        if (type == 2) {
            if (connected)
                client.sendMessage("LIST " + 0);
            else
                JOptionPane.showMessageDialog(null, "Please login first", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        if (type == 3) {
            if (connected) {
                connected = false;
                refresh.setVisible(false);
                panel.setVisible(false);
                frame.setTitle("Ball Game");
                info.setText("Welcome to the Game. First player to join gets the ball" +
                        "\nBegin by choosing one of the server options:" +
                        "\nStart: starts a new server on Localhost" +
                        "\nJoin: joins a server by providing an IP address (Or leave \"localhost\")");
                join.setText("Join");
                client.disconnect();
                start.setVisible(true);
            } else {
                serverAddress = JOptionPane.showInputDialog("Enter the server address: (default localhost)", "localhost");
                if (serverAddress == null) return;
                client = new GameClient(serverAddress, portNumber);
                if (client.start()) {
                    refresh.setVisible(true);
                    panel.setVisible(true);
                    join.setText("Leave");
                    client.sendMessage("LIST " + 0);
                    connected = true;
                    start.setVisible(false);
                } else
                    JOptionPane.showMessageDialog(null, "Cannot connect to " + serverAddress, "Connection Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (type == 4) {

            if (restart) {
                client = new GameClient("localhost", 1500);
                try {
                    Runtime rt = Runtime.getRuntime();
                    rt.exec("cmd /c start cmd.exe /K \"java -jar JavaServer.jar\"");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (client.start()) {
                    if (backUp[1] == 1)
                        client.sendMessage("RETURN " + backUp[0]);
                    restart = false;
                }
                client.sendMessage("LIST " + 0);
                join.setText("Leave");
                refresh.setVisible(true);
                panel.setVisible(true);
                connected = true;
                start.setVisible(false);

            } else if (!connected) {
                client = new GameClient(serverAddress, portNumber);
                try {
                    Runtime rt = Runtime.getRuntime();
                    rt.exec("cmd /c start cmd.exe /K \"java -jar JavaServer.jar\"");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                client.start();
                client.sendMessage("LIST " + 0);
                join.setText("Leave");
                refresh.setVisible(true);
                panel.setVisible(true);
                connected = true;
                start.setVisible(false);
            }
        }
    }
}



