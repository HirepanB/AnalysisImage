package control;

import java.awt.Image;
import unidaduno.LectorDeImagen;

/**
 *
 * @author sdelaot
 */
public class ControlImagen {
    private LectorDeImagen lector;

    public ControlImagen(String nombreArchivo) {
        lector = new LectorDeImagen(nombreArchivo);
    }

    public Image getImagen(int cual) {
        return lector.getImage(cual);
    }

    public int getAlto() {
        return lector.getAlto();
    }

    public int getAncho() {
        return lector.getAncho();
    }
}