package vista;

import control.ControlImagen;
import modelo.ModeloColor;
import modelo.ProcesadorDeColor;
import modelo.ResultadoEcualizacion;
import modelo.TipoEcualizacion;
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
    private JPanel panelTransformaciones;

    public VentanaPrincipal() {
        super("Editor de Imágenes Básico - Completo");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelDeImagen = new PanelDeImagen(null);
        panelDeImagen.setBackground(Color.DARK_GRAY);
        add(new JScrollPane(panelDeImagen), BorderLayout.CENTER);

        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panelControles, BorderLayout.WEST);

        // --- SECCIÓN 1: Carga ---
        JButton botonCargar = new JButton("Cargar Imagen...");
        panelControles.add(botonCargar);
        panelControles.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- SECCIÓN 2: Vistas y Análisis ---
        panelControles.add(new JLabel("Vistas y Análisis:"));
        JButton botonOriginal = new JButton("Original");
        JButton botonRGB = new JButton("Canales RGB");
        JButton botonGris = new JButton("Escala de Grises");
        JButton botonHistograma = new JButton("Histograma (Frecuencia)");
        JButton botonHistProb = new JButton("Histograma p(hi)");
        JButton botonCdf = new JButton("Densidad D(p(hi))");
        JButton botonEstadisticas = new JButton("Propiedades Estadísticas");
        panelControles.add(botonOriginal);
        panelControles.add(botonRGB);
        panelControles.add(botonGris);
        panelControles.add(botonHistograma);
        panelControles.add(botonHistProb);
        panelControles.add(botonCdf);
        panelControles.add(botonEstadisticas);
        panelControles.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- SECCIÓN 3: Mejora de Histograma ---
        panelControles.add(new JLabel("Mejora de Histograma:"));
        JButton botonEcualizarUniforme = new JButton("Ecualizar (Uniforme)");
        JButton botonEcualizarExp = new JButton("Ecualizar (Exponencial)");
        JButton botonEcualizarRay = new JButton("Ecualizar (Rayleigh)");
        JButton botonEcualizarHiperRaiz = new JButton("Ecualizar (Hip. Raíz)");
        JButton botonEcualizarHiperLog = new JButton("Ecualizar (Hip. Log)");
        panelControles.add(botonEcualizarUniforme);
        panelControles.add(botonEcualizarExp);
        panelControles.add(botonEcualizarRay);
        panelControles.add(botonEcualizarHiperRaiz);
        panelControles.add(botonEcualizarHiperLog);
        panelControles.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- SECCIÓN 4: Ajustes Interactivos ---
        panelControles.add(new JLabel("Ajustes Interactivos:"));
        panelDePestanas = new JTabbedPane();
        panelDePestanas.addTab("Brillo (Color)", crearPanelSlider(-100, 100, 0, "Brillo", 1.0, false, false));
        panelDePestanas.addTab("Brillo (Gris)", crearPanelSlider(-100, 100, 0, "Brillo", 1.0, false, true));
        panelDePestanas.addTab("Contraste (Color)", crearPanelSlider(0, 300, 100, "Contraste", 100.0, true, true));
        panelDePestanas.addTab("Contraste (Gris)", crearPanelSlider(0, 300, 100, "Contraste", 100.0, true, false));
        panelControles.add(panelDePestanas);
        panelControles.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- SECCIÓN 5: Transformaciones de Color ---
        panelControles.add(new JLabel("Transformaciones de Color:"));
        panelTransformaciones = new JPanel(new GridLayout(0, 1, 0, 5));
        panelControles.add(panelTransformaciones);
        panelTransformaciones.add(crearBotonTransformacion("RGB a CMY", ModeloColor.CMY, ProcesadorDeColor::rgbACmy));
        panelTransformaciones.add(crearBotonTransformacion("RGB a YIQ", ModeloColor.YIQ, ProcesadorDeColor::rgbAYiq));
        panelTransformaciones.add(crearBotonTransformacion("RGB a HSV", ModeloColor.HSV, ProcesadorDeColor::rgbAHsv));
        panelTransformaciones.add(crearBotonTransformacion("RGB a HSI", ModeloColor.HSI, ProcesadorDeColor::rgbAHsi));

        panelControles.add(Box.createVerticalGlue());
        labelInfo = new JLabel("Cargue una imagen para comenzar.");
        add(labelInfo, BorderLayout.SOUTH);

        // --- Lógica de los botones ---
        botonCargar.addActionListener(e -> cargarImagen());
        botonOriginal.addActionListener(e -> mostrarImagenOriginal());
        botonRGB.addActionListener(e -> mostrarCanalesRGB());
        botonGris.addActionListener(e -> mostrarEscalaDeGrises());
        botonHistograma.addActionListener(e -> mostrarHistograma());
        botonHistProb.addActionListener(e -> mostrarHistogramaProbabilidad());
        botonCdf.addActionListener(e -> mostrarCDF());
        botonEstadisticas.addActionListener(e -> mostrarEstadisticas());

        // Se conectan los nuevos botones al método flexible ecualizar()
        botonEcualizarUniforme.addActionListener(e -> ecualizar(TipoEcualizacion.UNIFORME));
        botonEcualizarExp.addActionListener(e -> ecualizar(TipoEcualizacion.EXPONENCIAL));
        botonEcualizarRay.addActionListener(e -> ecualizar(TipoEcualizacion.RAYLEIGH));
        botonEcualizarHiperRaiz.addActionListener(e -> ecualizar(TipoEcualizacion.HIPERBOLICA_RAICES));
        botonEcualizarHiperLog.addActionListener(e -> ecualizar(TipoEcualizacion.HIPERBOLICA_LOGARITMICA));

        componentesControl = new Component[]{botonOriginal, botonRGB, botonGris, botonHistograma, botonHistProb, botonCdf, botonEstadisticas, botonEcualizarUniforme, botonEcualizarExp, botonEcualizarRay, botonEcualizarHiperRaiz, botonEcualizarHiperLog, panelDePestanas, panelTransformaciones};
        setControlesHabilitados(false);

        setMinimumSize(new Dimension(900, 700));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @FunctionalInterface
    interface TransformacionInicial {
        Object aplicar(BufferedImage img);
    }

    private JButton crearBotonTransformacion(String texto, ModeloColor modeloDestino, TransformacionInicial t) {
        JButton boton = new JButton(texto);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, boton.getMinimumSize().height));
        boton.addActionListener(e -> {
            if (controlImagen != null && controlImagen.isImageLoaded()) {
                Image originalImg = controlImagen.getImagenOriginal();
                if (originalImg instanceof BufferedImage) {
                    BufferedImage original = (BufferedImage) originalImg;
                    Object resultado = t.aplicar(original);
                    new VentanaResultado(resultado, "Resultado: " + texto, modeloDestino);
                }
            }
        });
        return boton;
    }

    private JPanel crearPanelSlider(int min, int max, int inicio, String nombre, double divisor, boolean esContraste, boolean esGrisOColor) {
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

    // ÚNICO MÉTODO ECUALIZAR, AHORA FLEXIBLE
    private void ecualizar(TipoEcualizacion tipo) {
        if (controlImagen != null && controlImagen.isImageLoaded()) {
            double[] params = new double[1];
            String input;

            switch (tipo) {
                case EXPONENCIAL:
                case RAYLEIGH:
                    input = JOptionPane.showInputDialog(this, "Introduce el valor para alpha (α):", (tipo == TipoEcualizacion.EXPONENCIAL ? "0.05" : "80.0"));
                    try {
                        params[0] = Double.parseDouble(input);
                    } catch (NumberFormatException | NullPointerException e) { return; }
                    break;
                case HIPERBOLICA_RAICES:
                    input = JOptionPane.showInputDialog(this, "Introduce el valor para la potencia (pot):", "2.0");
                    try {
                        params[0] = Double.parseDouble(input);
                    } catch (NumberFormatException | NullPointerException e) { return; }
                    break;
            }

            Image originalGris = controlImagen.getImagenGris();
            int[] histOriginal = controlImagen.getHistograma();
            ResultadoEcualizacion resultado = controlImagen.getResultadoEcualizacion(tipo, params);

            if (resultado != null) {
                new FrameEcualizacion(originalGris, histOriginal, resultado.imagenEcualizada, resultado.histogramaEcualizado);
            }
        }
    }

    private void mostrarImagenOriginal() { if (controlImagen != null) panelDeImagen.setImagen(controlImagen.getImagenOriginal()); }
    private void mostrarCanalesRGB() { if (controlImagen != null) panelDeImagen.setImagen(controlImagen.getImagenCanalesRGB()); }
    private void mostrarEscalaDeGrises() { if (controlImagen != null) panelDeImagen.setImagen(controlImagen.getImagenGris()); }
    private void mostrarHistograma() {
        if (controlImagen != null) {
            int[] datos = controlImagen.getHistograma();
            if (datos != null) new FrameHistograma(datos);
        }
    }
    private void mostrarHistogramaProbabilidad() { /* ... Tu método aquí ... */ }
    private void mostrarCDF() { /* ... Tu método aquí ... */ }
    private void mostrarEstadisticas() { /* ... Tu método aquí ... */ }

    private void setControlesHabilitados(boolean habilitado) {
        for (Component comp : componentesControl) {
            comp.setEnabled(habilitado);
            if(comp instanceof JPanel){
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