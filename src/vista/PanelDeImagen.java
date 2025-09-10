package vista;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class PanelDeImagen extends JPanel {

    private Image imagen;

    public PanelDeImagen(Image img) {
        this.imagen = img;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //pinta el fondo
        //Pone la imagen en su tamanio normal.
        if (imagen != null) g.drawImage(imagen, 1, 1, this);
    }
}