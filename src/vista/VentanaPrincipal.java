package vista;

import control.ControlImagen;
import modelo.ModeloColor;
import modelo.ProcesadorDeColor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;

public class VentanaPrincipal extends JFrame {

    private ControlImagen controlImagen;
    private PanelDeImagen panelDeImagen;
    private JTabbedPane panelDePestanas;
    private JLabel labelInfo;
    private final Component[] componentesControl;
    private JPanel panelTransformaciones; // Panel para los nuevos botones

    public VentanaPrincipal() {
        super("Editor de Imágenes Básico");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelDeImagen = new PanelDeImagen(null);
        panelDeImagen.setBackground(Color.DARK_GRAY);
        add(new JScrollPane(panelDeImagen), BorderLayout.CENTER);

        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panelControles, BorderLayout.WEST);

        JButton botonCargar = new JButton("Cargar Imagen...");
        panelControles.add(botonCargar);
        panelControles.add(Box.createRigidArea(new Dimension(0, 20)));

        panelControles.add(new JLabel("Vistas Básicas:"));
        JButton botonOriginal = new JButton("Original");
        JButton botonRGB = new JButton("Canales RGB");
        JButton botonGris = new JButton("Escala de Grises");
        panelControles.add(botonOriginal);
        panelControles.add(botonRGB);
        panelControles.add(botonGris);
        panelControles.add(Box.createRigidArea(new Dimension(0, 20)));

        panelControles.add(new JLabel("Ajustes Interactivos:"));
        panelDePestanas = new JTabbedPane();
        panelDePestanas.addTab("Brillo (Color)", crearPanelSlider(-100, 100, 0, "Brillo", 1.0, false, false));
        panelDePestanas.addTab("Brillo (Gris)", crearPanelSlider(-100, 100, 0, "Brillo", 1.0, false, true));
        panelDePestanas.addTab("Contraste (Color)", crearPanelSlider(0, 300, 100, "Contraste", 100.0, true, true));
        panelDePestanas.addTab("Contraste (Gris)", crearPanelSlider(0, 300, 100, "Contraste", 100.0, true, false));
        panelControles.add(panelDePestanas);
        panelControles.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- SECCIÓN NUEVA: Transformaciones de Color ---
        panelControles.add(new JLabel("Transformaciones de Color:"));
        panelTransformaciones = new JPanel(new GridLayout(0, 1, 0, 5)); // 1 columna, 5px de espacio
        panelControles.add(panelTransformaciones);
        // Añadir botones de transformación
        panelTransformaciones.add(crearBotonTransformacion("RGB a CMY", ModeloColor.CMY, ProcesadorDeColor::rgbACmy));
        panelTransformaciones.add(crearBotonTransformacion("RGB a YIQ", ModeloColor.YIQ, ProcesadorDeColor::rgbAYiq));
        panelTransformaciones.add(crearBotonTransformacion("RGB a HSV", ModeloColor.HSV, ProcesadorDeColor::rgbAHsv));
        panelTransformaciones.add(crearBotonTransformacion("RGB a HSI", ModeloColor.HSI, ProcesadorDeColor::rgbAHsi));

        labelInfo = new JLabel("Cargue una imagen para comenzar.");
        add(labelInfo, BorderLayout.SOUTH);

        botonCargar.addActionListener(e -> cargarImagen());
        botonOriginal.addActionListener(e -> mostrarImagenOriginal());
        botonRGB.addActionListener(e -> mostrarCanalesRGB());
        botonGris.addActionListener(e -> mostrarEscalaDeGrises());

        componentesControl = new Component[]{botonOriginal, botonRGB, botonGris, panelDePestanas, panelTransformaciones};
        setControlesHabilitados(false);

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Método para crear los botones de la ventana principal
    private JButton crearBotonTransformacion(String texto, ModeloColor modeloDestino, VentanaResultado.Transformacion t) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> {
            if (controlImagen != null && controlImagen.isImageLoaded()) {
                BufferedImage original = (BufferedImage) controlImagen.getImagenOriginal();
                BufferedImage resultado = t.aplicar(original);
                new VentanaResultado(resultado, "Resultado: " + texto, modeloDestino);
            }
        });
        return boton;
    }

    private JPanel crearPanelSlider(int min, int max, int inicio, String nombre, double divisor, boolean esContraste, boolean esGrisOColor) {
        //... este método no cambia
        JPanel panel = new JPanel(new BorderLayout());
        JSlider slider = new JSlider(min, max, inicio);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing((max - min) / 2);
        JLabel labelValor = new JLabel(nombre + ": " + inicio);
        slider.addChangeListener(e -> {
            if (controlImagen == null || !slider.isEnabled()) return;
            int valor = slider.getValue();
            Image imgModificada;
            if (esContraste) {
                double factor = valor / divisor;
                labelValor.setText(String.format("%s: %.2fx", nombre, factor));
                imgModificada = controlImagen.modificarContraste(factor, esGrisOColor);
            } else {
                labelValor.setText(nombre + ": " + valor);
                imgModificada = controlImagen.modificarBrillo(valor, esGrisOColor);
            }
            panelDeImagen.setImagen(imgModificada);
        });
        panel.add(labelValor, BorderLayout.NORTH);
        panel.add(slider, BorderLayout.CENTER);
        return panel;
    }

    private void cargarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione una imagen");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (jpg, png, jpeg)", "jpg", "png", "jpeg"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            controlImagen = new ControlImagen(archivoSeleccionado.getAbsolutePath());
            if (controlImagen.isImageLoaded()) {
                labelInfo.setText("Imagen cargada: " + archivoSeleccionado.getName());
                setControlesHabilitados(true);
                mostrarImagenOriginal();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                setControlesHabilitados(false);
            }
        }
    }

    private void mostrarImagenOriginal() {
        if (controlImagen != null) panelDeImagen.setImagen(controlImagen.getImagenOriginal());
    }
    private void mostrarCanalesRGB() {
        if (controlImagen != null) panelDeImagen.setImagen(controlImagen.getImagenCanalesRGB());
    }
    private void mostrarEscalaDeGrises() {
        if (controlImagen != null) panelDeImagen.setImagen(controlImagen.getImagenGris());
    }

    private void setControlesHabilitados(boolean habilitado) {
        for (Component comp : componentesControl) {
            comp.setEnabled(habilitado);
            if(comp instanceof JPanel){ // Habilitar/deshabilitar botones internos
                for(Component c : ((JPanel)comp).getComponents()){
                    c.setEnabled(habilitado);
                }
            }
        }
        if (panelDePestanas.getTabCount() > 0) {
            for (int i = 0; i < panelDePestanas.getTabCount(); i++) {
                Component tab = panelDePestanas.getComponentAt(i);
                if (tab instanceof JPanel) {
                    for (Component comp : ((JPanel) tab).getComponents()) {
                        comp.setEnabled(habilitado);
                    }
                }
            }
        }
    }
}
