package imagenalysis;

import javax.swing.SwingUtilities;
import vista.VentanaPrincipal;

public class ImageAnalysis {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal();
            }
        });
    }
}