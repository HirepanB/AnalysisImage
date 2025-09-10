package vista;

import control.ControlImagen;
import javax.swing.*;
import java.awt.*;

public class FrameVisorImagen extends JFrame {
    private PanelDeImagen panel;
    private ControlImagen controlImagen;

    public FrameVisorImagen(String nombreArchivo) {
        super("Imagen Original");
        this.initComponents(nombreArchivo);
    }

    private void initComponents(String nombreArchivo) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contenedor = this.getContentPane();
        contenedor.setLayout(new BorderLayout());

        this.controlImagen = new ControlImagen(nombreArchivo);

        Image imagenOriginal = this.controlImagen.getImagen(5);
        this.panel = new PanelDeImagen(imagenOriginal);
        contenedor.add(this.panel, BorderLayout.CENTER);

        this.setSize(this.controlImagen.getAncho(), this.controlImagen.getAlto() + 40);

        this.setLocationByPlatform(true);
        this.setVisible(true);
    }
}