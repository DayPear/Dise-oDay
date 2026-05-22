/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pantallas;

import Controlador.interfaz.ICoordinadorAplicacion;
import dtos.ReservacionDTO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Aaron Burciaga - 262788
 * @author Brian Sandoval - 262741
 * @author Dayanara Peralta - 262695
 * @author María Valdez - 262775
 */
public class FrmDetallesCompra extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmDetallesCompra.class.getName());
    private ICoordinadorAplicacion coordinador;
    private ReservacionDTO reservacion;

    /**
     * Creates new form frmDetallesCompra
     */
    public FrmDetallesCompra(ICoordinadorAplicacion coordinador, ReservacionDTO reservacion) {
        this.coordinador = coordinador;
        this.reservacion = reservacion;
        initComponents();
        setLocationRelativeTo(null);
        cargarDatos();
    }

    private void limpiar() {
        iconEvento.setIcon(null);
        iconEvento.setText("");
        iconQR.setIcon(null);
        iconQR.setText("");
        txtEvento.setText("");
        txtAsientos.setText("");
        txtFechaHora.setText("");
        txtUbicacion.setText("");
        txtHorasAntes.setText("48");
    }

    public void setReservacion(ReservacionDTO reservacion) {
        limpiar();
        this.reservacion = reservacion;
        cargarDatos();
    }

    public void cargarDatos() {
        if (reservacion == null) {
            return;
        }
        // cargar evento
        if (reservacion.getBoleto().getEvento().getUrlImagen() != null && !reservacion.getBoleto().getEvento().getUrlImagen().isEmpty()) {

            String rutaLimpia = reservacion.getBoleto().getEvento().getUrlImagen().replace("/src/main/resources", "");
            String rutaAlternativa = reservacion.getBoleto().getEvento().getUrlImagen().replace("src/main/resources", "");

            java.net.URL imgUrl = getClass().getResource(rutaLimpia);
            if (imgUrl == null) {
                imgUrl = getClass().getResource(rutaAlternativa);
            }

            if (imgUrl != null) {
                ImageIcon icono = new ImageIcon(imgUrl);

                int ancho = 306;
                int alto = 202;

                Image imagenEscalada = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                iconEvento.setIcon(new ImageIcon(imagenEscalada));
            }
            iconEvento.setText("");
        }
        // cargar qr
        if (reservacion.getBoleto().getCodigoQR() != null && !reservacion.getBoleto().getCodigoQR().isEmpty()) {

            String rutaRelativa = reservacion.getBoleto().getCodigoQR();
            BufferedImage imgOriginal = null;

            // 1. Intentar cargar la imagen (con el método robusto anterior)
            try {
                String rutaLimpia = rutaRelativa.replace("/src/main/resources", "");
                java.net.URL imgUrl = getClass().getResource(rutaLimpia);

                if (imgUrl != null) {
                    imgOriginal = ImageIO.read(imgUrl);
                } else {
                    File archivoQR = new File(rutaRelativa.startsWith("/") ? rutaRelativa.substring(1) : rutaRelativa);
                    if (archivoQR.exists()) {
                        imgOriginal = ImageIO.read(archivoQR);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error leyendo la imagen: " + e.getMessage());
            }

            // 2. Si logramos obtener la imagen original, la escalamos con ALTA CALIDAD
            if (imgOriginal != null) {
                // Obtenemos el tamaño actual del JLabel para una adaptación perfecta
                // Si iconQR aún no es visible, usa tus valores fijos: int anchoTarget = 306; int altoTarget = 202;
                int anchoTarget = iconQR.getWidth();
                int altoTarget = iconQR.getHeight();

                // Corrección por si el layout aún no ha calculado el tamaño
                if (anchoTarget == 0) {
                    anchoTarget = 306;
                }
                if (altoTarget == 0) {
                    altoTarget = 202;
                }

                // Creamos una nueva imagen vacía con el tamaño del destino
                BufferedImage dimg = new BufferedImage(anchoTarget, altoTarget, BufferedImage.TYPE_INT_ARGB);

                // Obtenemos el objeto gráfico para "pintar" sobre la nueva imagen
                Graphics2D g2d = dimg.createGraphics();

                // ACTIVAMOS LA ALTA CALIDAD (Esto es clave)
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Pintamos la imagen original estirándola para ocupar todo el nuevo lienzo
                g2d.drawImage(imgOriginal, 0, 0, anchoTarget, altoTarget, null);
                g2d.dispose(); // Liberamos recursos gráficos

                // 3. Mostramos la imagen final
                iconQR.setIcon(new ImageIcon(dimg));
                iconQR.setText("");
            } else {
                iconQR.setText("QR no encontrado");
                iconQR.setIcon(null); // Limpiamos icono previo si hubo error
            }
        }

        this.txtEvento.setText(reservacion.getBoleto().getEvento().getNombreEvento());
        if (reservacion.getBoleto().getEvento().isGratuito()) {
            txtAsientos.setText("LIBRE");
        } else {
            txtAsientos.setText(reservacion.getBoleto().getAsiento().getAsiento().getFila() + reservacion.getBoleto().getAsiento().getAsiento().getNumero().toString());
        }
        DateTimeFormatter formateadorFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formateadorHora = DateTimeFormatter.ofPattern("HH:mm");
        this.txtFechaHora.setText(String.valueOf(reservacion.getBoleto().getEvento().getFechaHora().format(formateadorFecha)) + " - " + String.valueOf(reservacion.getBoleto().getEvento().getFechaHora().format(formateadorHora)));
        this.txtUbicacion.setText(reservacion.getBoleto().getEvento().getUbicacion().getNombre());
        this.txtHorasAntes.setText("48");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnInicio = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        iconEvento = new javax.swing.JLabel();
        lblDetalles = new javax.swing.JLabel();
        lblAsientos = new javax.swing.JLabel();
        lblEvento = new javax.swing.JLabel();
        lblFechaHora = new javax.swing.JLabel();
        lblUbicacion = new javax.swing.JLabel();
        txtAsientos = new javax.swing.JLabel();
        txtEvento = new javax.swing.JLabel();
        txtFechaHora = new javax.swing.JLabel();
        txtUbicacion = new javax.swing.JLabel();
        lblEntrada = new javax.swing.JLabel();
        iconQR = new javax.swing.JLabel();
        btnAceptar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtHorasAntes = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(31, 92, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PotroEventos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 49, 141));

        btnInicio.setBackground(new java.awt.Color(0, 49, 141));
        btnInicio.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        btnInicio.setForeground(new java.awt.Color(255, 255, 255));
        btnInicio.setText("Inicio");
        btnInicio.setBorder(null);
        btnInicio.setBorderPainted(false);
        btnInicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnInicioMouseClicked(evt);
            }
        });
        btnInicio.addActionListener(this::btnInicioActionPerformed);

        btnConsultar.setBackground(new java.awt.Color(0, 49, 141));
        btnConsultar.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        btnConsultar.setForeground(new java.awt.Color(255, 255, 255));
        btnConsultar.setText("Consultar");
        btnConsultar.setBorder(null);
        btnConsultar.setBorderPainted(false);
        btnConsultar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConsultarMouseClicked(evt);
            }
        });
        btnConsultar.addActionListener(this::btnConsultarActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(btnInicio)
                .addGap(18, 18, 18)
                .addComponent(btnConsultar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInicio)
                    .addComponent(btnConsultar))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        iconEvento.setText("iconEvento");

        lblDetalles.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        lblDetalles.setText("Detalles de Compra");

        lblAsientos.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        lblAsientos.setText("Asiento(s)");

        lblEvento.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        lblEvento.setText("Evento");

        lblFechaHora.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        lblFechaHora.setText("Fecha y Hora");

        lblUbicacion.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        lblUbicacion.setText("Ubicacion");

        txtAsientos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtAsientos.setText("Info asientos");

        txtEvento.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtEvento.setText("Nombre Evento");

        txtFechaHora.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFechaHora.setText("Info fecha y hora");

        txtUbicacion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtUbicacion.setText("Info ubicación");

        lblEntrada.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        lblEntrada.setText("Entrada QR");

        iconQR.setText("qr");

        btnAceptar.setBackground(new java.awt.Color(0, 119, 255));
        btnAceptar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAceptar.setForeground(new java.awt.Color(255, 255, 255));
        btnAceptar.setText("Aceptar");
        btnAceptar.setBorderPainted(false);
        btnAceptar.setFocusPainted(false);
        btnAceptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAceptarMouseClicked(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(217, 217, 217));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Tiempo límite para cancelar antes del evento:");

        txtHorasAntes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtHorasAntes.setText("X");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("horas antes del evento. ");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("<html><u>Consulte términos y condiciones.</u></font></html>");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtHorasAntes, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtHorasAntes)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(iconEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtUbicacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(459, 459, 459))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblAsientos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(lblDetalles)
                                .addGap(295, 295, 295))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(lblEntrada)
                                .addGap(109, 109, 109))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtAsientos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEvento)
                            .addComponent(lblFechaHora)
                            .addComponent(txtFechaHora, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(iconQR, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblUbicacion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAceptar)
                        .addGap(121, 121, 121))))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblDetalles)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(txtAsientos)
                                .addGap(18, 18, 18)
                                .addComponent(lblEvento)
                                .addGap(18, 18, 18)
                                .addComponent(txtEvento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblFechaHora))
                            .addComponent(iconEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtFechaHora)
                        .addGap(18, 18, 18)
                        .addComponent(lblUbicacion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUbicacion))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAsientos)
                            .addComponent(lblEntrada))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(iconQR, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(btnAceptar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAceptarMouseClicked
        // TODO add your handling code here:
        coordinador.mostrarInicio();
    }//GEN-LAST:event_btnAceptarMouseClicked

    private void btnInicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInicioMouseClicked
        // TODO add your handling code here:
        coordinador.mostrarInicio();
        this.dispose();
    }//GEN-LAST:event_btnInicioMouseClicked

    private void btnConsultarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConsultarMouseClicked
        // TODO add your handling code here:
        coordinador.mostrarConsultarMenu();
        this.dispose();
    }//GEN-LAST:event_btnConsultarMouseClicked

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        // TODO add your handling code here:
        coordinador.mostrarInicio();
    }//GEN-LAST:event_btnInicioActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        // TODO add your handling code here:
        coordinador.mostrarConsultarMenu();
    }//GEN-LAST:event_btnConsultarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnInicio;
    private javax.swing.JLabel iconEvento;
    private javax.swing.JLabel iconQR;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblAsientos;
    private javax.swing.JLabel lblDetalles;
    private javax.swing.JLabel lblEntrada;
    private javax.swing.JLabel lblEvento;
    private javax.swing.JLabel lblFechaHora;
    private javax.swing.JLabel lblUbicacion;
    private javax.swing.JLabel txtAsientos;
    private javax.swing.JLabel txtEvento;
    private javax.swing.JLabel txtFechaHora;
    private javax.swing.JLabel txtHorasAntes;
    private javax.swing.JLabel txtUbicacion;
    // End of variables declaration//GEN-END:variables
}
