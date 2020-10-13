using System;
using System.Diagnostics;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Web;

namespace GameClient
{
    public class GameClient : BallApp
    {
        public string server;
        public int port;
        public TcpClient socket;
        public NetworkStream stream;
        public static StreamReader sInput { get; private set; }
        public static StreamWriter sOutput { get; set; }
        public static BallApp app;
        public bool restart = false;
        public static bool ball;
        public static int id, ballHolder;

        public GameClient(string server, int port, BallApp ba)
        {
            this.server = server;
            this.port = port;
            app = ba;
            id = 0;
        }


        public bool Start()
        {
            
            {
                try
                {
                    ball = false;
                    socket = new TcpClient(server, port);
                    stream = socket.GetStream();
                    sInput = new StreamReader(stream);
                    sOutput = new StreamWriter(stream);
                }

                catch (Exception ec)
                {
                    DisplayFeed("Error connecting to server:" + ec);
                    return false;
                }
                DisplayNewFeed("Connection accepted " + socket.ToString());

                new ListenFromServer(this).Start();
                try
                {
                    if (restart)
                        id = backUp[0];
                }
                catch (Exception)
                {
                    return false;
                }
                SendMessage("ID " + id);
                return true;
            }
        }

        public static void DisplayFeed(string msg)
        {
            app.AppendFeed(msg);
        }


        public static void DisplayList(string msg)
        {
            app.AppendList(msg);
        }


        public static void DisplayNewFeed(string msg)
        {
            app.ReplaceFeed(msg);
        }


        public static void DisplayNewList(string msg)
        {
            app.ReplaceList(msg);
        }

        public static void SendMessage(string msg)
        {
            try
            {
                sOutput.WriteLine(msg);
                sOutput.Flush();
            }
            catch (Exception)
            { }

            if (msg.Contains("BALL"))
            {
                ball = false;
                SendMessage("LIST " + 0);
            }

        }


        public void Disconnect()
        {
            try
            {
                sOutput.Close();
                sInput.Close();
                socket.Close();
                Close();
            }
            catch (Exception) { }


        }

        public new void Leave()
        {
            Disconnect();
            app.LeaveSafe("Join");
            app.InputText("Localhost");
            app.LabelText("Enter Server's Address:");
            app.ReplaceFeed("> " + DateTime.Now.ToShortTimeString() + ": You have left the server");
            app.ReplaceList("Active Users: ");
            app.AppendList("(Offline)");
            app.TitleText("Ball Game");
            restart = false;
            connected = false;
            id = 0;
        }


        public void Save()
        {
            backUp = new int[2];
            backUp[0] = id;
            backUp[1] = ball ? 1 : 0;
        }

        public void SetTitle(string i)
        {
            app.TitleText(i);
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



        class ListenFromServer : CThread
        {
            GameClient gc;

            public ListenFromServer(GameClient gc)
            {
                this.gc = gc;
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
                        string date = DateTime.Now.ToShortTimeString();

                        if (n.Contains("BALL"))
                        {
                            ball = true;
                            ballHolder = id;
                            if (user != id)
                                DisplayFeed("> " + date + ": Ball Received from " + user);
                            else DisplayFeed("> " + date + ": Ball was returned");
                            DisplayFeed("> " + date + ": You now have the ball");
                            SendMessage("LIST " + 0);
                        }
                        else if (n.Contains("LIST"))
                        {
                            if (user == 0)
                                DisplayNewList("Active Users: " + date);
                            else
                            {
                                string userLine = user + "";
                                if (user == id)
                                    userLine += " (You)";
                                if (user == ballHolder)
                                    userLine += " (BALL)";
                                DisplayList("> " + userLine);
                            }
                        }
                        else if (n.Contains("ID"))
                        {
                            id = user;
                            DisplayFeed("> Your ID is : " + id);
                            gc.SetTitle("Ball Game: Your ID is " + id);
                        }
                        else if (n.Contains("JOINED"))
                        {
                            if (user != id)
                                DisplayFeed("> " + date + ": " + user + " has joined the game");
                            SendMessage("LIST " + 0);
                        }
                        else if (n.Contains("LEFT"))
                        {
                            DisplayFeed("> " + date + ": " + user + " has left the game");
                            SendMessage("LIST " + 0);
                        }
                        else if (n.Contains("WHO"))
                        {
                            ballHolder = user;
                            DisplayFeed("> " + date + ": " + ballHolder + " now has the ball");
                            SendMessage("LIST " + 0);
                        }
                        else if (n.Contains("RETURN"))
                        {
                            ball = false;
                            ballHolder = user;
                            SendMessage("LIST " + 0);
                        }
                    }
                    catch (IOException)
                    {
                        if (connected) //If server restart
                        {
                            gc.Save();
                            gc.Disconnect();
                            
                            Process.Start(@"GameServer.exe");
                            gc = new GameClient("Localhost", 1500, app)
                            {
                                restart = true
                            };
                            if (gc.Start())
                            {
                                DisplayFeed("Server has restarted & Game continued");
                                DisplayFeed("> You id is still : " + backUp[0]);
                                if (backUp[1] == 1)
                                    SendMessage("RETURN " + backUp[0]);
                                gc.restart = false;
                            }
                            else
                            {
                                gc.Leave();
                            }
                        }
                        break;
                    }
                    catch (Exception)
                    {
                        break;
                    }

                }

            }
        }

    }
}

