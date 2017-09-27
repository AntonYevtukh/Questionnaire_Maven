package app.config;

import app.utils.Serializer;
import app.utils.XmlSerializer;

/**
 * Created by Anton on 23.09.2017.
 */
public abstract class ConfigClass {

    public final static Serializer SERIALIZER = new XmlSerializer();
    public final static String SAVE_FILE_EXTENSION = ".xml";
    public final static String STORAGE_PATH = "D:\\Files";
            //"D:\\Storage\\Work\\Java\\Projects\\#ProgUA\\JavaProHomework\\Questionnaire\\storage";
    public final static String QUESTIONNAIRE_PATH = STORAGE_PATH + "\\questionnaires";
    public final static Object GLOBAL_LOCK = new Object();
}
