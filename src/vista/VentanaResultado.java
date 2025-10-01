package vista;

import modelo.ModeloColor;
import modelo.PixelHSI;
import modelo.PixelYIQ;
import modelo.ProcesadorDeColor;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VentanaResultado extends JFrame {

    private final Object datosReales;
    private final ModeloColor modeloActual;

    public VentanaResultado(Object datos, String titulo, ModeloColor modelo) {
        super(titulo);
        this.datosReales = datos;
        this.modeloActual = modelo;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        BufferedImage imagenVisual = crearImagenVisual();
        PanelDeImagen panelImagen = new PanelDeImagen(imagenVisual);
        add(new JScrollPane(panelImagen), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        add(panelBotones, BorderLayout.SOUTH);

        switch (modeloActual) {
            case CMY:
                panelBotones.add(crearBoton("a RGB", ModeloColor.RGB, (datos) -> ProcesadorDeColor.cmyARgb((BufferedImage) datos)));
                panelBotones.add(crearBoton("a CMYK", ModeloColor.CMYK, (datos) -> ProcesadorDeColor.cmyACmyk((BufferedImage) datos)));
                break;
            case CMYK:
                panelBotones.add(crearBoton("a CMY", ModeloColor.CMY, (datos) -> ProcesadorDeColor.cmykACmy((BufferedImage) datos)));
                break;
            case YIQ:
                panelBotones.add(crearBoton("a RGB", ModeloColor.RGB, (datos) -> ProcesadorDeColor.yiqARgb((PixelYIQ[][]) datos)));
                break;
            case HSI:
                panelBotones.add(crearBoton("a RGB", ModeloColor.RGB, (datos) -> ProcesadorDeColor.hsiARgb((PixelHSI[][]) datos)));
                break;
            case HSV:
                panelBotones.add(crearBoton("a RGB", ModeloColor.RGB, (datos) -> ProcesadorDeColor.hsvARgb((BufferedImage) datos)));
                break;
        }

        pack();
        setLocationByPlatform(true);
        setVisible(true);
    }

    private BufferedImage crearImagenVisual() {
        if (datosReales instanceof BufferedImage) {
            return (BufferedImage) datosReales;
        } else if (datosReales instanceof PixelYIQ[][]) {
            PixelYIQ[][] matrizYiq = (PixelYIQ[][]) datosReales;
            int h = matrizYiq.length;
            int w = matrizYiq[0].length;
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int Y = (int) Math.max(0, Math.min(255, matrizYiq[y][x].y * 255.0));
                    img.setRGB(x, y, new Color(Y, Y, Y).getRGB());
                }
            }
            return img;
        } else if (datosReales instanceof PixelHSI[][]) {
            PixelHSI[][] matrizHsi = (PixelHSI[][]) datosReales;
            int h = matrizHsi.length;
            int w = matrizHsi[0].length;
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int I = (int) Math.max(0, Math.min(255, matrizHsi[y][x].i * 255.0));
                    img.setRGB(x, y, new Color(I, I, I).getRGB());
                }
            }
            return img;
        }
        return null;
    }

    @FunctionalInterface
    interface Transformacion {
        Object aplicar(Object datos);
    }

    private JButton crearBoton(String texto, ModeloColor nuevoModelo, Transformacion t) {
        JButton boton = new JButton(modeloActual.name() + " " + texto);
        boton.addActionListener(e -> {
            Object resultado = t.aplicar(datosReales);
            new VentanaResultado(resultado, "Resultado: " + boton.getText(), nuevoModelo);
        });
        return boton;
    }
}
