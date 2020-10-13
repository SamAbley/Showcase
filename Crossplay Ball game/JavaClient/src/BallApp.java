import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


class BallApp {
    static GameClient client;
    static String serverAddress;
    static int portNumber;
    static JFrame frame;
    static JTextArea info, feed, users;
    static JButton start, join, refresh;
    static JTextField input;
    static boolean connected, restart;
    static JPanel panel;
    static int[] backUp;

    public static void main(String args[]) {
        //Server connection
        portNumber = 1500;
        serverAddress = "localhost";
        connected = false;
        restart = false;

        //Frame
        frame = new JFrame("Ball Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 480);
        frame.setTitle("Ball Game");

        //Instructions
        info = new JTextArea("Welcome to the Game. First player to join gets the ball" +
                "\nTo Begin by choosing a server option:" +
                "\nStart: starts a new server on Localhost" +
                "\nJoin: joins a server by asking you for an address (Or leave \"localhost\")");
        info.setEditable(false);

        //Label server
        Label serverLabel = new Label("Server: ");

        //Start server button
        start = new JButton("Start");
        start.addActionListener(new ButtonHandler(4));

        //Join server button
        join = new JButton("Join");
        join.addActionListener(new ButtonHandler(3));

        //Send ball interface
        JLabel label = new JLabel("Enter ID:");
        input = new JTextField(10); // accepts upto 10 characters
        input.setVisible(true);
        JButton send = new JButton("Send");
        send.addActionListener(new ButtonHandler(1));

        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(label);
        panel.add(input);
        panel.add(send);
        panel.setVisible(false);

        //Active user button & list
        refresh = new JButton("Refresh");
        refresh.addActionListener(new ButtonHandler(2));
        refresh.setVisible(false);

        users = new JTextArea("Active Users: \n(offline)", 15, 10);
        users.setEditable(false);
        JScrollPane scrollUsers = new JScrollPane(users,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //Game feed
        feed = new JTextArea(15, 45);
        feed.setEditable(false);
        JScrollPane scrollFeed = new JScrollPane(feed,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //Main panel
        JPanel main = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        main.setLayout(new GridBagLayout());

        //Instructions
        c.weighty = 1.0;
        c.gridwidth = 3;
        c.gridheight = 3;

        c.gridx = 1;
        c.gridy = 0;
        main.add(info, c);

        //Rest
        c.weighty = 1;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;

        //Server Label
        c.gridx = 4;
        c.gridy = 0;
        main.add(serverLabel, c);


        //Start button
        c.gridx = 4;
        c.gridy = 1;
        main.add(start, c);

        //Join/leave button
        c.weightx = 0;
        c.gridx = 4;
        c.gridy = 2;
        main.add(join, c);

        //Send id input panel
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 3;
        main.add(panel, c);

        //Refresh users list button
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        main.add(refresh, c);

        //Active users list
        c.gridwidth = 1;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 4;
        main.add(scrollUsers, c);

        //Ball feed
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 4;
        main.add(scrollFeed, c);

        //Frame events if closed
        frame.getContentPane().add(main);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (connected) {
                    client.sendMessage("Return");
                    client.disconnect();
                }
            }
        });
    }
}
