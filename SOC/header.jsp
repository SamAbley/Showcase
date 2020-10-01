<jsp:useBean id="obj" scope="session" class="Bean.LoginBean"/>

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/secondary.css">
    <link rel="stylesheet" href="css/fonts.css">
    <script type="text/javascript" src="js/header.js"></script>
    <script type="text/javascript" src="js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="js/message.js"></script>
    <title></title>
</head>
<div id="bar">
    <div id="header">
        <a style="text-decoration: none;" onclick="window.location='/SOC'">
            <img id="logo" src="images/camera.png"/>
        </a>
        <div id="btitle">
            <h1>Sharing Over the Cloud</h1>
        </div>
        <ul id="headerButWrapper">
            <li>
                <button class="headerButtons outer" onclick="window.location='/SOC/index.jsp'">
                    <div class="inner">
                        <img class="image_on" src="images/home2.png" alt="logo"/>
                        <img class="image_off" src="images/home3.png" alt="logo"/>
                    </div>
                </button>
            </li>
            <li>
                <button id="userName" class="headerButtons outer" onclick="window.location='/SOC/login.jsp'">
                    <div class="inner">
                        <img class="image_on" src="images/user2.png" alt="logo"/>
                        <img class="image_off" src="images/user3.png" alt="logo"/>
                    </div>
                </button>
            </li>

            <li>
                <form class="searchBox" id="searchBox" action="index.jsp" method="post">

                    <input class="searchInput" id="searchInput" name="query" onfocusout="searchReturn()"
                           placeholder="Search..."
                           type="text">
                    <input type="hidden" name="cat" value="1">
                    <button class="searchButton outer">
                        <div id="searchInner" class="inner">
                            <img id="searchOff" class="image_on" src="images/search2.png" alt="logo"/>
                            <img id="searchOn" class="image_off" src="images/search3.png" alt="logo"/>
                        </div>
                    </button>
                </form>
            </li>
        </ul>
        <script>

            var checkF = false;
            $(document).ready(function () {
                $("#searchBox").hover(function () {
                    searchFocus();
                    checkF = false;

                }, function () {
                    if (!checkF) {
                        searchReturn();
                    }

                });
            });

            document.getElementById('searchInput').onfocus = function () {
                searchFocus();
            };

            function searchFocus() {
                var blurEl = document.getElementById('searchInput');
                checkF = true;
                $("#searchInner").css("background", "linear-gradient(180deg, rgb(145, 231, 219), rgb(80, 90, 120))");
                var box = document.getElementById('searchBox');
                box.style.width = "190px";

                blurEl.style.width = "135px";
                document.getElementById('searchOff').style.display = "none";
                document.getElementById('searchOn').style.display = "grid";
                document.getElementById('searchInner').style.background = "linear-gradient(180deg, rgb(145, 231, 219), rgb(80, 90, 120))";

            }

            function searchReturn() {
                checkF = false;
                var blurEl = document.getElementById('searchInput');
                var box = document.getElementById('searchBox');
                box.style.width = "50px";
                document.getElementById('searchOn').style.display = "none";
                document.getElementById('searchOff').style.display = "block";
                blurEl.style.width = "0";

                document.getElementById('searchInner').style.background = "white";

            }

        </script>
    </div>
    <div style="position: fixed;right:100px;top:20px;margin-top:45px;z-index: 1">
        <%
            if (session.getAttribute("session") == "TRUE") {

                String URL = (String) session.getAttribute("url");
                out.println("<body onload=\"init(" + session.getAttribute("uid") + ", '" + URL + "');\"></body>");
            }
        %>
    </div>

</div>




