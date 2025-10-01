package vista;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class PanelDeImagen extends JPanel {

    private Image imagen;

    public PanelDeImagen(Image img) {
        this.imagen = img;
    }

    // --- CAMBIO 1: Se anula el método getPreferredSize ---
    // Le decimos al panel que su tamaño ideal es el tamaño real de la imagen.
    // Esto es crucial para que el JScrollPane sepa cuándo mostrar las barras.
    @Override
    public Dimension getPreferredSize() {
        if (imagen != null) {
            return new Dimension(imagen.getWidth(this), imagen.getHeight(this));
        }
        // Si no hay imagen, usa un tamaño por defecto.
        return new Dimension(200, 200);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.imagen != null) {
            // --- CAMBIO 2: Se dibuja la imagen en su tamaño 1:1 ---
            // Ya no se escala a getWidth() y getHeight(). Se dibuja en su tamaño natural.
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(this.imagen, 0, 0, this);
        }
    }

    public void setImagen(Image nuevaImagen) {
        this.imagen = nuevaImagen;
        // Revalida el componente para que el JScrollPane ajuste las barras si es necesario.
        revalidate();
        repaint();
    }
}