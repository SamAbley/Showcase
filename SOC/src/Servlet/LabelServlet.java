package Servlet;

import AWS.Rekognition;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/Label")
public class LabelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        int amount = Integer.parseInt(request.getParameter("amount"));
        String fileName = request.getParameter("fileName");
        System.out.println("DEBUG3 "+fileName);
        int type = Integer.parseInt(request.getParameter("type"));
        ArrayList<String> labels = null;
        try {
            if (type == 1)
                labels = Rekognition.detectLabels(fileName, amount);
            else
                labels = Rekognition.detectLabelsS3(fileName, amount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (String label : labels) {
            sb.append(label);
            sb.append(" ");
        }

        try {
            response.getWriter().print(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
