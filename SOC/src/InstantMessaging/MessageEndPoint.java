package InstantMessaging;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static AWS.Users.user;
import static AWS.Users.users;

@ServerEndpoint(value = "/{username}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public final class MessageEndPoint {


    private static HashMap<Integer, Map<String, String>> userList;
    public static HashMap<Integer, int[]> notifications = new HashMap<>();


    @OnOpen
    public void onOpen(@PathParam(MessageConstants.USER_NAME_KEY) final Integer userName, final Session session) {
        if (userName == 0) {
            throw new RegistrationFailedException("User name is required");
        } else {
            session.getUserProperties().put(MessageConstants.USER_NAME_KEY, userName);
            if (MessageManager.register(session, userName)) {
                System.out.printf("Session opened for %s\n", userName);

                if (!notifications.containsKey(userName)) {
                    notifications.put(userName, new int[0]);
                } else {
                    String nots = getNots(userName);
                    Message mes = new Message(0, nots, "Not", userName);
                    try {
                        session.getBasicRemote().sendObject(mes);
                    } catch (IOException | EncodeException e) {
                        e.printStackTrace();
                    }
                }

                MessageManager.broadcast(new Message((Integer) session.getUserProperties().get(MessageConstants.USER_NAME_KEY), "***Signed In***", "On", (Integer) session.getUserProperties().get(MessageConstants.RECIPIENT_KEY)), session);
            } else {
                throw new RegistrationFailedException("Already signed in elsewhere");
            }
        }
    }

    @OnError
    public void onError(final Session session, final Throwable throwable) {
        if (throwable instanceof RegistrationFailedException) {
            MessageManager.close(session, throwable.getMessage());
        }
    }

    @OnMessage
    public void onMessage(final Message message) {
        MessageManager.publish(message);
    }

    @OnClose
    public void onClose(final Session session) {
        if (MessageManager.remove(session, (Integer) session.getUserProperties().get(MessageConstants.USER_NAME_KEY))) {
            System.out.printf("Session closed for %s\n", session.getUserProperties().get(MessageConstants.USER_NAME_KEY));

            MessageManager.broadcast(new Message((Integer) session.getUserProperties().get(MessageConstants.USER_NAME_KEY), "***Signed Out***", "Off", (Integer) session.getUserProperties().get(MessageConstants.RECIPIENT_KEY)), session);
        }
    }

    private static final class RegistrationFailedException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public RegistrationFailedException(final String message) {
            super(message);
        }
    }

    String getNots(Integer user) {
        StringBuilder sb = new StringBuilder();
        int[] inbox = MessageEndPoint.notifications.get(user);
        if (inbox != null) {
            for (int not : inbox) {
                sb.append(not);
                sb.append(" ");
            }

        }
        return sb.toString();
    }

    public static HashMap<Integer, Map<String, String>> getUserList() {
        return userList;
    }

    public static void setUserList(HashMap<Integer, Map<String, String>> map) {
        userList = map;
    }


    public static HashMap<Integer, Map<String, String>> getUsers(String user) { //hashmap uid, name, url
        HashMap<Integer, Map<String, String>> result = new HashMap<>();
        ResultSet rs = null;
        if (user != null) {
            rs = user(user);
        } else
            rs = users();
        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String url = rs.getString(3);
                Map<String, String> m = new HashMap<>();
                m.put(name, url);
                result.put(id, m);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String[] mapBreaker(Set<Map.Entry<String, String>> me) { //breaking hashmap uid, name, url
        String s = me + "";
        String[] ss = s.split("=");
        String[] result = new String[2];
        result[0] = ss[0].replace("[", "");
        result[1] = ss[1].replace("]", "");
        return result;
    }

}
