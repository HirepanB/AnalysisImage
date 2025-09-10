package vista;

import control.ControlImagen;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class FrameBrilloInteractivo extends JFrame {
    private ControlImagen controlImagen;
    private PanelDeImagen panelDeImagen;
    private JSlider sliderBrillo;
    private JLabel labelValorBrillo;

    public FrameBrilloInteractivo(String nombreArchivo) {
        super("Brillo Interactivo");
        this.initComponents(nombreArchivo);
    }

    private void initComponents(String nombreArchivo) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Añade un poco de espacio

        this.controlImagen = new ControlImagen(nombreArchivo);

        // La imagen inicial se muestra con un brillo de 0 (sin cambios)
        Image imagenInicial = controlImagen.getImagenConBrillo(0);
        this.panelDeImagen = new PanelDeImagen(imagenInicial);
        add(this.panelDeImagen, BorderLayout.CENTER);

        // --- Configuración del Slider ---
        // Rango de -100 a 100, empezando en 0.
        sliderBrillo = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
        sliderBrillo.setMajorTickSpacing(50); // Marcas principales cada 50
        sliderBrillo.setMinorTickSpacing(10); // Marcas secundarias cada 10
        sliderBrillo.setPaintTicks(true);    // Muestra las marcas
        sliderBrillo.setPaintLabels(true);   // Muestra los números

        // --- Configuración de la Etiqueta ---
        labelValorBrillo = new JLabel("Brillo: 0");
        labelValorBrillo.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel para los controles en la parte inferior de la ventana
        JPanel panelControles = new JPanel(new BorderLayout());
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Añade margen
        panelControles.add(sliderBrillo, BorderLayout.CENTER);
        panelControles.add(labelValorBrillo, BorderLayout.NORTH);
        add(panelControles, BorderLayout.SOUTH);

        // --- Lógica del Slider ---
        sliderBrillo.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Obtiene el nuevo valor del slider
                int nuevoBrillo = sliderBrillo.getValue();

                // Actualiza la etiqueta
                labelValorBrillo.setText("Brillo: " + nuevoBrillo);

                // Genera la nueva imagen con el brillo modificado
                Image imagenModificada = controlImagen.getImagenConBrillo(nuevoBrillo);

                // Actualiza la imagen en el panel usando el nuevo método
                panelDeImagen.setImagen(imagenModificada);
            }
        });

        // Ajustamos el tamaño y la posición para que no se encime
        setSize(controlImagen.getAncho() + 50, controlImagen.getAlto() + 150);
        setLocation(200, 200);
        setVisible(true);
    }
}
