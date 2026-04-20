package utils;

import java.io.File;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class RutaEzkutuaAdapter extends XmlAdapter<String, String> {
    @Override
    public String marshal(String rutaOsoa) throws Exception {
        if (rutaOsoa == null || rutaOsoa.isEmpty()) {
			return "defecto";
		}
        File f = new File(rutaOsoa);
        String izena = f.getName();
        int pos = izena.lastIndexOf(".");
        return (pos > 0) ? izena.substring(0, pos) : izena;
    }

    @Override
    public String unmarshal(String v) throws Exception {
        return v;
    }
}