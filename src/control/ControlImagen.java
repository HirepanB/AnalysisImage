package control;

import java.awt.Image;
import unidaduno.LectorDeImagen;

public class ControlImagen {
    private LectorDeImagen lector;

    public ControlImagen(String rutaArchivo) {
        this.lector = new LectorDeImagen(rutaArchivo);
    }

    public boolean isImageLoaded() {
        return lector.isImageLoaded();
    }

    public Image getImagenOriginal() {
        return lector.getImagenOriginal();
    }

    public Image getImagenCanalesRGB() {
        return lector.getImagenCanalesRGB();
    }

    public Image getImagenGris() {
        return lector.getImagenGris();
    }

    public Image modificarBrillo(int factor, boolean esGris) {
        return lector.modificarBrillo(factor, esGris);
    }

    public Image modificarContraste(double factor, boolean esColor) {
        return lector.modificarContraste(factor, esColor);
    }
}