package imagenalysis;

import javax.swing.SwingUtilities;
import vista.FrameComponenteRGB;
import vista.FrameVisorImagen;

public class ImageAnalysis {
    public ImageAnalysis() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Crea la ventana para los componentes RGB
                new FrameComponenteRGB("CocoB.jpeg");

                // Crea la ventana para la imagen original
                new FrameVisorImagen("CocoB.jpeg");
            }
        });
    }
}