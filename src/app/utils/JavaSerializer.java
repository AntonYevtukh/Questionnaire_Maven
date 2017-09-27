package app.utils;

import java.io.*;

/**
 * Created by Anton on 22.09.2017.
 */
public class JavaSerializer implements Serializer {

    @Override
    public void serialize(Object obj, File file) {
        file.getParentFile().mkdirs();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(obj);
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public <T> T deserialize(Class<T> classToken, File file) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))){
            return (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }
}
