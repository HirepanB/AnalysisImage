package control;

import java.awt.Image;
import unidaduno.LectorDeImagen;

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

    public Image getImagenConBrillo(int factor) {
        return lector.modificarBrillo(factor);
    }

    /**
     * MÉTODO NUEVO: Solicita al lector la imagen con el contraste modificado.
     * @param factor El factor de contraste a aplicar.
     * @return La nueva imagen.
     */
    public Image getImagenConContraste(double factor) {
        return lector.modificarContraste(factor);
    }
}
