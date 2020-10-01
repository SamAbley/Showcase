<%@ page import="static Security.RecaptchaConstants.SITE_KEY" %>
<html>
<head>
    <title>
        SOC: Login
    </title>
    <link rel="stylesheet" href="css/Login.css">
    <script type="text/javascript" src="js/signUp.js"></script>
    <script src='https://www.google.com/recaptcha/api.js?hl=en'></script>
</head>
<jsp:useBean id="obj" scope="session" class="Bean.LoginBean"/>

<jsp:include page="header.jsp"/>
<% if (session.getAttribute("session") == "TRUE")
    response.sendRedirect("user.jsp");

%>
<script>
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const code = urlParams.get('code');

    if (code == 1) {
        alert("Sign In Error: The email or password that you have entered is incorrect");
        window.location = '/SOC/login.jsp';
    } else if (code == 2) {
        alert("Sign In Error: Account not verified, verification email has been resent");
        window.location = '/SOC/login.jsp';
    } else if (code == 3) {
        alert("Account successfully made, please verify your email using the link that has been sent to you");
        window.location = '/SOC/login.jsp';
    } else if (code == 4) {
        alert("Sign Up Error: The email that you are attempting to sign up with is already attached to an account, \nplease try another");
        window.location = '/SOC/login.jsp';
    } else if (code == 5) {
        alert("Sign Up Error: Your Captcha was invalid, please try again");
        window.location = '/SOC/login.jsp';
    }

</script>

<body style="overflow-y: scroll;">

<div id="homeTable">

    <div class="tabButs">
        <button class="tablinks log active" onclick="changeTab(event, 'login')">Login</button>
        <button class="tablinks log" onclick="changeTab(event, 'signup')">Sign Up</button>
    </div>


    <form id="login" class="tabcontent active" method="post" action="${pageContext.request.contextPath}/SignIn">
        Email:<input type="email" name="email"/><br/><br/>
        Password:<input type="password" name="pass"/><br/><br/>
        <input class="but" id="butsign" type="submit" value="Login"/>
    </form>


    <form id="signup" class="tabcontent" onsubmit="return confirmPass()" method="post"
          action="${pageContext.request.contextPath}/SignUp">
        Email:<input type="email" name="email"/><br/><br/>
        Password:<input type="password" id="pass" name="pass"/><br/><br/>
        Confirm Password:<input type="password" id="pass2" name="signup"/><br/>
        <div class="g-recaptcha" data-sitekey="<%=SITE_KEY%>"></div>
        <br/>
        <input class="but" id="butsign" type="submit" value="Sign up"/>
    </form>
</div>
</body>
</html>









