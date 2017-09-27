package app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * Created by Anton on 22.09.2017.
 */
public class JsonSerializer implements Serializer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void serialize(Object obj, File file) {

        file.getParentFile().mkdirs();
        String jsonString = GSON.toJson(obj);
        try(PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(jsonString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T deserialize(Class<T> classToken, File file) {

        T object = null;
        try {
            object = GSON.fromJson(new FileReader(file), classToken);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }
}
