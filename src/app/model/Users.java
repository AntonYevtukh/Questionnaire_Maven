package app.model;

import app.config.ConfigClass;
import app.controllers.UsersController;
import app.utils.XmlSerializer;
import exceptions.PasswordMismatchException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anton on 22.09.2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "users")
public class Users {

    private static Users instance;
    @XmlElement()
    private Map<String, User> loginUserMap;

    public static Users getInstance() {

        if (instance == null) {
            File file = new File(ConfigClass.STORAGE_PATH + "\\users" + ConfigClass.SAVE_FILE_EXTENSION);
            String dir = new File("").getAbsolutePath();
            System.out.println(dir);
            if (file.exists() && file.getName().equals("users.xml"))
                instance = ConfigClass.SERIALIZER.deserialize(Users.class, file);
            else
                instance = new Users();
        }
        return instance;
    }

    private Users() {
        loginUserMap = new HashMap<>();
    }

    public synchronized Users addUser(User user)
            throws UserAlreadyExistsException {
        if (isUserNameAlreadyExists(user.getLogin()))
            throw new UserAlreadyExistsException("User with same login \"" + user.getLogin() + "\" already exists");
        else
            loginUserMap.put(user.getLogin(), user);
        return this;
    }

    public synchronized Users deleteUser(String userName)
            throws UserNotFoundException {
        if (!loginUserMap.containsKey(userName))
            throw new UserNotFoundException("User with login " + userName + "not exists. May be it has been already deleted");
        synchronized (ConfigClass.GLOBAL_LOCK) {
            loginUserMap.remove(userName);
            Statistics.getInstance().removeUserStatistics(userName);
        }
        return this;
    }

    public synchronized boolean isUserNameAlreadyExists(String userName) {
        return loginUserMap.containsKey(userName);
    }

    public synchronized void login(String userName, String password)
            throws UserNotFoundException, PasswordMismatchException {
        if (!isUserNameAlreadyExists(userName))
            throw new UserNotFoundException("Wrong login");
        else if (!loginUserMap.get(userName).getPassword().equals(password))
            throw new PasswordMismatchException("Wrong password");
    }

    public synchronized void setUserPassword(String userName, String userPassword) {
        loginUserMap.get(userName).setPassword(userPassword);
    }

    public synchronized void renameUser(String oldUserName, String newUserName)
            throws UserAlreadyExistsException {
        if (loginUserMap.containsKey(newUserName) && !oldUserName.equals(newUserName))
            throw new UserAlreadyExistsException("User with login " + newUserName + " already exists");

        synchronized (ConfigClass.GLOBAL_LOCK) {
            User user = loginUserMap.get(oldUserName);
            loginUserMap.remove(oldUserName);
            user.setLogin(newUserName);
            loginUserMap.put(user.getLogin(), user);
            Statistics.getInstance().replaceUserName(oldUserName, newUserName);
        }
    }

    @Override
    public String toString() {
        return "Users{" +
                "loginUserMap=" + loginUserMap +
                '}';
    }
}
