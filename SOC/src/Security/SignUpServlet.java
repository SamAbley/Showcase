package Security;

import AWS.Users;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/SignUp")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("id");
        String pass = request.getParameter("hash");

        if (Users.verify(email, pass)) {
            int status = Users.validate(email, pass, session);
            if (status == 1) { //LOGGED IN
                response.sendRedirect("user.jsp?code=1");
            } else if (status == 0) {
                response.sendRedirect("login.jsp?code=1");

            }
        } else {
            response.sendRedirect("login.jsp?code=2");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("email");
        String password = request.getParameter("pass");

        int code = 3;

        String RecaptchaResponse = request.getParameter("g-recaptcha-response");

        // Verify CAPTCHA.
        if (!VerifyRecaptcha.verify(RecaptchaResponse)) {
            code = 5;
        } else {
            if (!Users.signUp(userName, password)) {
                code = 4;
            }
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp?code=" + code);

    }

}
