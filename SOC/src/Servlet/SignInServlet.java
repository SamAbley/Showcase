package Servlet;

import AWS.Users;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static AWS.Users.getMd5;

@WebServlet("/SignIn")
public class SignInServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");
        pass = getMd5(pass);
        int status = Users.validate(email, pass, session);
        if (status == 1) { //LOGGED IN

            response.sendRedirect("user.jsp");
        } else if (status == 0) {
            response.sendRedirect("login.jsp?code=1");

        } else if (status == -1) {
            response.sendRedirect("login.jsp?code=2");

        }
    }
}

