package app.controllers;

import app.config.ConfigClass;
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
import java.io.File;
import java.io.IOException;

/**
 * Created by Anton on 23.09.2017.
 */
public class UsersController extends HttpServlet {

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
            case "change":
                changeUserData(req, resp);
                break;
            default:
                resp.setStatus(400);
                resp.sendRedirect("/error_page.jsp");

        }
    }

    private void signIn(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = getServletContext().getRealPath("\\storage");
        System.out.println(path);
        String userName = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            Users.getInstance().login(userName, password);
            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("user_name", userName);
            resp.sendRedirect("/users?type=show_cabinet&user=" + userName);
        } catch (UserNotFoundException e) {
            req.setAttribute("prev_login", userName);
            req.setAttribute("prev_password", password);
            req.setAttribute("login_error", e.getMessage());
            req.getRequestDispatcher("/index.jsp?error_message=" + e.getMessage()).forward(req, resp);
        } catch (PasswordMismatchException e) {
            req.setAttribute("prev_login", userName);
            req.setAttribute("prev_password", password);
            req.setAttribute("password_error", e.getMessage());
            req.getRequestDispatcher("/index.jsp?error_message=" + e.getMessage()).forward(req, resp);
        }
    }

    private void signUp(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/views/new_user.jsp").forward(req, resp);
    }

    private void createUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userName = req.getParameter("login");
        String password = req.getParameter("password");
        User user = new User(userName, password);
        try {
            Users.getInstance().addUser(user);
        } catch (UserAlreadyExistsException e) {
            req.setAttribute("prev_login", userName);
            req.setAttribute("prev_password", password);
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/views/new_user.jsp").forward(req, resp);
            return;
        }
        HttpSession httpSession = req.getSession(true);
        httpSession.setAttribute("user_name", userName);
        resp.sendRedirect("/users?type=show_cabinet");
    }

    private void showCabinet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String userName = (String)req.getSession().getAttribute("user_name");
        if (userName != null)
            synchronized (ConfigClass.GLOBAL_LOCK) {
                req.setAttribute("questionnaires", Questionnaires.getInstance().getNameQuestionnaireMap().keySet());
                req.setAttribute("completed_questionnaires", Statistics.getInstance().getCompletedByUser(userName));
                req.getRequestDispatcher("/views/user_cabinet.jsp").forward(req, resp);
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
                resp.setStatus(400);
                resp.sendRedirect("/index.jsp" + e.getMessage());
            }
        }
        else
            resp.sendRedirect("/index.jsp");
    }

    private void changeUserData(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String oldUserName = (String)req.getSession().getAttribute("user_name");
        String newUserName = req.getParameter("login");
        String password = req.getParameter("password");
        if (oldUserName != null) {
            try {
                Users.getInstance().renameUser(oldUserName, newUserName);
                Users.getInstance().setUserPassword(newUserName, password);
                req.getSession(true).setAttribute("user_name", newUserName);
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
            } catch (UserAlreadyExistsException e) {
                resp.setStatus(400);
                resp.sendRedirect("/views/edit_user.jsp");
            }
        }
        else
            resp.sendRedirect("/index.jsp");
    }
}
