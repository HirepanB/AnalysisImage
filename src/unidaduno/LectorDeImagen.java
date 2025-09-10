package unidaduno;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class LectorDeImagen {
    private ColorModel colorModel;
    private int tipo;
    private BufferedImage laImagen;
    private String path;
    private String nombreArchivo;

    public LectorDeImagen(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
        path = "imagenes/" + this.nombreArchivo;
        System.out.println("Intentando cargar imagen desde: " + path);
        try {
            FileInputStream input = new FileInputStream(new File(path));
            laImagen = ImageIO.read(input);
            if (laImagen != null) {
                colorModel = laImagen.getColorModel();
                tipo = laImagen.getType();
            }
        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo de imagen: " + ioe);
        }
    }

    // ... los métodos existentes no se modifican ...
    public BufferedImage getBufferedImage() { return laImagen; }
    public void setPath(String path) { this.path = path; }
    public String getPath() { return path; }
    public String getNombreArchivo() { return nombreArchivo; }
    public int[][] getImagenInt(int queColor) {
        int alto = laImagen.getHeight();
        int ancho = laImagen.getWidth();
        int[][] imagenInt = new int[alto][ancho];
        int pixel;
        Color color = null;

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                pixel = laImagen.getRGB(x, y);
                switch (queColor) {
                    case 1:
                        int R = (pixel & 0x00ff0000) >> 16;
                        color = new Color(R, 0, 0);
                        imagenInt[y][x] = color.getRGB();
                        break;
                    case 2:
                        int G = (pixel & 0x0000ff00) >> 8;
                        color = new Color(0, G, 0);
                        imagenInt[y][x] = color.getRGB();
                        break;
                    case 3:
                        int B = pixel & 0x000000ff;
                        color = new Color(0, 0, B);
                        imagenInt[y][x] = color.getRGB();
                        break;
                    case 4:
                        int rojo = (pixel & 0x00ff0000) >> 16;
                        int verde = (pixel & 0x0000ff00) >> 8;
                        int azul = pixel & 0x000000ff;
                        double gris = (double) (rojo + verde + azul) / 3.0;
                        color = new Color((int) gris, (int) gris, (int) gris);
                        imagenInt[y][x] = color.getRGB();
                        break;
                    case 5:
                        imagenInt[y][x] = laImagen.getRGB(x, y);
                        break;
                }
            }
        }
        return imagenInt;
    }
    public Image getImage(int queColor) {
        Image imagenLocal;
        int[][] imagenInt = getImagenInt(queColor);
        int alto = laImagen.getHeight();
        int ancho = laImagen.getWidth();
        JFrame frameTmp = new JFrame();
        imagenLocal = frameTmp.createImage(new MemoryImageSource(ancho, alto, convertirInt2DAInt1D(imagenInt, ancho, alto), 0, ancho));
        return imagenLocal;
    }
    public int[] convertirInt2DAInt1D(int[][] matriz, int ancho, int alto) {
        int index = 0;
        int[] bufferInt = null;
        try {
            bufferInt = new int[ancho * alto];
            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    bufferInt[index++] = matriz[y][x];
                }
            }
        } catch (Exception e) {
            System.out.println("Error en la conversión: " + e);
        }
        return bufferInt;
    }
    public int getAlto() { return laImagen.getHeight(); }
    public int getAncho() { return laImagen.getWidth(); }
    public Image modificarBrillo(int factor) {
        if (laImagen == null) return null;
        int ancho = laImagen.getWidth();
        int alto = laImagen.getHeight();
        BufferedImage imagenModificada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = laImagen.getRGB(x, y);
                Color color = new Color(pixel);
                int r = color.getRed() + factor;
                int g = color.getGreen() + factor;
                int b = color.getBlue() + factor;
                r = Math.max(0, Math.min(255, r));
                g = Math.max(0, Math.min(255, g));
                b = Math.max(0, Math.min(255, b));
                Color nuevoColor = new Color(r, g, b);
                imagenModificada.setRGB(x, y, nuevoColor.getRGB());
            }
        }
        return imagenModificada;
    }

    /**
     * MÉTODO NUEVO: Modifica el contraste de la imagen en niveles de gris.
     * @param factor Un valor double. >1 para más contraste, <1 para menos. 1 es sin cambios.
     * @return Una nueva imagen en escala de grises con el contraste modificado.
     */
    public Image modificarContraste(double factor) {
        if (laImagen == null) return null;

        int ancho = laImagen.getWidth();
        int alto = laImagen.getHeight();
        BufferedImage imagenModificada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                Color color = new Color(laImagen.getRGB(x, y));

                // 1. Convertir a gris
                int grisActual = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                // 2. Aplicar la fórmula de contraste
                double nuevoGrisDouble = factor * (grisActual - 128) + 128;

                // 3. Asegurar que el valor esté en el rango 0-255
                int nuevoGrisInt = (int) Math.max(0, Math.min(255, nuevoGrisDouble));

                // 4. Crear el nuevo color de gris y asignarlo
                Color nuevoColorGris = new Color(nuevoGrisInt, nuevoGrisInt, nuevoGrisInt);
                imagenModificada.setRGB(x, y, nuevoColorGris.getRGB());
            }
        }
        return imagenModificada;
    }
}
