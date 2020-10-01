package Servlet;

import AWS.Upload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/Upload")
public class UploadProcess extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();


        String type = request.getParameter("type");

        switch (type) {
            case "profileD": {
                String eUid = request.getParameter("euid");
                int uid = Integer.parseInt(eUid);
                String profileName = request.getParameter("profileName");
                String bio = request.getParameter("bio");
                Upload.dbProfile(uid, profileName, bio);
                try {
                    response.sendRedirect("user.jsp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "profileP": {
                String fileName = request.getParameter("FileInput");
                int uid = (int) session.getAttribute("uid");
                String filePath = (new File("").getAbsolutePath()).replace("\\", "/");
                filePath = filePath.replace("/bin", "/webapps/SOC/upload/");
                File f = new File(filePath + fileName);
                Upload.uploadProfile(filePath + fileName, uid);
                f.delete();
                try {
                    response.sendRedirect("user.jsp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "message": {

                String fileName = request.getParameter("file");
                int uid = (int) session.getAttribute("uid");
                String filePath = (new File("").getAbsolutePath()).replace("\\", "/");
                filePath = filePath.replace("/bin", "/webapps/SOC/upload/");
                File f = new File(filePath + fileName);
                String url = Upload.uploadMes(filePath + fileName, uid);
                f.delete();
                try {
                    response.getWriter().print(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

            default:
                String name = request.getParameter("pname");
                String description = request.getParameter("description");
                String categories = request.getParameter("categories");
                name = Upload.removeIllegal(name);
                description = Upload.removeIllegal(description);
                categories = Upload.removeIllegal(categories);

                if (type.equals("edit")) {
                    int pid = Integer.parseInt(request.getParameter("pid"));

                    String pubCheck = request.getParameter("pub");
                    boolean pub = pubCheck != null;


                    Upload.dbEdit(pid, name, description, categories, pub);

                } else if (type.equals("delete")) {
                    int pid = Integer.parseInt(request.getParameter("pid"));
                    int uid = (int) session.getAttribute("uid");
                    Upload.dbDelete(pid, uid);
                } else {

                    String filePath = (new File("").getAbsolutePath()).replace("\\", "/");
                    filePath = filePath.replace("/bin", "/webapps/SOC/upload/");

                    String fileName = request.getParameter("FileInput");
                    int uid = (int) session.getAttribute("uid");

                    String pubCheck = request.getParameter("pub");
                    boolean pub = pubCheck != null;


                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String date = dtf.format(LocalDateTime.now());
                    File f = new File(filePath + fileName);
                    Upload.upload(filePath + fileName, uid, name, description, categories, pub, date);

                    f.delete();
                }
                try {
                    response.sendRedirect("user.jsp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }


    }
}
