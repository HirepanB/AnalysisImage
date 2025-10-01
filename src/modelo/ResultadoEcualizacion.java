package modelo;

import java.awt.image.BufferedImage;

public class ResultadoEcualizacion {
    public final BufferedImage imagenEcualizada;
    public final int[] histogramaEcualizado;

    public ResultadoEcualizacion(BufferedImage imagen, int[] histograma) {
        this.imagenEcualizada = imagen;
        this.histogramaEcualizado = histograma;
    }
}