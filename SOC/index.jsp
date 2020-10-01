<%@ page import="java.sql.ResultSet" %>
<%@ page import="AWS.Photo" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>

<jsp:useBean id="pageBean" class="Bean.PageBean"/>
<jsp:setProperty property="*" name="pageBean"/>
<html>
<head>
    <link rel="stylesheet" href="css/Login.css">
    <title>SOC: Home</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div id="homeTable">

    <%
        ResultSet photoSet;

        /* Parameters */
        String query = pageBean.getQuery();
        int cat = pageBean.getCat();
        int pageNO = pageBean.getPage();
        int photoCount = pageBean.getImage();
        int photoCheck = 0;
        String date = pageBean.getDate();

        if (pageNO == 1) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            date = dtf.format(LocalDateTime.now());
        }

        if (query != null) {
    %>
    <script>

        $(document).ready(function () {
            searchFocus();
            checkF = true;
        });
    </script>
    <div class="tab">
        <button class="<% if(cat == 1) out.println("tablinks active"); else out.println("tablinks");%>"
                onclick="document.getElementById('photo').submit();">Photo
        </button>
        <button class="<% if(cat == 2) out.println("tablinks active"); else out.println("tablinks");%>"
                onclick="document.getElementById('creator').submit();">Creator
        </button>
        <button class="<% if(cat == 3) out.println("tablinks active"); else out.println("tablinks");%>"
                onclick="document.getElementById('category').submit();">Category
        </button>
    </div>
    <form id="photo" action="index.jsp" method="post">
        <input type="hidden" name="query" value="<%=query%>"/>
        <input type="hidden" name="cat" value="1"/>
    </form>
    <form id="creator" action="index.jsp" method="post">
        <input type="hidden" name="query" value="<%=query%>"/>
        <input type="hidden" name="cat" value="2"/>
    </form>
    <form id="category" action="index.jsp" method="post">
        <input style="font-weight: bold" type="hidden" name="query" value="<%=query%>"/>
        <input type="hidden" name="cat" value="3"/>
    </form>
    <%
            String searchType;

            if (cat == 1) {
                photoSet = Photo.searchPhoto(query, photoCount);
                searchType = "Photo";
            } else if (cat == 2) {
                photoSet = Photo.searchCreator(query, photoCount);
                searchType = "Creator";
            } else {
                photoSet = Photo.searchCategory(query, photoCount);
                searchType = "Category";
            }
            out.println("<p id='searchResult'>" + searchType + " search results for: <span style='font-weight: bold;'> \"" + query + "\"</span></p>");
        } else
            photoSet = Photo.publicPhotos(photoCount, date);

        try {
            while (photoSet.next()) {
                photoCheck++;
                String[] cats = photoSet.getString(5).split(" ");
    %>
    <div id="imageWrapper">
        <div id="homeImages"><img src="<%= photoSet.getString(1) %>" alt="Photo">
        </div>
        <div id="imageDescription">

            <div class="userImg">
                <img src="<%= photoSet.getString(8) %>">
            </div>
            <div class="imageLine1">
                <form class="imageUsernameForm" method="get" action="user.jsp">
                    <input type="hidden" id="uid" name="uid" value="<%=photoSet.getInt(7)%>"/>
                    <input type="hidden" id="name" name="name" value="<%=photoSet.getString(2)%>"/>
                    <input type="submit" class="imageUsername" style="padding: 0"
                           value="<%= photoSet.getString(2) %>"/> <%=photoSet.getString(3)%>
                </form>
            </div>
            <p class="description"><%= photoSet.getString(4) %>
            </p>

            <% /* Category buttons */
                if (!cats[0].equals("")) {
                    out.println("<p  class='categoriesWrapper'>");
                    for (int i = 0; i < cats.length; i++) {
                        String label = cats[i];
                        out.print(" <form method='post' class='categories' action='index.jsp'>");
                        out.print("<input type='hidden' name='query' value='" + label + "'>");
                        out.print("<input type='hidden' name='cat' value='3'>");
                        out.print("<input type='submit' class='catsub' name='submit' value='" + label + "'>");
                        out.print("</form >");
                    }
                    out.println("</p>");
                }
            %>


            <p class="date"><%= photoSet.getString(6) %>
            </p>
        </div>
    </div>
    <%

            }
        } catch (Exception ignored) {
        }
    %>
    <div id="pageButs">
        <%
            /* Next Button */
            if (photoCheck == 10) {
        %>
        <form action="index.jsp?page=<%=pageNO+1%>" method="post">
            <input type="hidden" name="page" value="<%=pageNO+1%>"/>
            <input type="hidden" name="date" value="<%=date%>"/>
            <input type="hidden" name="image" value="<%=photoCount+10%>"/>
            <%
                if (query != null) {
            %>
            <input type="hidden" name="cat" value="<%=cat%>"/>
            <input type="hidden" name="query" value="<%=query%>"/>
            <%}%>
            <input class="but" type="submit" value="Next page"/>
        </form>
        <%
            }

            /* Back Button */
            if (pageNO != 1) {
        %>
        <form action="index.jsp?page=<%=pageNO-1%>" method="post">
            <input type="hidden" name="page" value="<%=pageNO-1%>"/>
            <input type="hidden" name="date" value="<%=date%>"/>
            <input type="hidden" name="image" value="<%=photoCount-10%>"/>
            <%
                if (query != null) {
            %>
            <input type="hidden" name="cat" value="<%=cat%>"/>
            <input type="hidden" name="query" value="<%=query%>"/>
            <%}%>
            <input class="but" type="submit" value="back"/>
        </form>
        <%
            }
        %>
    </div>
</div>
</body>
</html>