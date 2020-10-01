<%@ page import="InstantMessaging.MessageEndPoint" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="static InstantMessaging.MessageEndPoint.mapBreaker" %>
<%@ page import="java.util.Collections" %>
<%@ page import="static InstantMessaging.MessageEndPoint.setUserList" %>
<%@ page import="static InstantMessaging.MessageEndPoint.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="InstantMessaging.MessageManager" %><%--
  Created by IntelliJ IDEA.
  User: abley
  Date: 17/01/2020
  Time: 17:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%

    int uid = 0;
    if (session.getAttribute("session") != "TRUE")
        response.sendRedirect("/SOC/SignIn");
    else {
        uid = (int) session.getAttribute("uid");
    }%>
<jsp:include page="header.jsp"/>
<head>
    <title>SOC: Messages</title>
    <link rel="stylesheet" href="css/Messages.css">
    <script type="text/javascript" src="js/message.js"></script>
</head>
<body>
<div id="messageWrapper">
    <div id="imgCover">
        <div id="sendImgDiv">
            <div id="sendImgTop">
                <button style=' float: left; left: 10px; top: 10px;' class='but' onclick='sendImgOff()'>Back</button>
                <span style="position:relative; padding-top: 10px" id="imgFile">Please Choose a file</span>
            </div>
            <div id="sendImgMid">

                <form id="uploadImages" action="${pageContext.request.contextPath}/PreviewImgSend" method="post"
                      enctype="multipart/form-data">
                    <label for="uploadPhoto" id="uploadPhotoLabel" title="Choose file"></label>
                    <input id="uploadPhoto" type="file" onchange="preview()" name="file" size="50"/>
                    <div id="loading">
                        <img src='images/Loading.png'/>
                    </div>
                    <div id="uploadWrapper">
                        <img id='upPhoto' src='images/upload2.png?"+timeMilli+"'/>
                    </div>
                </form>
            </div>
            <div id="detailsWrapper">
                <input id="caption" type="text" name="caption" placeholder="Caption..." autocomplete="off"/>
                <button class="but" id="sendImg" name="send" onclick="sendImage()">Send</button>
                <script>
                    $("#caption").on('keyup', function (e) {
                        if (e.keyCode === 13) {
                            sendImage();
                        }
                    });
                </script>
            </div>
        </div>
    </div>
    <div id="listWrapper">
        <div id="listTitle">
            Users
        </div>
        <div id="listSearch">
            <div class="listSearchBox" id="listSearchBox">

                <input class="listSearchInput" id="listSearchInput" name="query" placeholder="Search..."
                       type="text">
                <button class="listSearchButton outer" onclick="searchUsers()">
                    <div id="listSearchInner" class="inner">
                        <img id="searchOff" class="image_on" src="images/search2.png" alt="logo"/>
                        <img id="searchOn" class="image_off" src="images/search3.png" alt="logo"/>
                    </div>
                </button>
            </div>
        </div>

        <div id="list">
            <div id="searchWrapper">
                <%
                    String query = (String) session.getAttribute("query");
                    session.removeAttribute("query");
                    if (query != null) {

                        out.println("<button style='float: left; margin-left: 6px;' class='but' onclick='resetUsers()'>Back</button><br>");
                        out.println("<br><p id='searchResult' style='width:90%; margin: 0 auto 15px auto; padding-bottom:10px; border-bottom:1px solid black;'>Search results for: <span style='font-weight: bold;'>\"" + query + "\"</span></p>");
                    }
                    HashMap<Integer, Map<String, String>> users = getUsers(query);
                    setUserList(users);

                    for (Map.Entry<Integer, Map<String, String>> integerStringEntry : users.entrySet()) {
                        int id = integerStringEntry.getKey();
                        int on = 0;
                        if (MessageManager.userOnline(id))
                            on = 1;
                        Map<String, String> s = integerStringEntry.getValue();
                        String[] split = mapBreaker(s.entrySet());
                        String name = split[0];
                        String url = split[1];
                        if (id != uid) {
                            out.println("");
                            out.println("<a class='but user' id='User" + id + "' onclick='selectUser(" + id + ", \"" + name + "\",\"" + url + "\", " + on + ")'>");
                            out.println("<div class='listPic'>" +
                                    "<img src='" + url + "'>" +
                                    "</div> " +
                                    "<div class='listName'>" + name +
                                    " </div>");
                            if (on == 1)
                                out.println("<div class='notOnline'>" +
                                        "</div>");
                            out.println("</a>");
                            out.println("<br>");
                        }
                    }


                %>
                <script>
                    $("#listSearchInput").on('keyup', function (e) {
                        if (e.keyCode === 13) {
                            searchUsers();
                        }
                    });
                </script>
            </div>
        </div>
    </div>
    <div id="feedCover">
        <img src='images/message.png'/>
    </div>
    <div id="feedWrapper">
        <div id="topWrapper">
            <div class="userPicL">
                <img id="picL" src="">
            </div>
            <div id="feedTitle">
            </div>
        </div>
        <div id="reverseWrapper">
            <div id="feed">
            </div>
        </div>
        <div id="bottomWrapper">
            <input id="message" type="text" name="message" placeholder="Message" autocomplete="off"/>
            <input id="type" type="hidden" name="type" value="Text"/>
            <button title="Send Image" class="formButton outer mesBut" id=""
                    onclick="sendImgOn();">
                <div class="inner">
                    <img class="image_on" src="images/upload2.png" alt="logo"/><img class="image_off"
                                                                                    src="images/upload2Alt.png"
                                                                                    alt="logo"/>
                </div>
            </button>
            <%--<button class="but" id="send" name="send" onclick="sendMessage();">Send</button>--%>
            <button title="Send Message" class="formButton outer mesBut" id="send"
                    onclick="sendMessage();">
                <div class="inner">
                    <img class="image_on" src="images/send.png" alt="logo"/><img class="image_off"
                                                                                 src="images/sendAlt.png"
                                                                                 alt="logo"/>
                </div>
            </button>
            <script>
                $("#message").on('keyup', function (e) {
                    if (e.keyCode === 13) {
                        sendMessage();
                    }
                });
            </script>
        </div>
    </div>
</div>
</body>
</html>
