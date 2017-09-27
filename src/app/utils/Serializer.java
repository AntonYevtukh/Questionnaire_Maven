package app.utils;

import java.io.File;

/**
 * Created by Anton on 22.09.2017.
 */
public interface Serializer {

    void serialize(Object obj, File file);
    <T> T deserialize(Class<T> classToken, File file);
}
