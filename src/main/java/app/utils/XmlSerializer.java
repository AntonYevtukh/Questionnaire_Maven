package app.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Anton on 22.09.2017.
 */
public class XmlSerializer implements Serializer {

    @Override
    public void serialize(Object obj, File file) {

        file.getParentFile().mkdirs();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(obj, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T deserialize(Class<T> classToken, File file) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classToken);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (T) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
