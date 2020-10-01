package Servlet;

import AWS.Upload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static AWS.Rekognition.detectModeration;
import static AWS.Rekognition.detectText;

@WebServlet("/Preview")
public class PreviewImage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // HttpSession session = request.getSession();
        String filePath = (request.getRealPath("") + "upload\\").replace("\\", "/");
        String fileName = null;
        String contentType = request.getContentType();

        if ((contentType.contains("multipart/form-data"))) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                List fileItems = upload.parseRequest(request);
                for (Object fileItem : fileItems) {
                    FileItem fi = (FileItem) fileItem;
                    if (!fi.isFormField()) {
                        fileName = fi.getName();


                        File file = new File(filePath + fileName);

                        if (file.isFile())
                            file.delete();


                        fi.write(file);

                        //Image content/text moderation check
                        String checkModeration = detectModeration(fileName);
                        String checkText = detectText(fileName);

                        if (checkModeration.length() > 0) {
                            file.delete();
                            response.getWriter().print("*" + checkModeration);
                            return;
                        } else if (checkText.length() > 0) {
                            file.delete();
                            response.getWriter().print("?" + checkText);
                            return;
                        }

                        if (file.length() >= 2097152) {
                            Upload.JavaImageResizer(file, 1400);
                        }

                        /* Deletes photo from tomcat server storage after 15 mins */
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                file.delete();
                            }
                        }, 900000);
                    }
                }


                response.getWriter().print(fileName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
