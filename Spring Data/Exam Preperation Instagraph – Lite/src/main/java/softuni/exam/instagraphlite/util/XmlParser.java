package softuni.exam.instagraphlite.util;

import javax.xml.bind.JAXBException;

public interface XmlParser {

    <T> T formFile(String filePath, Class<T> tClass) throws JAXBException;
}
