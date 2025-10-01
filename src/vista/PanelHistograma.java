package vista;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class PanelHistograma extends JPanel {
    private final int[] datosFrecuencia;
    private final double[] datosProbabilidad;
    private final boolean esProbabilidad;
    private final boolean dibujarComoLinea;
    private double maxValorY = 0;
    private static final int PADDING = 40;

    // Constructores existentes (sin cambios)
    public PanelHistograma(int[] datos) {
        this(datos, null, false, false);
    }
    public PanelHistograma(double[] datos) {
        this(null, datos, true, false);
    }

    /**
     * CONSTRUCTOR NUEVO: Acepta un booleano para dibujar como línea.
     */
    public PanelHistograma(double[] datos, boolean esLinea) {
        this(null, datos, true, esLinea);
    }

    private PanelHistograma(int[] frec, double[] prob, boolean esProb, boolean esLinea) {
        this.datosFrecuencia = frec;
        this.datosProbabilidad = prob;
        this.esProbabilidad = esProb;
        this.dibujarComoLinea = esLinea;

        if (esProbabilidad) {
            maxValorY = esLinea ? 1.0 : 0.0; // La CDF siempre llega a 1.0
            if (!esLinea) {
                for (double p : prob) if (p > maxValorY) maxValorY = p;
            }
        } else {
            for (int f : frec) if (f > maxValorY) maxValorY = f;
        }
        setBorder(BorderFactory.createEtchedBorder());
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if ((datosFrecuencia == null && datosProbabilidad == null) || maxValorY == 0) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int anchoPanel = getWidth();
        int altoPanel = getHeight();
        int anchoGrafico = anchoPanel - 2 * PADDING;
        int altoGrafico = altoPanel - 2 * PADDING;

        if (dibujarComoLinea) {
            // --- Lógica para dibujar Gráfico de Línea (CDF) ---
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));
            for (int i = 0; i < 255; i++) {
                int x1 = PADDING + (int) (i * ((double) anchoGrafico / 255.0));
                int y1 = (altoPanel - PADDING) - (int) (datosProbabilidad[i] * altoGrafico);
                int x2 = PADDING + (int) ((i + 1) * ((double) anchoGrafico / 255.0));
                int y2 = (altoPanel - PADDING) - (int) (datosProbabilidad[i+1] * altoGrafico);
                g2d.drawLine(x1, y1, x2, y2);
            }
        } else {
            // --- Lógica para dibujar Gráfico de Barras (Histograma) ---
            g2d.setColor(new Color(70, 130, 180));
            float anchoBarra = (float) anchoGrafico / 256.0f;
            for (int i = 0; i < 256; i++) {
                double valorActual = esProbabilidad ? datosProbabilidad[i] : datosFrecuencia[i];
                int alturaBarra = (int) ((valorActual / maxValorY) * (altoGrafico));
                int x = PADDING + (int) (i * anchoBarra);
                int y = altoPanel - PADDING - alturaBarra;
                g2d.fillRect(x, y, (int) Math.ceil(anchoBarra), alturaBarra);
            }
        }

        // --- Dibujar Ejes y Escalas ---
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(PADDING, altoPanel - PADDING, anchoPanel - PADDING, altoPanel - PADDING); // Eje X
        g2d.drawLine(PADDING, PADDING, PADDING, altoPanel - PADDING); // Eje Y
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));

        // Etiquetas Eje Y
        String maxLabel = esProbabilidad ? new DecimalFormat("#.##").format(maxValorY) : String.valueOf((int) maxValorY);
        g2d.drawString(maxLabel, 5, PADDING);
        g2d.drawString("0", 15, altoPanel - PADDING + 5);

        // Etiquetas Eje X
        g2d.drawString("0", PADDING - 3, altoPanel - PADDING + 15);
        g2d.drawString("128", PADDING + anchoGrafico / 2 - 10, altoPanel - PADDING + 15);
        g2d.drawString("255", anchoPanel - PADDING - 10, altoPanel - PADDING + 15);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String tituloEjeX = "Nivel de Intensidad (Gris)";
        int anchoTexto = g2d.getFontMetrics().stringWidth(tituloEjeX);
        g2d.drawString(tituloEjeX, (anchoPanel - anchoTexto) / 2, altoPanel - 5);
    }
}