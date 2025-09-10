package vista;

import control.ControlImagen;
import javax.swing.*;
import java.awt.*;

public class FrameBrillo extends JFrame {
    private PanelDeImagen panel;
    private ControlImagen controlImagen;

    public FrameBrillo(String nombreArchivo, int factorBrillo) {
        super("Imagen con Brillo Modificado (Factor: " + factorBrillo + ")");
        this.initComponents(nombreArchivo, factorBrillo);
    }

    private void initComponents(String nombreArchivo, int factorBrillo) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contenedor = this.getContentPane();
        contenedor.setLayout(new BorderLayout());

        this.controlImagen = new ControlImagen(nombreArchivo);

        Image imagenModificada = this.controlImagen.getImagenConBrillo(factorBrillo);

        this.panel = new PanelDeImagen(imagenModificada);
        contenedor.add(this.panel, BorderLayout.CENTER);

        this.setSize(this.controlImagen.getAncho(), this.controlImagen.getAlto() + 40);

        this.setLocation(100, 100);
        this.setVisible(true);
    }
}