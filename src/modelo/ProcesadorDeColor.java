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
                dest.setRGB(x, y, new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue()).getRGB());
            }
        }
        return dest;
    }

    public static BufferedImage cmyARgb(BufferedImage src) {
        return rgbACmy(src); // La transformación es idéntica
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

                if (k == 255) {
                    dest.setRGB(x, y, new Color(0, 0, k).getRGB());
                } else {
                    int c_ = 255 * (c - k) / (255 - k);
                    int m_ = 255 * (m - k) / (255 - k);
                    int y__ = 255 * (y_ - k) / (255 - k);
                    // --- LÍNEA CORREGIDA ---
                    dest.setRGB(x, y, new Color(c_, m_, y__).getRGB());
                }
            }
        }
        return dest;
    }

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
                int y = k;

                dest.setRGB(j, i, new Color(c, m, y).getRGB());
            }
        }
        return dest;
    }

    // --- RGB -> YIQ (Devuelve datos completos) ---
    public static PixelYIQ[][] rgbAYiq(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        PixelYIQ[][] matrizYiq = new PixelYIQ[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = new Color(src.getRGB(x, y));
                double r = c.getRed() / 255.0;
                double g = c.getGreen() / 255.0;
                double b = c.getBlue() / 255.0;

                double y_ = 0.299 * r + 0.587 * g + 0.114 * b;
                double i = 0.596 * r - 0.274 * g - 0.322 * b;
                double q = 0.211 * r - 0.523 * g + 0.312 * b;
                matrizYiq[y][x] = new PixelYIQ(y_, i, q);
            }
        }
        return matrizYiq;
    }

    // --- YIQ -> RGB (Usa datos completos) ---
    public static BufferedImage yiqARgb(PixelYIQ[][] matrizYiq) {
        int h = matrizYiq.length;
        int w = matrizYiq[0].length;
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                PixelYIQ p = matrizYiq[y][x];
                double r = p.y + 0.956 * p.i + 0.621 * p.q;
                double g = p.y - 0.272 * p.i - 0.647 * p.q;
                double b = p.y - 1.106 * p.i + 1.703 * p.q;

                int R = (int) Math.max(0, Math.min(255, r * 255.0));
                int G = (int) Math.max(0, Math.min(255, g * 255.0));
                int B = (int) Math.max(0, Math.min(255, b * 255.0));
                dest.setRGB(x, y, new Color(R, G, B).getRGB());
            }
        }
        return dest;
    }

    // --- RGB <-> HSV (Usando las funciones nativas de Java) ---
    public static BufferedImage rgbAHsv(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = new Color(src.getRGB(x, y));
                float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
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

    // --- RGB -> HSI (Devuelve datos completos) ---
    public static PixelHSI[][] rgbAHsi(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        PixelHSI[][] matrizHsi = new PixelHSI[h][w];
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
                double S = (r + g + b == 0) ? 0 : 1 - (3.0 / (r + g + b)) * min;
                double I = (r + g + b) / 3.0;
                matrizHsi[y][x] = new PixelHSI(H, S, I);
            }
        }
        return matrizHsi;
    }

    // --- HSI -> RGB (Usa datos completos) ---
    public static BufferedImage hsiARgb(PixelHSI[][] matrizHsi) {
        int h = matrizHsi.length;
        int w = matrizHsi[0].length;
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                PixelHSI p = matrizHsi[y][x];
                double r, g, b;
                double h_rad = p.h;

                if (h_rad >= 0 && h_rad < 2 * Math.PI / 3) { // Sector RG
                    b = p.i * (1 - p.s);
                    r = p.i * (1 + (p.s * Math.cos(h_rad)) / Math.cos(Math.PI / 3 - h_rad));
                    g = 3 * p.i - (r + b);
                } else if (h_rad >= 2 * Math.PI / 3 && h_rad < 4 * Math.PI / 3) { // Sector GB
                    h_rad = h_rad - 2 * Math.PI / 3;
                    r = p.i * (1 - p.s);
                    g = p.i * (1 + (p.s * Math.cos(h_rad)) / Math.cos(Math.PI / 3 - h_rad));
                    b = 3 * p.i - (r + g);
                } else { // Sector BR
                    h_rad = h_rad - 4 * Math.PI / 3;
                    g = p.i * (1 - p.s);
                    b = p.i * (1 + (p.s * Math.cos(h_rad)) / Math.cos(Math.PI / 3 - h_rad));
                    r = 3 * p.i - (g + b);
                }
                int R = (int) Math.max(0, Math.min(255, r * 255.0));
                int G = (int) Math.max(0, Math.min(255, g * 255.0));
                int B = (int) Math.max(0, Math.min(255, b * 255.0));
                dest.setRGB(x, y, new Color(R, G, B).getRGB());
            }
        }
        return dest;
    }
}