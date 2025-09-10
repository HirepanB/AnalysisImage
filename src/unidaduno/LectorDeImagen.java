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

        // SOLUCIÓN FINAL: Asegúrate de que esta línea solo contenga el nombre de la carpeta.
        path = "imagenes/" + this.nombreArchivo;

        System.out.println("Intentando cargar imagen desde: " + path);
        try {
            FileInputStream input = new FileInputStream(new File(path));
            laImagen = ImageIO.read(input);
            colorModel = laImagen.getColorModel();
            tipo = laImagen.getType();
        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo de imagen: " + ioe);
        }
    }

    // ... el resto del código es el mismo ...

    public BufferedImage getBufferedImage() {
        return laImagen;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

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
                    case 1: // rojo
                        int R = (pixel & 0x00ff0000) >> 16;
                        color = new Color(R, 0, 0);
                        imagenInt[y][x] = color.getRGB();
                        break;
                    case 2: // verde
                        int G = (pixel & 0x0000ff00) >> 8;
                        color = new Color(0, G, 0);
                        imagenInt[y][x] = color.getRGB();
                        break;
                    case 3: // azul
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
        } catch (NegativeArraySizeException e) {
            System.out.println(" Error alto, ancho o ambos negativos" + "en convierteInt2DAInt1D(double[][])" + e);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(" Error desbordamiento en bufferInt" + "en " + e);
        } catch (NullPointerException e) {
            System.out.println(" Error bufferInt nulo" + " en convierteInt2DAInt1D(double[][]) " + e);
        }
        return bufferInt;
    }

    public int getAlto() {
        return laImagen.getHeight();
    }

    public int getAncho() {
        return laImagen.getWidth();
    }
}