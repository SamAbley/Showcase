using System;
using System.Diagnostics;
using System.Windows.Forms;
using static GameClient.GameClient;

namespace GameClient
{
    public partial class BallApp : Form
    {      
        public static GameClient client;
        private delegate void SafeCallDelegate(string text);
        public static bool connected = false;
        public static int[] backUp = null;
        public BallApp()
        {
            InitializeComponent();
            Controls.Add(input);
            Controls.Add(feed);
            Controls.Add(users);
            Controls.Add(join);
            Controls.Add(send);
            feed.ReadOnly = true;
            users.ReadOnly = true;
            info.ReadOnly = true;
        }


        private void Send_Click(object sender, EventArgs e)
        {
            if (connected)
                if (ball)
                {
                    try
                    {
                        int msg = Convert.ToInt32(input.Text);

                        if (msg > 0)
                        {
                            SendMessage("BALL " + msg);
                            ball = false;
                            AppendFeed("> " + DateTime.Now.ToShortTimeString() + ": Ball sent to " + msg);
                        }
                        else
                            AppendFeed("> Cannot send to " + msg);

                    }
                    catch (Exception)
                    {
                        AppendFeed("> Cannot send to " + input.Text);

                    }
                }
                else
                    AppendFeed("> Cannot send as you do not have the ball");

        }

        private void Refresh_Click(object sender, EventArgs e) //Refresh list
        {
            if (connected)
                SendMessage("LIST " + 0);
        }

        public void Join_Click(object sender, EventArgs e) //Login / Logout
        {
            if (connected)
            {
                connected = false;
                client.Disconnect();
                backUp = null;
                inputLabel.Text = "Enter Server's Address:";
                input.Text = "Localhost";
                join.Text = "Join";
                feed.Text = "> " + DateTime.Now.ToShortTimeString() + ": You have left the server";
                users.Text = "Active Users:";
                users.AppendText("(Offline)");
                Text = "Ball Game";
                start.Visible = true;


            }
            else
            {
                string serverAddress = input.Text;
                if (serverAddress == null) 
                    return;
                client = new GameClient(serverAddress, 1500, this);
                client.restart = false;
                if (client.Start())
                {
                    SendMessage("LIST " + 0);
                    connected = true;
                    inputLabel.Text = "                      Enter ID:";
                    input.Text = "";
                    join.Text = "Leave";
                    start.Visible = false;
                }
                else
                    feed.Text = "> Cannot connect to " + input.Text + ". Try again";
            }
        }

        private void List_TextChanged(object sender, EventArgs e)
        { }

        private void Input_TextChanged(object sender, EventArgs e)
        { }
        private void Feed_TextChanged(object sender, EventArgs e)
        { }
        private void Info_TextChanged(object sender, EventArgs e)
        { }
        private void InputLabel_Click(object sender, EventArgs e)
        { }

      

        private void BallApp_Load(object sender, EventArgs e)
        { }


        public void TitleText(string text)
        {
            if (InvokeRequired)
            {
                var d = new SafeCallDelegate(TitleText);
                Invoke(d, new object[] { text });
            }
            else
                Text = text;

        }

        public void InputText(string text)
        {
            if (input.InvokeRequired)
            {
                var d = new SafeCallDelegate(InputText);
                input.Invoke(d, new object[] { text });
            }
            else
                input.Text = text;

        }

        public void LeaveSafe(string text)
        {
            if (join.InvokeRequired)
            {
                var d = new SafeCallDelegate(LeaveSafe);
                join.Invoke(d, new object[] { text });
            }
            else
            {
                join.Text = text;
                connected = false;
            }
        }

        public void LabelText(string text)
        {
            if (join.InvokeRequired)
            {
                var d = new SafeCallDelegate(LabelText);
                inputLabel.Invoke(d, new object[] { text });
            }
            else
                inputLabel.Text = text;

        }

        public void AppendFeed(string text)
        {
            if (feed.InvokeRequired)
            {
                var d = new SafeCallDelegate(AppendFeed);
                feed.Invoke(d, new object[] { text });
            }
            else
                try { feed.Text = feed.Text.Insert(0, Environment.NewLine + text + Environment.NewLine); }
                catch (Exception) { }

        }

        public void AppendList(string text)
        {
            if (users.InvokeRequired)
            {

                var d = new SafeCallDelegate(AppendList);
                users.Invoke(d, new object[] { text });

            }
            else
                users.AppendText(Environment.NewLine + text);

        }

        public void ReplaceFeed(string text)
        {
            if (feed.InvokeRequired)
            {
                var d = new SafeCallDelegate(ReplaceFeed);
                feed.Invoke(d, new object[] { text });
            }
            else
                feed.Text = text;
        }

        public void ReplaceList(string text)
        {
            if (users.InvokeRequired)
            {
                var d = new SafeCallDelegate(ReplaceList);
                users.Invoke(d, new object[] { text });
            }
            else
                users.Text = text;
        }

        private void BallApp_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (connected)
            {
                SendMessage("Return");
                client.Disconnect();
            }
        }

        private void Start_Click(object sender, EventArgs e)
        {
            Process.Start(@"GameServer.exe");
            input.Text = "Localhost";
            join.PerformClick();
        }
    }
}
