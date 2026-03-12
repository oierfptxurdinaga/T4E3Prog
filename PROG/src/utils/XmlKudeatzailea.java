package utils;

import java.io.File;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import model.Federazioa; // Asegúrate de importar tus clases

public class XmlKudeatzailea {

    public boolean esportatuXML(Federazioa federazioa, String rutaFitxategia) {
        try {
            JAXBContext context = JAXBContext.newInstance(Federazioa.class);
            Marshaller marshaller = context.createMarshaller();
            
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            File file = new File(rutaFitxategia);
            marshaller.marshal(federazioa, file);
            
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}