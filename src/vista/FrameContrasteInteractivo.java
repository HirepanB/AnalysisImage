package vista;

import control.ControlImagen;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.text.DecimalFormat;

public class FrameContrasteInteractivo extends JFrame {
    private ControlImagen controlImagen;
    private PanelDeImagen panelDeImagen;
    private JSlider sliderContraste;
    private JLabel labelValorContraste;

    public FrameContrasteInteractivo(String nombreArchivo) {
        super("Contraste Interactivo");
        this.initComponents(nombreArchivo);
    }

    private void initComponents(String nombreArchivo) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        this.controlImagen = new ControlImagen(nombreArchivo);

        // La imagen inicial se muestra con contraste de 1.0x (sin cambios)
        Image imagenInicial = controlImagen.getImagenConContraste(1.0);
        this.panelDeImagen = new PanelDeImagen(imagenInicial);
        add(this.panelDeImagen, BorderLayout.CENTER);

        // --- Configuración del Slider ---
        // Rango de 0 a 300, empezando en 100. (Representa un factor de 0.0x a 3.0x)
        sliderContraste = new JSlider(JSlider.HORIZONTAL, 0, 300, 100);
        sliderContraste.setMajorTickSpacing(100);
        sliderContraste.setMinorTickSpacing(25);
        sliderContraste.setPaintTicks(true);
        sliderContraste.setPaintLabels(true);

        // --- Configuración de la Etiqueta ---
        labelValorContraste = new JLabel("Contraste: 1.00x");
        labelValorContraste.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel para los controles en la parte inferior
        JPanel panelControles = new JPanel(new BorderLayout());
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelControles.add(sliderContraste, BorderLayout.CENTER);
        panelControles.add(labelValorContraste, BorderLayout.NORTH);
        add(panelControles, BorderLayout.SOUTH);

        // --- Lógica del Slider ---
        sliderContraste.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Obtiene el valor del slider y lo convierte a un factor double
                int valorSlider = sliderContraste.getValue();
                double nuevoFactor = valorSlider / 100.0;

                // Actualiza la etiqueta con formato
                DecimalFormat df = new DecimalFormat("#.##");
                labelValorContraste.setText("Contraste: " + df.format(nuevoFactor) + "x");

                // Genera la nueva imagen con el contraste modificado
                Image imagenModificada = controlImagen.getImagenConContraste(nuevoFactor);

                // Actualiza la imagen en el panel
                panelDeImagen.setImagen(imagenModificada);
            }
        });

        setSize(controlImagen.getAncho() + 50, controlImagen.getAlto() + 150);
        setLocation(300, 300); // Posición diferente para no encimarse
        setVisible(true);
    }
}
