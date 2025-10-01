package vista;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FrameEcualizacion extends JFrame {

    public FrameEcualizacion(Image imgOriginalGris, int[] histOriginal, Image imgEcualizada, int[] histEcualizado) {
        super("Resultado de Ecualización de Histograma");
        initComponents(imgOriginalGris, histOriginal, imgEcualizada, histEcualizado);
    }

    private void initComponents(Image imgOriginalGris, int[] histOriginal, Image imgEcualizada, int[] histEcualizado) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLayout(new GridLayout(2, 2, 10, 10)); // Cuadrícula de 2x2 con espacio

        // Panel 1: Imagen Original en Gris
        JPanel panelImgOriginal = new PanelDeImagen(imgOriginalGris);
        panelImgOriginal.setBorder(new TitledBorder("Imagen en Gris Original"));
        add(panelImgOriginal);

        // Panel 2: Imagen Ecualizada
        JPanel panelImgEcualizada = new PanelDeImagen(imgEcualizada);
        panelImgEcualizada.setBorder(new TitledBorder("Imagen Ecualizada"));
        add(panelImgEcualizada);

        // Panel 3: Histograma Original
        JPanel panelHistOriginal = new PanelHistograma(histOriginal);
        panelHistOriginal.setBorder(new TitledBorder("Histograma Original"));
        add(panelHistOriginal);

        // Panel 4: Histograma Ecualizado
        JPanel panelHistEcualizado = new PanelHistograma(histEcualizado);
        panelHistEcualizado.setBorder(new TitledBorder("Histograma Ecualizado"));
        add(panelHistEcualizado);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
