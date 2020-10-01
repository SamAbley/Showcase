<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page import="AWS.Users" %>
<%@ page import="AWS.Photo" %>
<%@ page import="AWS.Rekognition" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="static AWS.Rekognition.top3" %>


<jsp:useBean id="pb" scope="page" class="Bean.ProfileBean"/>
<jsp:setProperty property="*" name="pb"/>

<jsp:useBean id="ptb" scope="page" class="Bean.PhotoBean"/>
<jsp:setProperty property="*" name="ptb"/>

<jsp:useBean id="ph" class="Bean.UploadBean"/>
<jsp:setProperty property="*" name="ph"/>
<html>
<head>
    <title>SOC: Account</title>
    <script type="text/javascript" src="js/jquery-3.4.1.min.js"></script>
    <link rel="stylesheet" href="css/Upload.css">
</head>
<jsp:include page="header.jsp"/>
<script>
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const code = urlParams.get('code');

    if (code == 1) {
        alert("Email has been verified, enjoy uploading!");
        window.location = '/SOC/user.jsp';
    }

</script>
<% int uid = 0;
    if (request.getParameter("uid") != null)
        uid = Integer.parseInt(request.getParameter("uid"));
    String userName = request.getParameter("name");
    int uid2 = 0;
    if (session.getAttribute("uid") != null)
        uid2 = (int) session.getAttribute("uid");

    ResultSet rs = null;
    int pid = -1;
%>
<body>
<div id="homeTable">

    <%
        boolean own = false;
        boolean profile = false;
        if (uid != 0 && uid != uid2) {
            rs = Photo.profilePub(uid); //someone else's profile
            ResultSet rsp = Users.profile(uid);
            if (rsp.next()) {
                String profileName = rsp.getString(1);
                String bio = rsp.getString(2);
                String url = rsp.getString(3);
    %>
    <div id="profileWrapper">
        <div id="imgWrapper">
            <img src="<%=url%>"/>
        </div>
        <div id="infoWrapper">

            <div class="profileName"><%=profileName%>
            </div>
            <div class="bio"><%=bio%>
            </div>


            <div class="top3Wrapper">
                <%
                    String[] cats = top3(uid);
                    int i = 1;
                    if (cats != null) {
                        for (String label : cats) {
                            out.println("<div style='width:20px; margin-top:4px;'>#" + i + "</div>");
                            out.print("<div method='post' class='topcats' style='width:auto;'>");

                            out.print("<form method='post' class='categories' style='width:auto;' action='index.jsp'>");
                            out.print("<input type='hidden' name='query' value='" + label + "'>");
                            out.print("<input type='hidden' name='cat' value='3'>");
                            out.print("<input type='submit' class='catsub top3' name='submit' value='" + label + "'>");
                            out.print("</form >");
                            out.print("</div >");
                            i++;
                        }
                    }
                %>
            </div>
        </div>
    </div>
    <%}%>
    <h2 class='userTitle'><%=userName%>'s photos</h2>

    <%
    } else if (session.getAttribute("session") == "TRUE") { //Logged in
        own = true;
        uid = (int) session.getAttribute("uid");
        session.setMaxInactiveInterval(180); //SESSION TIMEOUT


        if (ptb.getPid() != null) {//edit photo
            pid = Integer.parseInt(ptb.getPid());
            rs = Photo.editPhoto(pid);
            out.println("<h2 class='userTitle'>Your Photo</h2>");
        } else {//normal profile
            profile = true;
            rs = Photo.profile(uid);

            //call profile ResultSet
            ResultSet rsp = Users.profile(uid);
            if (rsp.next()) {
                String profileName = rsp.getString(1);
                String bio = rsp.getString(2);
                String url = rsp.getString(3);


    %>

    <div id="profileWrapper">

        <form class="editForm" method="post" action="user.jsp">
            <input type="hidden" name="pname" value="1"/>
            <input type="hidden" name="profileUrl" value="<%=url%>"/>
            <!-- <input class="but" type="submit"  value="Change Picture"/> -->
            <button title="Edit Picture" class="formButton outer" id="editPictureBut">
                <div id="editPictureInner" class="inner">
                    <img class="image_on" src="images/edit.png" alt="logo"/><img class="image_off"
                                                                                 src="images/editAlt.png" alt="logo"/>
                </div>
            </button>
        </form>

        <div id="imgWrapper">
            <div id="loading">
                <img src='images/Loading.png'/>
            </div>
            <img id="upPhoto" src="<%=url%>"/>
        </div>

        <%
            if (pb.getEuid() != null) { //edit profileDETAILS
                uid = Integer.parseInt(pb.getEuid());
                profileName = pb.getProfileName();
                bio = pb.getBio();
        %>
        <form id="infoWrapper" action="${pageContext.request.contextPath}/Upload" method="post">
            <input type="hidden" name="type" value="profileD"/>
            <input style="width: 100%" type="text" class="profileName" id="profileName" name="profileName"
                   value="<%=profileName%>">
            <textarea style="width: 100%; height: 120px" class="bio" id="bio" name="bio"><%=bio%></textarea>
            <input type="hidden" id="euid" name="euid" value="<%=uid%>"/>
            <div class="profileButs" id="detailsWrapper">
                <button type="button" style=" float: left; bottom: 10px; position: absolute" class="but"
                        onclick="window.location='/SOC/user.jsp'">Back
                </button>

                <button style="margin: 0 70px" title="Save" class="formButton outer"
                        onclick="form.submit()">
                    <div class="inner">
                        <img class="image_on" src="images/save.png" alt="logo"/><img class="image_off"
                                                                                     src="images/saveAlt.png"
                                                                                     alt="logo"/>
                    </div>


                </button>
            </div>
        </form>

        <%
        } else if (ph.getPname() != null) { //EDIT profile PHOTO
        %>

        <div id="infoWrapper">

            <form id="uploadImages" action="${pageContext.request.contextPath}/Preview" method="post"
                  enctype="multipart/form-data">
                <input style="display: inline-block" class="but" id="uploadProfile" type="file" onchange="preview()"
                       name="file" size="50"/>
                <label style="margin: 10px 20px" id="imgFile">File:</label>


            </form>
            <div class="profileButs">
                <button style="float: left; bottom: 10px; position: absolute" class="but"
                        onclick="window.location='/SOC/user.jsp'">Back
                </button>


                <form class="editForm" id="detailsWrapper" style="display: none; margin: 0 70px"
                      onsubmit="confirm('Are you sure want to change this?');"
                      action="${pageContext.request.contextPath}/Upload" method="post">
                    <input id="FileInput" type="hidden" name="FileInput"/>
                    <input type="hidden" name="type" value="profileP"/>

                    <button style="" title="Save" class="formButton outer"
                            onclick="alert('You will need to allow a few seconds before refreshing the page so that your new profile picture can upload to the server') && form.submit()">

                        <div class="inner">
                            <img class="image_on" src="images/save.png" alt="logo"/><img class="image_off"
                                                                                         src="images/saveAlt.png"
                                                                                         alt="logo"/>
                        </div>

                    </button>
                </form>

            </div>

        </div>


        <%
        } else { //normal profile

        %>
        <script>
            $(document).ready(function () {
                $("#imgWrapper").hover(function () {
                    $("#editPictureBut").css("display", "block");
                }, function () {
                    $("#editPictureBut").css("display", "none");
                });
            });
            $(document).ready(function () {
                $("#editPictureBut").hover(function () {
                    $("#editPictureBut").css("display", "block");
                }, function () {
                    //$("#editPictureBut").css("display", "none");
                });
            });


        </script>

        <div id="infoWrapper">
            <div id="editWrapper">
                <form class="editForm" method="post" action="user.jsp">
                    <input type="hidden" name="euid" value="<%=uid%>"/>
                    <input type="hidden" name="profileName" value="<%=profileName%>"/>
                    <input type="hidden" name="bio" value="<%=bio%>"/>
                    <!-- <input class="but" type="submit"  value="Edit Profile"/> -->
                    <button title="Edit Profile" class="formButton outer" id="editProfileBut">
                        <div class="inner">
                            <img class="image_on" src="images/edit.png" alt="logo"/><img class="image_off"
                                                                                         src="images/editAlt.png"
                                                                                         alt="logo"/>
                        </div>
                    </button>
                </form>
                <div class="profileName"><%=profileName%>
                </div>
                <div class="bio"><%=bio%>
                </div>
            </div>

            <div class="profileButs">


                <button title="Upload" class="formButton outer" id="uploadBut"
                        onclick="window.location='upload.jsp';">
                    <div class="inner">
                        <img class="image_on" src="images/upload2.png" alt="logo"/><img class="image_off"
                                                                                        src="images/upload2Alt.png"
                                                                                        alt="logo"/>
                    </div>
                </button>

                <button title="Messages" class="formButton outer"
                        onclick="window.location='messages.jsp';" id="messageBut">
                    <div class="inner">
                        <img class="image_on" src="images/message.png" alt="logo"/><img class="image_off"
                                                                                        src="images/messageAlt.png"
                                                                                        alt="logo"/>
                    </div>
                </button>
                <div class="topWrapper" title="Top categories">
                    <div class="top3Wrapper">
                        <%
                            String[] cats = top3(uid);
                            int i = 1;
                            if (cats != null) {
                                for (String label : cats) {
                                    out.println("<div style='width:20px; margin-top:4px;'>#" + i + "</div>");
                                    out.print("<div method='post' class='topcats' style='width:auto;'>");

                                    out.print("<form method='post' class='categories' style='width:auto;' action='index.jsp'>");
                                    out.print("<input type='hidden' name='query' value='" + label + "'>");
                                    out.print("<input type='hidden' name='cat' value='3'>");
                                    out.print("<input type='submit' class='catsub top3' name='submit' value='" + label + "'>");
                                    out.print("</form >");
                                    out.print("</div >");
                                    i++;
                                }
                            }
                        %>
                    </div>
                </div>


                <button title="Logout" class="formButton outer" id="logoutBut"
                        onclick="window.location='/SOC/SignIn';">
                    <div class="inner">
                        <img class="image_on" src="images/logout.png" alt="logo"/><img class="image_off"
                                                                                       src="images/logoutAlt.png"
                                                                                       alt="logo"/>
                    </div>
                </button>

            </div>
        </div>
        <script>
            $(document).ready(function () {
                $("#editWrapper").hover(function () {
                    $("#editProfileBut").css("display", "block");
                }, function () {
                    $("#editProfileBut").css("display", "none");
                });
            });


        </script>
        <%}%>
    </div>


    <% }
        out.println("<h2 class='userTitle'>Your Photos</h2>");
    }
    } else {
        response.sendRedirect("/SOC/SignIn");
    }
        try {
            if (rs != null) {
                while (rs.next()) {
                    String[] cats = rs.getString(5).split(" ");
    %>
    <div id="imageWrapper">
        <%
            if (profile)
                if (!rs.getBoolean(7)) {
        %>
        <div id="outer"></div>
        <div id="inner"></div>
        <img id="private" src="images/private.png">
        <%}%>
        <div id="homeImages">
            <img src="<%= rs.getString(1) %>" alt="Photo">
        </div>
        <div id="imageDescription">
            <%
                if (pid == -1) {     //view all photos
            %>

            <%=rs.getString(3)%>
            <p class="description"><%= rs.getString(4) %>
            </p>
            <%
                if (!cats[0].equals("")) {

                    for (int i = 0; i < cats.length; i++) {
                        String label = cats[i];
                        out.print(" <form method='post' class='categories' action='index.jsp'>");
                        out.print("<input type='hidden' name='query' value='" + label + "'>");
                        out.print("<input type='hidden' name='cat' value='3'>");
                        out.print("<input type='submit' class='catsub' name='submit' value='" + label + "'>");
                        out.print("</form >");
                    }
                }
            %>
            <p class="date"><%= rs.getString(6) %>
            </p>
            <%
                if (session.getAttribute("session") == "TRUE" && own) {
            %>
            <form method="post" action="user.jsp">
                <input type="hidden" name="pid" value="<%=rs.getInt(2)%>"/>
                <button id="editDescription" title="Edit Photo Details" class="formButton outer"
                        onclick="window.location='messages.jsp';">
                    <div id="editInner" class="inner">
                        <img class="image_on" src="images/edit.png" alt="logo"/><img class="image_off"
                                                                                     src="images/editAlt.png"
                                                                                     alt="logo"/>
                    </div>
                </button>
            </form>
            <%
                }
            } else {            //Edit photo
            %>

            <!-- EDIT Form -->
            <form id="uploadDescription"
                  onsubmit="return checkDetails() && confirm('Are you sure want to apply these changes?');"
                  action="${pageContext.request.contextPath}/Upload" method="post">
                Name: <input class="uploadText" id="namejs" type="text" name="pname" size="40"
                             value="<%=rs.getString(3)%>"/>

                Description: <textarea class="uploadText" id="descriptionjs" name="description" cols="40"
                                       rows="5"><%= rs.getString(4) %></textarea>

                <div>
                    Categories:
                    <button class="but" type="button" id="autoGen" style="width: 100px; top: 10px" onclick="autoOn()">
                        Auto Generate
                    </button>
                </div>
                <textarea class="uploadText" id="categoriesjs" name="categories" cols="71" rows="5"
                          placeholder="Enter your own or auto generate (separated by spaces)"><%
                    int amt = ptb.getAmount();
                    boolean amount = amt != 0;
                    String s3Photo = uid + "/photo" + pid;
                    if (amount) {
                        ArrayList<String> labels = Rekognition.detectLabelsS3(s3Photo, amt);
                        for (String label : labels) {
                            out.print(label + " ");
                        }
                    } else
                        out.print(rs.getString(5));


                %></textarea>

                <span id="sliderLabel">Generate:</span>
                <div id="sliderWrapper">
                    <div id="analyse">
                        <label for="amtRange" title="Amount of labels displayed">Amount: </label>
                        <input type="range" min="1" max="20" value="<%
                if(amount) out.print(amt);
                else out.print(5);
                %>" name="amount" class="slider" id="amtRange" title="Amount of labels displayed">
                        <span id="amtDisplay" title="Amount of labels displayed"></span>

                        <button class="but" type="button" id="ana" style="bottom:5px; margin-left: 10px"
                                onclick="autoGenerate()">Analyse Photo
                        </button>
                    </div>
                </div>
                <%
                    String check = "";
                    if (rs.getInt(6) == 1)
                        check = "checked";
                %>
                <label for="pubjs">Public?</label>
                <input id="pubjs" style="" type="checkbox" name="pub" value="true" <%=check%> />
                <input type="hidden" id="pid" name="pid" value="<%=rs.getInt(2)%>"/>
                <input type="hidden" name="type" value="edit"/>


                <input class="but" style="top: 10px; width: 120px" type="submit" value="Apply Changes"/>
                <button class="but" style="top: 10px; left:10px; width: 90px" type="button"
                        onclick="window.location='/SOC/user.jsp'">Back
                </button>


            </form>
            <script>

                var sliderA = document.getElementById("amtRange");
                var outputA = document.getElementById("amtDisplay");
                outputA.innerHTML = sliderA.value;

                sliderA.oninput = function () {
                    outputA.innerHTML = this.value;
                };

                function autoGenerate() {
                    var amt = document.getElementById("amtRange").value;
                    var file = "<%=s3Photo%>";

                    document.getElementById("categoriesjs").innerHTML = "Generating...";
                    $.ajax({
                        url: "Label",
                        data: {
                            amount: amt,
                            fileName: file,
                            type: 2
                        },
                        method: 'post',
                        success: function (data) {
                            document.getElementById("categoriesjs").innerHTML = data;
                            document.getElementById("categoriesjs").focus();

                        },
                        error: function (e) {
                            alert('Error: ' + e.message);
                        }
                    });

                }
            </script>
            <!-- DELETE BUTTON -->
            <form style="display: inline-block; margin-left: 5px"
                  onclick="return confirm('Are you sure you want to delete this photo?');"
                  action="${pageContext.request.contextPath}/Upload"
                  method="post">
                <input type="hidden" name="pid" value="<%=rs.getInt(2)%>"/>
                <input id="uploadPhoto" type="file" onchange="preview()" name="file" size="50"/>
                <input type="hidden" name="type" value="delete"/>
                <input class="but" type="submit" class="imageUsername" style="padding: 2px" value="Delete"/>
            </form>

            <%}%>
        </div>
    </div>
    <% }
    }
    } catch (SQLException ignored) {
    }%>
</div>

</body>
</html>
