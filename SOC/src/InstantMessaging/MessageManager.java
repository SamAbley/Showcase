package InstantMessaging;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public final class MessageManager {

    private static final Lock LOCK = new ReentrantLock();
    private static final Set<Session> SESSIONS = new CopyOnWriteArraySet<>();
    private static HashMap<Integer, Session> users = new HashMap();

    private MessageManager() {
        throw new IllegalStateException(MessageConstants.INSTANTIATION_NOT_ALLOWED);
    }

    public static boolean userOnline(Integer id) {
        return users.containsKey(id);
    }


    static void removeNot(Integer recipient, Integer sender) {
        int[] original = MessageEndPoint.notifications.get(recipient);
        if (original != null) {
            int len = original.length;
            int[] result = new int[len];
            int n = 0;
            for (int not : original) {
                if (not != sender) {
                    result[n] = not;
                    n++;
                }
            }
            result = omit(result);
            MessageEndPoint.notifications.replace(recipient, result);
        }
    }

    static int[] omit(int[] inbox) {
        int count = 0;
        for (int n : inbox) {
            if (n == 0)
                count++;
        }


        int len = inbox.length - count;
        int[] result = new int[len];
        int n = 0;

        for (int i : inbox) {

            if (i != 0) {
                result[n] = i;
                n++;
            }
        }
        return result;
    }


    static void addNot(Integer recipient, Integer sender) {

        if (!MessageEndPoint.notifications.containsKey(recipient)) {
            MessageEndPoint.notifications.put(recipient, new int[]{sender});
        } else {
            int[] original = MessageEndPoint.notifications.get(recipient);


            int len = original.length;
            int[] result = new int[len + 1];
            int n = 0;

            for (int i : original) {

                if (i != 0) {
                    result[n] = i;
                    n++;
                }
            }
            result[len] = sender;
            MessageEndPoint.notifications.replace(recipient, result);
        }
    }

    static void broadcast(final Message message, final Session origin) {
        assert !Objects.isNull(message);
        SESSIONS.stream().filter(session -> !session.equals(origin)).forEach(session -> {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }

    static void publish(final Message message) {
        assert !Objects.isNull(message);

        String type = message.getType();
        String msg = message.getMessage();
        int to = message.getRecipient();
        int from = message.getUserName();

        String fileName;
        if (from < to) {
            fileName = from + "chat" + to;
        } else {
            fileName = to + "chat" + from;
        }

        if (!type.equals("Feed")) {

            addNot(to, from);
            Session send = users.get(message.getRecipient());
            SESSIONS.stream().filter(session -> session.equals(send)).forEach(session -> {
                try {
                    session.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            });

            if (type.equals("Img"))
                msg = "IMG" + msg;

            if (present(fileName)) {
                String file = (new File("").getAbsolutePath().replace("\\", "/")).replace("bin", "webapps/SOC/messages/") + fileName;
                saveChat(msg, file);
            } else {
                String file = (new File("").getAbsolutePath().replace("\\", "/")).replace("bin", "webapps/SOC/messages/") + fileName;
                File f = new File(file);
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saveChat(msg, file);
            }
        } else {
            Session send = users.get(message.getUserName());
            removeNot(from, to);

            SESSIONS.stream().filter(session -> session.equals(send)).forEach(session -> {
                try {
                    ArrayList<String> feed = new ArrayList<>();
                    if (present(fileName)) {
                        String file = (new File("").getAbsolutePath().replace("\\", "/")).replace("bin", "webapps/SOC/messages/") + fileName;
                        feed = addTo(file);
                    }
                    for (String line : feed) {

                        Message mes;
                        if (line.length() > 3) {
                            String img = line.substring(0, 3);
                            if (img.equals("IMG"))
                                mes = new Message(from, line.substring(3), "Img", to);
                            else
                                mes = new Message(from, line, "Text", to);
                            session.getBasicRemote().sendObject(mes);
                        }
                    }

                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    private static ArrayList<String> addTo(String fileName) {
        BufferedReader reader;
        ArrayList<String> updatedFeed = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(
                    fileName));
            String line = reader.readLine();
            while (line != null) {
                updatedFeed.add(line);
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return updatedFeed;
    }

    static boolean register(final Session session, Integer user) {
        assert !Objects.isNull(session);

        boolean result = false;
        try {
            LOCK.lock();

            result = !SESSIONS.contains(session) && SESSIONS.stream()
                    .noneMatch(elem -> (int) elem.getUserProperties().get(MessageConstants.USER_NAME_KEY) == (int) session.getUserProperties().get(MessageConstants.USER_NAME_KEY)) && SESSIONS.add(session);
            users.put(user, session);
        } finally {
            LOCK.unlock();
        }
        return result;
    }

    static void close(final Session session, final String message) {
        assert !Objects.isNull(session);

        try {
            session.close(new CloseReason(CloseCodes.VIOLATED_POLICY, message));
        } catch (IOException e) {
            throw new RuntimeException("Unable to close session", e);
        }
    }

    static boolean remove(final Session session, int user) {
        assert !Objects.isNull(session);
        users.remove(user);
        return SESSIONS.remove(session);
    }


    private static void saveChat(String text, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(fileName, true)  //Set true for append mode
            );
            writer.newLine();   //Add new line
            writer.write(text);
            writer.close();
        } catch (Exception ignored) {
        }
    }

    private static boolean present(String fileName) {
        File[] chats = chats();

        boolean present = false;
        if (chats != null)
            for (File f : chats) {

                if (f.getName().equals(fileName))
                    present = true;
            }
        return present;
    }

    private static File[] chats() { //works
        File folder = new File((new File("").getAbsolutePath().replace("\\", "/")).replace("bin", "webapps/SOC/messages/"));
        return folder.listFiles();
    }

}
