package Servlet;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

@WebServlet("/edit")
public class EditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);


    }


    @Override
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int out = Integer.parseInt(request.getParameter("output"));
        String file = request.getParameter("fileName");
        int type = Integer.parseInt(request.getParameter("type"));

        String path = (new File("").getAbsolutePath()).replace("\\", "/");
        path = path.replace("/bin", "/webapps/SOC/upload/");
        String fileName = path + file;


        try {
            File f = new File(fileName);
            BufferedImage image = ImageIO.read(f);

            if (type == 3) {
                BufferedImage rotatedImage = apply(image, 90);
                ImageIO.write(rotatedImage, "JPG", new File(fileName));
            } else if (type == 1) {
                RescaleOp rop;
                if (out < 1)
                    rop = new RescaleOp(0.90F, 0F, null);
                else
                    rop = new RescaleOp(1.1F, 0F, null);
                // applying the parameters on the image
                // by using filter() method, it takes the
                // Source and destination objects of buffered reader
                // here our destination object is null
                BufferedImage bi
                        = rop.filter(image, null);
                ImageIO.write(bi, "JPG", new File(fileName));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //response.getWriter().print(file);
            //response.getWriter().flush();
            response.getOutputStream().print(file);
            //response.getOutputStream().flush();
            response.getOutputStream().close();
            //response.getWriter().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage apply(BufferedImage image, int angle) {
        final double rads = Math.toRadians(angle);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads, 0, 0);
        at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(image, rotatedImage);
        return rotatedImage;
    }

}
