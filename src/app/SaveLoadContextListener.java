package app;

import app.config.GlobalVariables;
import app.model.Questionnaires;
import app.model.Statistics;
import app.model.Users;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

/**
 * Created by Anton on 23.09.2017.
 */
@WebListener
public class SaveLoadContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //TODO Data integrity
        Questionnaires.getInstance();
        Users.getInstance();
        Statistics.getInstance();

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        File usersFile = new File(GlobalVariables.STORAGE_PATH + "\\users" + GlobalVariables.SAVE_FILE_EXTENSION);
        GlobalVariables.SERIALIZER.serialize(Users.getInstance(), usersFile);
        File statisticsFile = new File(GlobalVariables.STORAGE_PATH + "\\statistics" + GlobalVariables.SAVE_FILE_EXTENSION);
        GlobalVariables.SERIALIZER.serialize(Statistics.getInstance(), statisticsFile);
    }
}
