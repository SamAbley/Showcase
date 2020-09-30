using System;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Collections.Generic;
using System.Web;
using System.Text.RegularExpressions;
using System.Timers;
using Timer = System.Timers.Timer;

namespace GameServer
{
    public class GameServer
    {
        private static int uniqueId;
        public int port;
        private bool running;
        public static int from = 0;
        public static int to = 0;
        private static List<Client> players;

        public GameServer(int port)
        {
            this.port = port;
            players = new List<Client>();
        }

        public static void Main(String[] args)
        {
            GameServer server = new GameServer(1500);
            server.Start();
        }

        public void Start()
        {
            running = true;

            try
            {
                TcpListener serverSocket = new TcpListener(port);
                serverSocket.Start();
                Timer timer = new Timer();
                while (running)
                {
                    Display("GameServer waiting for players on port " + port);
                    TcpClient client = serverSocket.AcceptTcpClient();
                    Client ct = new Client(client);
                    players.Add(ct);
                    ct.Start();

                    if (players.Count == 1)
                    {
                        timer = new Timer(2000);
                        timer.Elapsed += OnTimedEvent;
                        timer.Start();
                    }

                }

                try
                {
                    serverSocket.Stop();
                    foreach (Client ct in players)
                    {
                        try
                        {
                            ct.stream.Close();
                        }
                        catch (IOException) { }
                    }
                }
                catch (Exception e)
                {
                    Display("Exception closing the server and clients: " + e);
                }
            }
            catch (IOException e)
            {
                Display(" Exception on new ServerSocket: " + e);
            }
            catch (SocketException) { }
        }
        private static void OnTimedEvent(Object source, ElapsedEventArgs e)
        {
            if (Who() == 0)
                foreach (Client ct in players)
                {
                    ct.SendMessage("BALL " + 0);
                    ct.ball = true;
                    Display("The Ball Has been sent from the Server to " + ct.id);
                    break;  
                }
        }

        public static void Display(string msg)
        {
            Console.WriteLine(DateTime.Now.ToShortTimeString() + ": " + msg);
        }

        public static bool Broadcast(string w)
        {
            string type = Regex.Match(w, @"\d+").Value;
            int user = int.Parse(type);

            try
            {
                if (user == -1)
                {
                    return true;
                }
            }
            catch (ObjectDisposedException) { }

            if (w.Contains("BALL")) //Transfering the ball
            {
                try
                {
                    if (user == 0)//Check if there is a user left to send the ball to
                    {
                        LastPlayer(from);
                        return true;
                    }
                }
                catch (ObjectDisposedException) { }

                bool found = false;
                foreach (Client ct in players)
                {
                    if (user == ct.id) {
                        Display("The ball is being sent from " + from + " to " + to);
                        if (!ct.SendMessage("BALL " + from))
                        {
                            Display("A disconnected client was removed from the game");
                            players.Remove(ct);
                            break;
                        }
                    found = true;
                    ct.ball = true;
                    break;
                    }
                }
                return found;
            }
            else if (w.Contains("JOINED")) // join notification 
            {
                Display(user + " has joined");

                foreach (Client ct in players)
                    if (!ct.SendMessage(w))
                    {
                        players.Remove(ct);
                        Display("A disconnected client was removed from the game");
                        break;
                    }
                DisplayUsers();
            }
            else if (w.Contains("LEFT")) // leave notification 
            {
                Display(user + " has left");

                foreach (Client ct in players)
                    if (!ct.SendMessage(w))
                    {
                        players.Remove(ct);
                        Display("Disconnected Client " + ct.id + " removed from list.");
                        break;
                    }
                DisplayUsers();
            }
            else if (w.Contains("WHO")) // Who has the ball notification
            {
                Display(user + " has the ball");

                foreach (Client ct in players)
                    if (user != ct.id)
                        if (!ct.SendMessage(w))
                        {
                            players.Remove(ct);
                            Display("A disconnected client was removed from the game");
                            break;
                        }
            }
            return false;
        }

        public static void Remove(int id)
        {
            foreach (Client ct in players)
                if (ct.id == id)
                {
                    if (ct.ball) LastPlayer(id);
                    players.Remove(ct);
                    Broadcast("LEFT" + ct.id);
                    break;
                }
        }

        private static void DisplayUsers()
        {

            int[] dis = new int[players.Count];
            int n = 0;
            foreach (Client ct in players)
            {
                dis[n] = ct.id;
                n++;
            }
            Display("Active Users : [" + string.Join(", ", dis) + "]");
        }

        static void LastPlayer(int id)
        {
            int lastClient = -1;

            foreach (Client ct in players)
                if (id != ct.id)
                {
                    lastClient = ct.id;
                    break;
                }
            Broadcast("BALL" + lastClient);
        }

        static bool CheckUsers(int id)
        {
            foreach (Client ct in players)
                if (id == ct.id)
                    return true;
            return false;
        }

        abstract class CThread
        {
            private Thread _thread;

            protected CThread()
            {
                _thread = new Thread(new ThreadStart(RunThread));
            }

            // Thread methods / properties
            public void Start() => _thread.Start();
            public void Join() => _thread.Join();
            public bool IsAlive => _thread.IsAlive;

            // Override in base class
            public abstract void RunThread();
        }

        public static int Who()
        {
            foreach (Client ct in players)
                if (ct.ball)
                    return ct.id;
            return 0;
        }

        class Client : CThread
        {
            TcpClient socket;
            public NetworkStream stream;
            StreamReader sInput;
            StreamWriter sOutput;
            public int id;
            public bool ball;

            public Client(TcpClient socket)
            {
                this.socket = socket;
                try
                {
                    stream = socket.GetStream();
                    sInput = new StreamReader(stream);
                    sOutput = new StreamWriter(stream);

                }
                catch (IOException e)
                {
                    Display("Exception creating new Input/output Streams: " + e);
                    return;
                }
            }

            public override void RunThread()
            {
                while (true)
                {
                    try
                    {
                        string k = sInput.ReadLine();
                        string n = HttpUtility.UrlDecode(k, Encoding.UTF8);
                        string type = Regex.Match(n, @"\d+").Value;
                        int user = int.Parse(type);

                        if (n.Contains("BALL"))
                        {
                            to = user;
                            from = id;
                            bool confirmation = Broadcast("BALL " + to);
                            if (from != to)
                                ball = false;
                            if (!confirmation){
                                ball = true;
                                SendMessage("BALL " + id);
                                Display("Ball was returned to " + id);
                            }
                        }
                        else if (n.Contains("LIST"))    // send list of active clients
                        {
                            SendMessage("LIST " + 0);
                            foreach (Client ct in players)
                                SendMessage("LIST " + ct.id);

                        }
                        else if (n.Contains("ID"))
                        {
                            if (user == 0)  //New player
                            {
                                while (CheckUsers(uniqueId + 1))
                                    ++uniqueId;

                                id = ++uniqueId;
                                SendMessage("ID " + id);
                                if (Who() != 0)
                                    SendMessage("WHO " + Who());
                                Broadcast("JOINED " + id);
                            }
                            else    //Player from previous server
                            {
                                id = user;
                                if (uniqueId < id) uniqueId = id;
                                Broadcast("JOINED " + id);
                                if (Who() != 0)
                                    SendMessage("WHO " + Who());
                            }
                        }
                        else if (n.Contains("RETURN"))
                        {
                            if (Who() != 0)
                            {
                                Client ct = players[Who()];
                                ct.ball = false;
                                ct.SendMessage("RETURN " + user);
                            }
                            ball = true;
                            SendMessage("BALL " + user);
                        }
                    }
                    catch (Exception)
                    {
                        break;
                    }

                }
                if (id != 0)
                    Remove(id);
                End();
            }

            private void End()
            {
                try
                {
                    if (sOutput != null) sOutput.Close();
                    if (sInput != null) sInput.Close();
                    if (socket != null) socket.Close();
                }
                catch (Exception) { }
            }

            public bool SendMessage(string i)
            {
                if (!socket.Connected)
                {
                    End();
                    return false;
                }

                try
                {
                    sOutput.WriteLine(i);
                    sOutput.Flush();
                }
                catch (IOException) { }

                if (i.Contains("BALL"))
                    Broadcast("WHO " + id);
                return true;
            }
        }
    }
}
