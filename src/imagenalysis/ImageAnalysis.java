package imagenalysis;

import javax.swing.SwingUtilities;
import vista.FrameBrilloInteractivo;
import vista.FrameComponenteRGB;
import vista.FrameContrasteInteractivo; // 1. Se importa la nueva clase para el contraste
import vista.FrameNivelesDeGris;
import vista.FrameVisorImagen;

public class ImageAnalysis {
    public ImageAnalysis() {
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Se mantienen todas las ventanas existentes
                new FrameComponenteRGB("CocoB.jpeg");
                new FrameVisorImagen("CocoB.jpeg");
                new FrameNivelesDeGris("CocoB.jpeg");
                new FrameBrilloInteractivo("CocoB.jpeg");

                // 2. Se añade la línea que crea la nueva ventana para el contraste interactivo.
                new FrameContrasteInteractivo("CocoB.jpeg");
            }
        });
    }
}

