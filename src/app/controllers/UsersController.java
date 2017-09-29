package app.controllers;

import app.config.GlobalVariables;
import app.model.Questionnaires;
import app.model.Statistics;
import app.model.User;
import app.model.Users;
import exceptions.PasswordMismatchException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Anton on 23.09.2017.
 */
public class UsersController extends HttpServlet {

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestType = req.getParameter("type");
        switch (requestType) {
            case "sign_in":
                signIn(req, resp);
                break;
            case "sign_up":
                signUp(req, resp);
                break;
            case "new_user":
                newUser(req, resp);
                break;
            case "create":
                createUser(req, resp);
                break;
            case "show_cabinet":
                showCabinet(req, resp);
                break;
            case "exit":
                exit(req, resp);
                break;
            case "remove":
                removeUser(req, resp);
                break;
            case "edit":
                editUser(req, resp);
                break;
            case "update":
                updateUser(req, resp);
                break;
            default:
                resp.setStatus(400);
                resp.sendRedirect("/error_page.jsp");

        }
    }

    private void signIn(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userName = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            Users.getInstance().login(userName, password);
            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("user_name", userName);
            resp.sendRedirect("/users?type=show_cabinet");
        } catch (UserNotFoundException e) {
            req.setAttribute("prev_login", userName);
            req.setAttribute("prev_password", password);
            req.setAttribute("login_error", e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (PasswordMismatchException e) {
            req.setAttribute("prev_login", userName);
            req.setAttribute("prev_password", password);
            req.setAttribute("password_error", e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    private void signUp(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/views/new_user.jsp").forward(req, resp);
    }

    private void newUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/new_user.jsp").forward(req, resp);
    }

    private void editUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("prev_login", req.getSession().getAttribute("user_name"));
        req.getRequestDispatcher("/WEB-INF/views/edit_user.jsp").forward(req, resp);
    }

    private void createUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userName = req.getParameter("login");
        String password = req.getParameter("password");
        String passwordConfirmation = req.getParameter("password_confirmation");
        boolean success = true;
        success = validateUserData(req, userName, password, passwordConfirmation);
        User user = new User(userName, password);
        try {
            Users.getInstance().addUser(user);
            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("user_name", userName);
        } catch (UserAlreadyExistsException e) {
            req.setAttribute("prev_login", userName);
            req.setAttribute("prev_password", password);
            req.setAttribute("login_error", e.getMessage());
            req.setAttribute("prev_password_confirmation", passwordConfirmation);
            success = false;
        }
        if (success)
            resp.sendRedirect("/index.jsp");
        else
            req.getRequestDispatcher("/WEB-INF/views/new_user.jsp").forward(req, resp);
    }

    private void showCabinet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String userName = (String)req.getSession().getAttribute("user_name");
        if (userName != null)
            synchronized (GlobalVariables.GLOBAL_LOCK) {
                req.setAttribute("questionnaires", Questionnaires.getInstance().getNameQuestionnaireMap().keySet());
                req.setAttribute("completed_questionnaires", Statistics.getInstance().getCompletedByUser(userName));
                req.getRequestDispatcher("/WEB-INF/views/user_cabinet.jsp").forward(req, resp);
            }
        else
            resp.sendRedirect("/index.jsp");
    }

    private void exit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession httpSession = req.getSession(true);
        httpSession.setAttribute("user_name", null);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private void removeUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String userName = (String)req.getSession().getAttribute("user_name");
        if (userName != null) {
            try {
                Users.getInstance().deleteUser(userName);
                req.getSession().removeAttribute("user_name");
                resp.sendRedirect("/index.jsp");
            } catch (UserNotFoundException e) {
                resp.sendRedirect("/index.jsp");
            }
        }
        else
            resp.sendRedirect("/index.jsp");
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String oldUserName = (String)req.getSession().getAttribute("user_name");
        String newUserName = req.getParameter("login");
        String password = req.getParameter("password");
        String passwordConfirmation = req.getParameter("password_confirmation");
        boolean success = true;
        if (oldUserName != null) {
            success = validateUserData(req, newUserName, password, passwordConfirmation);
            try {
                Users.getInstance().renameUser(oldUserName, newUserName);
                Users.getInstance().setUserPassword(newUserName, password);
                req.getSession(true).setAttribute("user_name", newUserName);
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
            } catch (UserAlreadyExistsException e) {
                req.setAttribute("prev_login", newUserName);
                req.setAttribute("prev_password", password);
                req.setAttribute("prev_password_confirmation", passwordConfirmation);
                req.setAttribute("login_error", e.getMessage());
                success = false;
            }
            if (success)
                resp.sendRedirect("/index.jsp");
            else
                req.getRequestDispatcher("/WEB-INF/views/edit_user.jsp").forward(req, resp);
        }
        else
            resp.sendRedirect("/index.jsp");
    }

    private boolean validateUserData(HttpServletRequest req, String userName, String password, String passwordConfirmation) {

        boolean success = true;

        if (!userName.matches(GlobalVariables.LOGIN_REGEXP)) {
            req.setAttribute("login_error",
                    "Login should has length between 4 and 32 and should contains only symbols and digits");
            success = false;
        }
        if (!password.matches(GlobalVariables.PASSWORD_REGEXP)) {
            req.setAttribute("password_error",
                    "Password should has length between 4 and 16 and should contains only symbols and digits");
            success = false;
        }
        if(!password.equals(passwordConfirmation)) {
            req.setAttribute("password_confirmation_error", "Password and password confirmation are not equal");
            success = false;
        }

        if(!success) {
            req.setAttribute("prev_login", userName);
            req.setAttribute("prev_password", password);
            req.setAttribute("prev_password_confirmation", passwordConfirmation);
        }
        return success;
    }
}
