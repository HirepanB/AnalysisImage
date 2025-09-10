package vista;

import control.ControlImagen;
import javax.swing.*;
import java.awt.*;

public class FrameNivelesDeGris extends JFrame {
    private PanelDeImagen panel;
    private ControlImagen controlImagen;

    public FrameNivelesDeGris(String nombreArchivo) {
        super("Imagen en Niveles de Gris"); // Título para la nueva ventana
        this.initComponents(nombreArchivo);
    }

    private void initComponents(String nombreArchivo) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contenedor = this.getContentPane();
        contenedor.setLayout(new BorderLayout());

        this.controlImagen = new ControlImagen(nombreArchivo);

        // --- CAMBIO CLAVE ---
        // Solicitamos la imagen con el código '4', que corresponde a los niveles de gris
        // según la lógica en tu clase LectorDeImagen.
        Image imagenGris = this.controlImagen.getImagen(4);

        this.panel = new PanelDeImagen(imagenGris);
        contenedor.add(this.panel, BorderLayout.CENTER);

        this.setSize(this.controlImagen.getAncho(), this.controlImagen.getAlto() + 40);

        // Posiciona esta ventana ligeramente diferente para que no se encime con las otras
        this.setLocation(50, 50);
        this.setVisible(true);
    }
}
