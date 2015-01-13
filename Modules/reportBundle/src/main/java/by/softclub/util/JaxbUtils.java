package by.softclub.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * User: chgv
 * Date: 20.06.2014
 * Time: 9:59
 */
public class JaxbUtils {

    public static  <T> InputStream getXML(T currencyExchangeData) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(currencyExchangeData.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
            jaxbMarshaller.marshal(currencyExchangeData, os);
            os.flush();
            return new ByteArrayInputStream(os.toByteArray());
        }
    }

    public static  <T> String getFilePath(T currencyExchangeData) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(currencyExchangeData.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        final Path tempFile = Files.createTempFile("xmlData", "xml");
        try(OutputStream os = Files.newOutputStream(tempFile)){
            jaxbMarshaller.marshal(currencyExchangeData, os);
            os.flush();
        }
        return tempFile.toString();
    }
}
