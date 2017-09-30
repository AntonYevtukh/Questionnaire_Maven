package app.config;

import app.utils.Serializer;
import app.utils.XmlSerializer;

import java.io.File;

/**
 * Created by Anton on 23.09.2017.
 */
public abstract class GlobalVariables {

    public final static String LOGIN_REGEXP = "[a-zA-Z0-9_]{4,32}";
    public final static String PASSWORD_REGEXP = "[a-zA-Z0-9_]{4,16}";
            //"^(?=[_a-zA-Z0-9]*?[A-Z])(?=[_a-zA-Z0-9]*?[a-z])(?=[_a-zA-Z0-9]*?[0-9])[_a-zA-Z0-9]{4,8}$";
    public final static Serializer SERIALIZER = new XmlSerializer();
    public final static String SAVE_FILE_EXTENSION = ".xml";
    public final static String STORAGE_PATH = //"D:\\Storage\\Work\\Java\\Projects\\Questionnaire\\web\\storage";
            new File(GlobalVariables.class.getClassLoader().getResource("").getFile()).getParent() + "\\storage";
    public final static String QUESTIONNAIRE_PATH = STORAGE_PATH + "\\questionnaires";
    public final static Object GLOBAL_LOCK = new Object();

    static {
        System.out.println("Storage path: " + STORAGE_PATH);
    }
}
