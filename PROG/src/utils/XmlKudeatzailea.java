package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import model.Federazioa; 

public class XmlKudeatzailea {

    public boolean esportatuXML(Federazioa federazioa, String rutaFitxategia) {
        try {
            JAXBContext context = JAXBContext.newInstance(Federazioa.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(rutaFitxategia)), StandardCharsets.UTF_8)) {

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
                writer.write("<?xml-model href=\"federazioa.xsd\" type=\"application/xml\" schematypens=\"http://www.w3.org/2001/XMLSchema\"?>\n");
                marshaller.marshal(federazioa, writer);
            }
            
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}