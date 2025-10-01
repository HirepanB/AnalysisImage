package unidaduno;

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
                    case 1: color = new Color((pixel >> 16) & 0xFF, 0, 0); break;
                    case 2: color = new Color(0, (pixel >> 8) & 0xFF, 0); break;
                    case 3: color = new Color(0, 0, pixel & 0xFF); break;
                    case 4:
                        int r = (pixel >> 16) & 0xFF, g = (pixel >> 8) & 0xFF, b = pixel & 0xFF;
                        int gris = (r + g + b) / 3;
                        color = new Color(gris, gris, gris);
                        break;
                }
                if (color != null) imagenCanal.setRGB(x, y, color.getRGB());
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
}