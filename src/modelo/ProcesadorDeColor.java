package modelo;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ProcesadorDeColor {

    // --- RGB <-> CMY ---
    public static BufferedImage rgbACmy(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = new Color(src.getRGB(x, y));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                // C = 255 - R, etc.
                dest.setRGB(x, y, new Color(255 - r, 255 - g, 255 - b).getRGB());
            }
        }
        return dest;
    }

    public static BufferedImage cmyARgb(BufferedImage src) {
        // La transformación es idéntica
        return rgbACmy(src);
    }

    // --- CMY <-> CMYK ---
    public static BufferedImage cmyACmyk(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color cmy = new Color(src.getRGB(x, y));
                int c = cmy.getRed();
                int m = cmy.getGreen();
                int y_ = cmy.getBlue();

                int k = Math.min(c, Math.min(m, y_));

                // Representamos K en el canal azul, C en el rojo, M en el verde
                // La Y se pierde visualmente pero se usa para la reconversión.
                // Esta es una APROXIMACIÓN VISUAL.
                if (k == 255) { // Evitar división por cero
                    dest.setRGB(x, y, new Color(0, 0, k).getRGB());
                } else {
                    int c_ = 255 * (c - k) / (255 - k);
                    int m_ = 255 * (m - k) / (255 - k);
                    dest.setRGB(x, y, new Color(c_, m_, k).getRGB());
                }
            }
        }
        return dest;
    }

    // NOTA: La conversión CMYK a CMY no se puede hacer visualmente de forma perfecta
    // desde nuestra imagen de 3 canales. Esto es una simplificación.
    public static BufferedImage cmykACmy(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Color cmyk = new Color(src.getRGB(j, i));
                int c_ = cmyk.getRed();
                int m_ = cmyk.getGreen();
                int k = cmyk.getBlue();

                int c = (c_ * (255 - k) / 255) + k;
                int m = (m_ * (255 - k) / 255) + k;
                // Asumimos Y = 0 para la visualización
                int y = k;

                dest.setRGB(j, i, new Color(c, m, y).getRGB());
            }
        }
        return dest;
    }

    // --- RGB <-> YIQ ---
    public static BufferedImage rgbAYiq(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = new Color(src.getRGB(x, y));
                float r = c.getRed() / 255f;
                float g = c.getGreen() / 255f;
                float b = c.getBlue() / 255f;

                float y_ = 0.299f * r + 0.587f * g + 0.114f * b;
                float i = 0.596f * r - 0.274f * g - 0.322f * b;
                float q = 0.211f * r - 0.523f * g + 0.312f * b;

                // Mapeo para visualización (Y a todos los canales para luminancia)
                int Y_vis = (int)(y_ * 255);
                dest.setRGB(x, y, new Color(Y_vis, Y_vis, Y_vis).getRGB());
            }
        }
        return dest;
    }

    public static BufferedImage yiqARgb(BufferedImage src) {
        // La conversión inversa desde una imagen YIQ visualizada (solo Y) no es posible
        // sin la información de I y Q. Devolvemos la imagen en grises.
        return src;
    }

    // --- RGB <-> HSV ---
    public static BufferedImage rgbAHsv(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = new Color(src.getRGB(x, y));
                float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                // Mapeamos H, S, V a R, G, B para visualización
                int H = (int)(hsv[0] * 255);
                int S = (int)(hsv[1] * 255);
                int V = (int)(hsv[2] * 255);
                dest.setRGB(x, y, new Color(H, S, V).getRGB());
            }
        }
        return dest;
    }

    public static BufferedImage hsvARgb(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color hsvColor = new Color(src.getRGB(x, y));
                float h_ = hsvColor.getRed() / 255f;
                float s_ = hsvColor.getGreen() / 255f;
                float v_ = hsvColor.getBlue() / 255f;
                dest.setRGB(x, y, Color.HSBtoRGB(h_, s_, v_));
            }
        }
        return dest;
    }

    // --- RGB <-> HSI ---
    // HSI es similar a HSV pero matemáticamente distinto, más complejo
    public static BufferedImage rgbAHsi(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = new Color(src.getRGB(x, y));
                double r = c.getRed() / 255.0;
                double g = c.getGreen() / 255.0;
                double b = c.getBlue() / 255.0;

                double num = 0.5 * ((r - g) + (r - b));
                double den = Math.sqrt((r - g) * (r - g) + (r - b) * (g - b));
                double theta = Math.acos(num / (den + 1e-10));

                double H = (b <= g) ? theta : (2 * Math.PI - theta);
                double min = Math.min(r, Math.min(g, b));
                double S = 1 - (3.0 / (r + g + b + 1e-10)) * min;
                double I = (r + g + b) / 3.0;

                // Mapeo para visualización
                int H_vis = (int) (H / (2 * Math.PI) * 255);
                int S_vis = (int) (S * 255);
                int I_vis = (int) (I * 255);

                dest.setRGB(x, y, new Color(H_vis, S_vis, I_vis).getRGB());
            }
        }
        return dest;
    }

    public static BufferedImage hsiARgb(BufferedImage src) {
        // La conversión inversa es muy compleja y no trivial.
        // Por simplicidad, devolvemos la misma imagen como demostración.
        return src;
    }
}
