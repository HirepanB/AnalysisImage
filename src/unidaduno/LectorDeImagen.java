package unidaduno;

import modelo.ResultadoEcualizacion;
import modelo.TipoEcualizacion; // Importar el nuevo enum
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LectorDeImagen {
    private BufferedImage laImagen;

    public LectorDeImagen(String rutaArchivo) {
        try {
            laImagen = ImageIO.read(new File(rutaArchivo));
        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo de imagen: " + ioe);
            laImagen = null;
        }
    }

    public boolean isImageLoaded() {
        return laImagen != null;
    }

    public Image getImagenOriginal() {
        return laImagen;
    }

    public Image getImagenCanalesRGB() {
        if (!isImageLoaded()) return null;
        Image rojo = getImagenCanal(1);
        Image verde = getImagenCanal(2);
        Image azul = getImagenCanal(3);
        int ancho = laImagen.getWidth();
        int alto = laImagen.getHeight();
        BufferedImage imagenCombinada = new BufferedImage(ancho * 3, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagenCombinada.createGraphics();
        g.drawImage(rojo, 0, 0, null);
        g.drawImage(verde, ancho, 0, null);
        g.drawImage(azul, ancho * 2, 0, null);
        g.dispose();
        return imagenCombinada;
    }

    public Image getImagenGris() {
        return getImagenCanal(4);
    }

    private Image getImagenCanal(int queColor) {
        if (!isImageLoaded()) return null;
        int ancho = laImagen.getWidth();
        int alto = laImagen.getHeight();
        BufferedImage imagenCanal = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = laImagen.getRGB(x, y);
                Color color = null;
                switch (queColor) {
                    case 1:
                        color = new Color((pixel >> 16) & 0xFF, 0, 0);
                        break;
                    case 2:
                        color = new Color(0, (pixel >> 8) & 0xFF, 0);
                        break;
                    case 3:
                        color = new Color(0, 0, pixel & 0xFF);
                        break;
                    case 4:
                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = pixel & 0xFF;
                        int gris = (r + g + b) / 3;
                        color = new Color(gris, gris, gris);
                        break;
                }
                if (color != null) {
                    imagenCanal.setRGB(x, y, color.getRGB());
                }
            }
        }
        return imagenCanal;
    }

    public Image modificarBrillo(int factor, boolean esGris) {
        if (!isImageLoaded()) return null;
        int ancho = laImagen.getWidth();
        int alto = laImagen.getHeight();
        BufferedImage imagenModificada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                Color color = new Color(laImagen.getRGB(x, y));
                if (esGris) {
                    int gris = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                    int nuevoGris = Math.max(0, Math.min(255, gris + factor));
                    imagenModificada.setRGB(x, y, new Color(nuevoGris, nuevoGris, nuevoGris).getRGB());
                } else {
                    int r = Math.max(0, Math.min(255, color.getRed() + factor));
                    int g = Math.max(0, Math.min(255, color.getGreen() + factor));
                    int b = Math.max(0, Math.min(255, color.getBlue() + factor));
                    imagenModificada.setRGB(x, y, new Color(r, g, b).getRGB());
                }
            }
        }
        return imagenModificada;
    }

    public Image modificarContraste(double factor, boolean esColor) {
        if (!isImageLoaded()) return null;
        int ancho = laImagen.getWidth();
        int alto = laImagen.getHeight();
        BufferedImage imagenModificada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                Color color = new Color(laImagen.getRGB(x, y));
                if (esColor) {
                    int r = (int) Math.max(0, Math.min(255, factor * (color.getRed() - 128) + 128));
                    int g = (int) Math.max(0, Math.min(255, factor * (color.getGreen() - 128) + 128));
                    int b = (int) Math.max(0, Math.min(255, factor * (color.getBlue() - 128) + 128));
                    imagenModificada.setRGB(x, y, new Color(r, g, b).getRGB());
                } else {
                    int gris = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                    int nuevoGris = (int) Math.max(0, Math.min(255, factor * (gris - 128) + 128));
                    imagenModificada.setRGB(x, y, new Color(nuevoGris, nuevoGris, nuevoGris).getRGB());
                }
            }
        }
        return imagenModificada;
    }

    public int[] getHistograma() {
        if (!isImageLoaded()) return null;
        int[] histograma = new int[256];
        int ancho = laImagen.getWidth();
        int alto = laImagen.getHeight();
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                Color color = new Color(laImagen.getRGB(x, y));
                int gris = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                histograma[gris]++;
            }
        }
        return histograma;
    }

    /**
     * MÉTODO MODIFICADO: Ahora es flexible y puede aplicar varios tipos de ecualización.
     * @param tipo El tipo de ecualización a aplicar (UNIFORME, EXPONENCIAL, etc.).
     * @param params Parámetros adicionales como alpha o pot.
     * @return Un objeto con la imagen ecualizada y su nuevo histograma.
     */
    public ResultadoEcualizacion ecualizarHistograma(TipoEcualizacion tipo, double... params) {
        if (!isImageLoaded()) return null;

        int[] histOriginal = getHistograma();
        int ancho = laImagen.getWidth();
        int alto = laImagen.getHeight();
        int totalPixeles = ancho * alto;
        double[] cdf = getCDF(); // Reutilizamos el cálculo de la CDF

        // Crear la Tabla de Consulta (LUT) según el tipo de ecualización
        int[] lut = new int[256];
        double f_min = 0;
        double f_max = 255;

        switch (tipo) {
            case UNIFORME: // Ecuación 2.16
                for (int i = 0; i < 256; i++) {
                    lut[i] = (int) (f_min + (f_max - f_min) * cdf[i]);
                }
                break;
            case EXPONENCIAL: // Ecuación 2.17
                double alphaExp = params[0];
                for (int i = 0; i < 256; i++) {
                    double val = f_min - (1.0 / alphaExp) * Math.log(1.0 - cdf[i]);
                    lut[i] = (int) Math.round(val);
                }
                break;
            case RAYLEIGH: // Ecuación 2.18
                double alphaRay = params[0];
                for (int i = 0; i < 256; i++) {
                    double val = f_min + Math.sqrt(2 * Math.pow(alphaRay, 2) * Math.log(1.0 / (1.0 - cdf[i])));
                    lut[i] = (int) Math.round(val);
                }
                break;
            case HIPERBOLICA_RAICES: // Ecuación 2.19
                double pot = params[0];
                double f_max_pow = Math.pow(f_max, 1.0/pot);
                double f_min_pow = Math.pow(f_min, 1.0/pot);
                for (int i = 0; i < 256; i++) {
                    double base = ((f_max_pow - f_min_pow) * cdf[i]) + f_min_pow;
                    lut[i] = (int)Math.round(Math.pow(base, pot));
                }
                break;
            case HIPERBOLICA_LOGARITMICA: // Ecuación 2.20
                double f_min_log = 1; // f_min no puede ser 0
                for(int i = 0; i < 256; i++){
                    lut[i] = (int)Math.round(f_min_log * Math.pow(f_max / f_min_log, cdf[i]));
                }
                break;
        }

        // Crear la nueva imagen aplicando la LUT
        BufferedImage imgEcualizada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        int[] histEcualizado = new int[256];
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                Color color = new Color(laImagen.getRGB(x, y));
                int grisOriginal = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                int nuevoGris = lut[grisOriginal];
                nuevoGris = Math.max(0, Math.min(255, nuevoGris)); // Acotar por seguridad
                histEcualizado[nuevoGris]++;

                Color nuevoColor = new Color(nuevoGris, nuevoGris, nuevoGris);
                imgEcualizada.setRGB(x, y, nuevoColor.getRGB());
            }
        }
        return new ResultadoEcualizacion(imgEcualizada, histEcualizado);
    }

    public double[] getHistogramaNormalizado() {
        if (!isImageLoaded()) return null;

        int[] histFrecuencia = getHistograma();
        int totalPixeles = laImagen.getWidth() * laImagen.getHeight();
        double[] histProbabilidad = new double[256];

        for (int i = 0; i < 256; i++) {
            // P(i) = N(i) / Total de Píxeles
            histProbabilidad[i] = (double) histFrecuencia[i] / totalPixeles;
        }
        return histProbabilidad;
    }

    /**
     * MÉTODO NUEVO: Calcula la Función de Distribución Acumulada (CDF).
     * @return Un arreglo de 256 doubles con la probabilidad acumulada.
     */
    public double[] getCDF() {
        if (!isImageLoaded()) return null;

        double[] histProb = getHistogramaNormalizado();
        double[] cdf = new double[256];
        double sumaAcumulada = 0.0;

        for (int i = 0; i < 256; i++) {
            sumaAcumulada += histProb[i];
            cdf[i] = sumaAcumulada;
        }
        // Asegurarse de que el último valor sea exactamente 1.0 por si hay errores de precisión
        if (cdf.length > 0) {
            cdf[cdf.length - 1] = 1.0;
        }
        return cdf;
    }


    /**
     * MÉTODO NUEVO: Calcula las 5 propiedades estadísticas del histograma.
     * @return Un String formateado con todos los resultados.
     */
    public String getEstadisticasHistograma() {
        if (!isImageLoaded()) return "No hay imagen cargada.";

        double[] prob = getHistogramaNormalizado();
        if (prob == null) return "No se pudo calcular el histograma.";

        // 1. Media (Ecuación 2.3)
        double media = 0.0;
        for (int i = 0; i < 256; i++) {
            media += i * prob[i];
        }

        // 2. Varianza (Ecuación 2.4)
        double varianza = 0.0;
        for (int i = 0; i < 256; i++) {
            varianza += Math.pow(i - media, 2) * prob[i];
        }

        // 3. Asimetría (Ecuación 2.5)
        double asimetria = 0.0;
        for (int i = 0; i < 256; i++) {
            asimetria += Math.pow(i - media, 3) * prob[i];
        }

        // 4. Energía (Ecuación 2.6)
        double energia = 0.0;
        for (int i = 0; i < 256; i++) {
            energia += Math.pow(prob[i], 2);
        }

        // 5. Entropía (Ecuación 2.7)
        double entropia = 0.0;
        for (int i = 0; i < 256; i++) {
            if (prob[i] > 0) { // Evitar log(0)
                entropia -= prob[i] * (Math.log(prob[i]) / Math.log(2));
            }
        }

        // Formatear los resultados en un String para mostrarlos
        return String.format(
                "Propiedades Estadísticas del Histograma:\n\n" +
                        "1. Media (Brillo Promedio): %.4f\n" +
                        "2. Varianza (Contraste): %.4f\n" +
                        "3. Asimetría: %.4f\n" +
                        "4. Energía: %.4f\n" +
                        "5. Entropía: %.4f\n",
                media, varianza, asimetria, energia, entropia
        );
    }


}