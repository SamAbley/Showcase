package Servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Search")
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);


    }


    @Override
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) {
        String input = request.getParameter("query");
        HttpSession session = request.getSession();
        session.setAttribute("query", input);

    }


}
