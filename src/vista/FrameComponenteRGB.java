package vista;

import control.ControlImagen;
import javax.swing.*;
import java.awt.*;

public class FrameComponenteRGB extends JFrame {

    private PanelDeImagen panelRojo;
    private PanelDeImagen panelVerde;
    private PanelDeImagen panelAzul;
    private ControlImagen controlImagen;

    public FrameComponenteRGB(String nombreArchivo) {
        super("Visor de Componentes: RGB");
        this.initComponents(nombreArchivo);
    }

    private void initComponents(String nombreArchivo) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contenedor = this.getContentPane();

        contenedor.setLayout(new GridLayout(1, 3));

        this.controlImagen = new ControlImagen(nombreArchivo);

        Image imagenRoja = this.controlImagen.getImagen(1);
        Image imagenVerde = this.controlImagen.getImagen(2);
        Image imagenAzul = this.controlImagen.getImagen(3);

        this.panelRojo = new PanelDeImagen(imagenRoja);
        contenedor.add(this.panelRojo);

        this.panelVerde = new PanelDeImagen(imagenVerde);
        contenedor.add(this.panelVerde);

        this.panelAzul = new PanelDeImagen(imagenAzul);
        contenedor.add(this.panelAzul);

        this.setSize(this.controlImagen.getAncho() * 3, this.controlImagen.getAlto() + 40);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}