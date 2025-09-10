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
        super.paintComponent(g);
        if (this.imagen != null) {
            // MEJORA: La imagen ahora se escala para ajustarse al tamaño del panel.
            g.drawImage(this.imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * MÉTODO NUEVO: Permite cambiar la imagen que se muestra en el panel.
     * @param nuevaImagen La nueva imagen a mostrar.
     */
    public void setImagen(Image nuevaImagen) {
        this.imagen = nuevaImagen;
        // Forza al panel a redibujarse para mostrar la nueva imagen.
        repaint();
    }
}
