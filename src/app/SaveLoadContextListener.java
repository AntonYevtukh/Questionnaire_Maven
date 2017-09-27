package app;

import app.config.ConfigClass;
import app.model.Questionnaire;
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
        Users.getInstance();
        Statistics.getInstance();

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        File usersFile = new File(ConfigClass.STORAGE_PATH + "\\users" + ConfigClass.SAVE_FILE_EXTENSION);
        ConfigClass.SERIALIZER.serialize(Users.getInstance(), usersFile);
        File statisticsFile = new File(ConfigClass.STORAGE_PATH + "\\statistics" + ConfigClass.SAVE_FILE_EXTENSION);
        ConfigClass.SERIALIZER.serialize(Statistics.getInstance(), statisticsFile);
    }
}
