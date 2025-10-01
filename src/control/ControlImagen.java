package control;

import modelo.ResultadoEcualizacion;
import java.awt.Image;
import modelo.TipoEcualizacion; // Importar el nuevo enum
import unidaduno.LectorDeImagen;

public class ControlImagen {
    private LectorDeImagen lector;

    public ControlImagen(String rutaArchivo) {
        this.lector = new LectorDeImagen(rutaArchivo);
    }

    // ... los métodos existentes no cambian ...
    public boolean isImageLoaded() { return lector.isImageLoaded(); }
    public Image getImagenOriginal() { return lector.getImagenOriginal(); }
    public Image getImagenCanalesRGB() { return lector.getImagenCanalesRGB(); }
    public Image getImagenGris() { return lector.getImagenGris(); }
    public Image modificarBrillo(int factor, boolean esGris) { return lector.modificarBrillo(factor, esGris); }
    public Image modificarContraste(double factor, boolean esColor) { return lector.modificarContraste(factor, esColor); }

    /**
     * MÉTODO NUEVO: Obtiene los datos del histograma de la imagen original.
     */
    public int[] getHistograma() {
        return lector.getHistograma();
    }

    /**
     * MÉTODO MODIFICADO: Ahora es flexible y pasa los parámetros al modelo.
     */
    public ResultadoEcualizacion getResultadoEcualizacion(TipoEcualizacion tipo, double... params) {
        return lector.ecualizarHistograma(tipo, params);
    }

    public double[] getHistogramaNormalizado() {
        return lector.getHistogramaNormalizado();
    }

    /**
     * MÉTODO NUEVO: Obtiene los datos de la CDF.
     */
    public double[] getCDF() {
        return lector.getCDF();
    }
    /**
     * MÉTODO NUEVO: Obtiene las estadísticas del histograma.
     */
    public String getEstadisticasHistograma() {
        return lector.getEstadisticasHistograma();
    }

}