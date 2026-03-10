package utils;

import java.io.File;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import model.Federazioa; // Asegúrate de importar tus clases

public class XmlKudeatzailea {

    public boolean esportatuXML(Federazioa federazioa, String rutaFitxategia) {
        try {
            // Creamos el contexto. 
            // NOTA: Si tienes herencia en Jokalari/Talde, añádelas aquí también.
            JAXBContext context = JAXBContext.newInstance(Federazioa.class);
            Marshaller marshaller = context.createMarshaller();
            
            // Formatear el XML para que se vea bonito y tabulado
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Generar el archivo
            File file = new File(rutaFitxategia);
            marshaller.marshal(federazioa, file);
            
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}