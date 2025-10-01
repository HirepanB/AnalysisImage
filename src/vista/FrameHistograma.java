package vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FrameHistograma extends JFrame {

    // Constructor para histograma de frecuencia
    public FrameHistograma(int[] histOriginal) {
        super("Histograma de Frecuencia");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panelHistOriginal = new PanelHistograma(histOriginal);
        panelHistOriginal.setBorder(new TitledBorder("Histograma de Luminancia"));
        add(panelHistOriginal);
        setSize(512 + 50, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Constructor para histograma de probabilidad
    public FrameHistograma(double[] datos, String titulo) {
        this(datos, titulo, false); // Llama al nuevo constructor, modo barras por defecto
    }

    /**
     * CONSTRUCTOR NUEVO Y PRINCIPAL: Acepta un booleano para dibujar como línea.
     */
    public FrameHistograma(double[] datos, String titulo, boolean esLinea) {
        super(titulo);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panelHistProb = new PanelHistograma(datos, esLinea); // Pasa el flag
        panelHistProb.setBorder(new TitledBorder(titulo));
        add(panelHistProb);
        setSize(512 + 50, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // El constructor para la ecualización no se modifica.
    public FrameHistograma(Image imgOriginalGris, int[] histOriginal, Image imgEcualizada, int[] histEcualizado) {
        super("Resultado de Ecualización de Histograma");
        /* ... */
    }
}