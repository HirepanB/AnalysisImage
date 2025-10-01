package vista;

import modelo.ModeloColor;
import modelo.ProcesadorDeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VentanaResultado extends JFrame {

    private BufferedImage imagenActual;
    private ModeloColor modeloActual;

    public VentanaResultado(BufferedImage imagen, String titulo, ModeloColor modelo) {
        super(titulo);
        this.imagenActual = imagen;
        this.modeloActual = modelo;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLayout(new BorderLayout());

        PanelDeImagen panelImagen = new PanelDeImagen(imagenActual);
        add(new JScrollPane(panelImagen), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        add(panelBotones, BorderLayout.SOUTH);

        // Añadir botones según el modelo de color actual
        switch (modeloActual) {
            case RGB:
                // No se inicia desde aquí, sino desde la ventana principal
                break;
            case CMY:
                panelBotones.add(crearBoton("a RGB", ModeloColor.RGB, ProcesadorDeColor::cmyARgb));
                panelBotones.add(crearBoton("a CMYK", ModeloColor.CMYK, ProcesadorDeColor::cmyACmyk));
                break;
            case CMYK:
                panelBotones.add(crearBoton("a CMY", ModeloColor.CMY, ProcesadorDeColor::cmykACmy));
                break;
            case YIQ:
                panelBotones.add(crearBoton("a RGB (Aprox.)", ModeloColor.RGB, ProcesadorDeColor::yiqARgb));
                break;
            case HSI:
                panelBotones.add(crearBoton("a RGB (Aprox.)", ModeloColor.RGB, ProcesadorDeColor::hsiARgb));
                break;
            case HSV:
                panelBotones.add(crearBoton("a RGB", ModeloColor.RGB, ProcesadorDeColor::hsvARgb));
                break;
        }

        pack(); // Ajusta el tamaño de la ventana al contenido
        setLocationByPlatform(true);
        setVisible(true);
    }

    // Interfaz funcional para representar una transformación
    @FunctionalInterface
    interface Transformacion {
        BufferedImage aplicar(BufferedImage img);
    }

    private JButton crearBoton(String texto, ModeloColor nuevoModelo, Transformacion t) {
        JButton boton = new JButton(modeloActual.name() + " " + texto);
        boton.addActionListener(e -> {
            BufferedImage resultado = t.aplicar(imagenActual);
            new VentanaResultado(resultado, "Resultado: " + boton.getText(), nuevoModelo);
        });
        return boton;
    }
}
