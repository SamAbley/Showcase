using System;
using System.Windows.Forms;

namespace GameClient
{
    public partial class BallApp
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }
        

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(BallApp));
            this.send = new System.Windows.Forms.Button();
            this.button2 = new System.Windows.Forms.Button();
            this.join = new System.Windows.Forms.Button();
            this.input = new System.Windows.Forms.TextBox();
            this.feed = new System.Windows.Forms.TextBox();
            this.users = new System.Windows.Forms.TextBox();
            this.info = new System.Windows.Forms.TextBox();
            this.inputLabel = new System.Windows.Forms.Label();
            this.start = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // button1
            // 
            this.send.Location = new System.Drawing.Point(378, 161);
            this.send.Name = "button1";
            this.send.Size = new System.Drawing.Size(70, 24);
            this.send.TabIndex = 0;
            this.send.Text = "Send";
            this.send.UseVisualStyleBackColor = true;
            this.send.Click += new System.EventHandler(this.Send_Click);
            // 
            // button2
            // 
            this.button2.Location = new System.Drawing.Point(65, 161);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(70, 24);
            this.button2.TabIndex = 1;
            this.button2.Text = "Refresh";
            this.button2.UseVisualStyleBackColor = true;
            this.button2.Click += new System.EventHandler(this.Refresh_Click);
            // 
            // button3
            // 
            this.join.Location = new System.Drawing.Point(454, 161);
            this.join.Name = "button3";
            this.join.Size = new System.Drawing.Size(70, 24);
            this.join.TabIndex = 2;
            this.join.Text = "Join";
            this.join.UseVisualStyleBackColor = true;
            this.join.Click += new System.EventHandler(this.Join_Click);
            // 
            // textBox1
            // 
            this.input.Location = new System.Drawing.Point(272, 164);
            this.input.Name = "textBox1";
            this.input.Size = new System.Drawing.Size(100, 20);
            this.input.TabIndex = 3;
            this.input.Text = "Localhost";
            this.input.TextChanged += new System.EventHandler(this.Input_TextChanged);
            // 
            // textBox2
            // 
            this.feed.Location = new System.Drawing.Point(189, 191);
            this.feed.Multiline = true;
            this.feed.Name = "textBox2";
            this.feed.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.feed.Size = new System.Drawing.Size(335, 210);
            this.feed.TabIndex = 4;
            this.feed.TextChanged += new System.EventHandler(this.Feed_TextChanged);
            // 
            // textBox3
            // 
            this.users.AccessibleName = "ta3";
            this.users.Location = new System.Drawing.Point(65, 192);
            this.users.Multiline = true;
            this.users.Name = "textBox3";
            this.users.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.users.Size = new System.Drawing.Size(118, 210);
            this.users.TabIndex = 5;
            this.users.Text = "Active Users:\r\n\r\n (offline)";
            this.users.TextChanged += new System.EventHandler(this.List_TextChanged);
            // 
            // textBox4
            // 
            this.info.Location = new System.Drawing.Point(65, 12);
            this.info.Multiline = true;
            this.info.Name = "textBox4";
            this.info.Size = new System.Drawing.Size(459, 114);
            this.info.TabIndex = 6;
            this.info.Text = resources.GetString("textBox4.Text");
            this.info.TextChanged += new System.EventHandler(this.Info_TextChanged);
            // 
            // label1
            // 
            this.inputLabel.AutoSize = true;
            this.inputLabel.Location = new System.Drawing.Point(155, 167);
            this.inputLabel.Name = "label1";
            this.inputLabel.Size = new System.Drawing.Size(117, 13);
            this.inputLabel.TabIndex = 7;
            this.inputLabel.Text = "Enter Server\'s Address:";
            this.inputLabel.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            this.inputLabel.Click += new System.EventHandler(this.InputLabel_Click);
            // 
            // button4
            // 
            this.start.Location = new System.Drawing.Point(454, 133);
            this.start.Name = "button4";
            this.start.Size = new System.Drawing.Size(70, 23);
            this.start.TabIndex = 9;
            this.start.Text = "Start";
            this.start.UseVisualStyleBackColor = true;
            this.start.Click += new System.EventHandler(this.Start_Click);
            // 
            // BallApp
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(665, 426);
            this.Controls.Add(this.start);
            this.Controls.Add(this.inputLabel);
            this.Controls.Add(this.info);
            this.Controls.Add(this.users);
            this.Controls.Add(this.feed);
            this.Controls.Add(this.input);
            this.Controls.Add(this.join);
            this.Controls.Add(this.button2);
            this.Controls.Add(this.send);
            this.Name = "BallApp";
            this.Text = "Ball Game";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.BallApp_FormClosing);
            this.Load += new System.EventHandler(this.BallApp_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button send;
        private System.Windows.Forms.Button button2;
        public System.Windows.Forms.Button join;
        public System.Windows.Forms.TextBox input;
        public  System.Windows.Forms.TextBox feed;
        public System.Windows.Forms.TextBox info;
        public  System.Windows.Forms.TextBox users;
        private Label inputLabel;
        private Button start;
    }
}

