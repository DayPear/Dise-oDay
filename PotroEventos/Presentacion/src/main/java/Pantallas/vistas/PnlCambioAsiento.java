/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Pantallas.vistas;

import Controlador.interfaz.ICoordinadorAplicacion;
import dtos.AsientoDTO;
import dtos.AsientoEventoDTO;
import dtos.EventoDTO;
import dtos.ReservacionDTO;
import dtos.SeccionDTO;
import java.awt.Image;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import utilerias.BotonUtileria;

/**
 * Panel para consultar la información de un evento específico y seleccionar
 * asientos gráficamente a través de PnlEstadio.
 *
 * @author Aaron Burciaga - 262788
 * @author Brian Sandoval - 262741
 * @author Dayanara Peralta - 262695
 * @author María Valdez - 262775
 */
public class PnlCambioAsiento extends javax.swing.JPanel {

    private ICoordinadorAplicacion coordinador;
    private EventoDTO evento;
    private PnlEstadio estadioVisual;
    private ReservacionDTO reservacion;
    private List<AsientoEventoDTO> asientosSeleccionados = new ArrayList<>();

    /**
     * Guarda el total actual de la compra.
     */
    private Long totalCompra = 0L;

    /**
     * Constructor del panel de consulta de evento.
     *
     * @param coordinador Interfaz para la comunicación y navegación.
     * @param reservacion la reservacion que se va a consultar.
     */
    public PnlCambioAsiento(ICoordinadorAplicacion coordinador, ReservacionDTO reservacion) {
        this.coordinador = coordinador;
        this.evento = reservacion.getBoleto().getEvento();
        this.reservacion = reservacion;

        initComponents();

        txtInfo.setContentType("text/html");
        txtInfo.setEditable(false);
        txtInfo.setOpaque(false);
        BotonUtileria.estilizarBoton(btnVolver);
        BotonUtileria.estilizarBoton(btnCambioAsiento);

        modoPantalla();
        cargarDatos();
        if (!evento.isGratuito()) {
            cargarEstadio();
            actualizarEtiquetasAsientos(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

    }

    public void modoPantalla() {
        if (evento.isGratuito()) {
            lblTuSeccion.setText("Tu Boleto");
            btnCambioAsiento.setText("Adquirir Boleto");
            PnlEstadio.setVisible(true);
//            jSeparator1.setVisible(false);
//            jSeparator2.setVisible(false);
//            jSeparator3.setVisible(false);
//            jSeparator4.setVisible(false);
        } else {
            lblTuSeccion.setText("Tu Sección");
            btnCambioAsiento.setText("CambiarAsiento");
            PnlEstadio.setVisible(true);
//            jSeparator1.setVisible(true);
//            jSeparator2.setVisible(true);
//            jSeparator3.setVisible(true);
//            jSeparator4.setVisible(true);
        }
    }

    private void cargarEstadio() {
        try {
            Map<SeccionDTO, List<AsientoEventoDTO>> mapa = coordinador.obtenerMapaOcupacion(evento);
            List<AsientoDTO> catalogo = coordinador.obtenerCatalogoAsientos();

            if (mapa == null || catalogo == null) {
                System.err.println("Datos del estadio nulos");
                return;
            }

            estadioVisual = new PnlEstadio(mapa, catalogo, (List<SeccionDTO> secciones, List<AsientoDTO> asientosInfo, List<AsientoEventoDTO> asientosEventos) -> {
                actualizarEtiquetasAsientos(secciones, asientosInfo, asientosEventos);
            }, coordinador);

            estadioVisual.setPreferredSize(new java.awt.Dimension(400, 400));

            PnlEstadio.removeAll();
            PnlEstadio.setLayout(new java.awt.BorderLayout());

            PnlEstadio.add(estadioVisual, java.awt.BorderLayout.CENTER);

            PnlEstadio.revalidate();
            PnlEstadio.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza las etiquetas de UI dependiendo de si hay 0, 1 o múltiples
     * asientos seleccionados y calcula el total general.
     *
     * @param secciones Lista de secciones correspondientes a los asientos.
     * @param asientosInfo Lista con la información técnica de los asientos.
     * @param asientosEventos Lista de asientos seleccionados del evento.
     */
    private void actualizarEtiquetasAsientos(
            List<SeccionDTO> secciones,
            List<AsientoDTO> asientosInfo,
            List<AsientoEventoDTO> asientosEventos
    ) {

        // Guardar los asientos seleccionados
        this.asientosSeleccionados = asientosEventos;

        if (evento == null || evento.isGratuito()) {
            totalCompra = 0L;
            return;
        }

        // Caso: ningún asiento seleccionado
        if (asientosEventos == null || asientosEventos.isEmpty()) {
            lblSeccion.setText("-");
            lblFila.setText("-");
            lblAsiento.setText("-");
            return;
        }

        // Caso: un solo asiento
        if (asientosEventos.size() == 1) {
            SeccionDTO seccion = secciones.get(0);
            AsientoDTO asiento = asientosInfo.get(0);

            lblSeccion.setText(seccion.getNombre());
            lblFila.setText(asiento.getFila());
            lblAsiento.setText(String.valueOf(asiento.getNumero()));
        } else {

            // Caso: múltiples asientos
            StringBuilder textoSecciones = new StringBuilder("<html>");
            StringBuilder textoFilas = new StringBuilder("<html>");
            StringBuilder textoAsientos = new StringBuilder("<html>");

            for (int i = 0; i < asientosEventos.size(); i++) {
                SeccionDTO seccion = secciones.get(i);
                AsientoDTO asiento = asientosInfo.get(i);

                if (seccion != null && asiento != null) {
                    textoSecciones.append(seccion.getNombre()).append("<br>");
                    textoFilas.append(asiento.getFila()).append("<br>");
                    textoAsientos.append(asiento.getNumero()).append("<br>");
                }
            }

            textoSecciones.append("</html>");
            textoFilas.append("</html>");
            textoAsientos.append("</html>");

            lblSeccion.setText(textoSecciones.toString());
            lblFila.setText(textoFilas.toString());
            lblAsiento.setText(textoAsientos.toString());
        }

    }

    public void cargarDatos() {
        if (evento == null) {
            return;
        }

        if (reservacion.getBoleto() != null && reservacion.getBoleto().getAsiento() != null) {
            AsientoEventoDTO asientoActual = reservacion.getBoleto().getAsiento();
            SeccionDTO seccionActual = reservacion.getBoleto().getAsiento().getAsiento().getSeccion();
            AsientoDTO asientoInfoActual = reservacion.getBoleto().getAsiento().getAsiento();

            lblSeccionActual.setText(
                    seccionActual != null ? seccionActual.toString() : "-"
            );

            lblFilaActual.setText(
                    asientoInfoActual != null ? asientoInfoActual.getFila() : "-"
            );

            lblAsientoActl.setText(
                    asientoInfoActual != null
                            ? String.valueOf(asientoActual.getAsiento().getNumero())
                            : "-"
            );
        }

        if (evento.getUrlImagen() != null && !evento.getUrlImagen().isEmpty()) {

            String rutaLimpia = evento.getUrlImagen().replace("/src/main/resources", "");
            String rutaAlternativa = evento.getUrlImagen().replace("src/main/resources", "");

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

        this.lblNombre.setText(evento.getNombreEvento());
        txtInfo.setContentType("text/html");
        txtInfo.setText("<html><div style='text-align: justify; font-family: sans-serif; font-size: 11px;'>"
                + evento.getInformacionEvento() + "</div></html>");
        txtInfo.setEditable(false);
        txtInfo.setOpaque(false);
        txtInfo.setBackground(new java.awt.Color(0, 0, 0, 0));
        jScrollPane2.setOpaque(false);
        jScrollPane2.getViewport().setOpaque(false);
        jScrollPane2.setBorder(null);
        DateTimeFormatter formateadorFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formateadorHora = DateTimeFormatter.ofPattern("HH:mm");
        this.lblFechaHora.setText(String.valueOf(evento.getFechaHora().format(formateadorFecha)) + " - " + String.valueOf(evento.getFechaHora().format(formateadorHora)));
        this.lblUbicacion.setText(evento.getUbicacion().getNombre());
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
        iconEvento = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblFechaHora = new javax.swing.JLabel();
        lblUbicacion = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblTuSeccion = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblSecc = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblSeccion = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        lblFila = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblAsiento = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        btnCambioAsiento = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        lblSecc1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblAsientoActl = new javax.swing.JLabel();
        lblFilaActual = new javax.swing.JLabel();
        lblSeccionActual = new javax.swing.JLabel();
        lblAsientoActual = new javax.swing.JLabel();
        PnlEstadio = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtInfo = new javax.swing.JTextPane();

        setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        iconEvento.setText("iconEvento");

        lblNombre.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNombre.setText("Título Evento");

        lblFechaHora.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblFechaHora.setText("Fecha y Hora");

        lblUbicacion.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblUbicacion.setText("Ubicación Evento");

        btnVolver.setBackground(new java.awt.Color(31, 92, 204));
        btnVolver.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnVolver.setForeground(new java.awt.Color(255, 255, 255));
        btnVolver.setText("Volver");
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setOpaque(true);
        btnVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVolverMouseClicked(evt);
            }
        });
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        lblTuSeccion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTuSeccion.setText("Tu Sección");

        lblSecc.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSecc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSecc.setText("Sección");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblSeccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSeccion.setText("seccion");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Fila");

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblFila.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFila.setText("fila");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Numero Asiento");

        lblAsiento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAsiento.setText("asientos");

        btnCambioAsiento.setBackground(new java.awt.Color(31, 92, 204));
        btnCambioAsiento.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCambioAsiento.setForeground(new java.awt.Color(255, 255, 255));
        btnCambioAsiento.setText("Cambio asiento");
        btnCambioAsiento.setBorderPainted(false);
        btnCambioAsiento.setFocusPainted(false);
        btnCambioAsiento.setOpaque(true);
        btnCambioAsiento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCambioAsientoMouseClicked(evt);
            }
        });

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblSecc1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSecc1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSecc1.setText("Sección");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Fila");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Numero Asiento");

        lblAsientoActl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAsientoActl.setText("asientos");

        lblFilaActual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFilaActual.setText("fila");

        lblSeccionActual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSeccionActual.setText("seccion");

        lblAsientoActual.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblAsientoActual.setText("Asiento actual");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTuSeccion)
                .addGap(104, 104, 104))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator4)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 7, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblSeccion, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblSecc, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblFila, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel12)
                                            .addComponent(lblAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnCambioAsiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator8)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblSeccionActual, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSecc1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblFilaActual, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel13)
                                    .addComponent(lblAsientoActl, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblAsientoActual)
                                .addGap(81, 81, 81)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTuSeccion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSecc)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblSeccion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 4, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblFila)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblAsiento)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAsientoActual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSecc1)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblSeccionActual)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblFilaActual)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jSeparator6)
                    .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblAsientoActl)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(btnCambioAsiento)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        PnlEstadio.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout PnlEstadioLayout = new javax.swing.GroupLayout(PnlEstadio);
        PnlEstadio.setLayout(PnlEstadioLayout);
        PnlEstadioLayout.setHorizontalGroup(
            PnlEstadioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 287, Short.MAX_VALUE)
        );
        PnlEstadioLayout.setVerticalGroup(
            PnlEstadioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 297, Short.MAX_VALUE)
        );

        txtInfo.setEditable(false);
        txtInfo.setBorder(null);
        txtInfo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtInfo.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(txtInfo);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(iconEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                                .addComponent(PnlEstadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(69, 69, 69))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblUbicacion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblFechaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(iconEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblNombre)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(lblFechaHora)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblUbicacion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PnlEstadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(12, 12, 12))))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVolverMouseClicked
        // TODO add your handling code here:
        coordinador.mostrarInicio();
    }//GEN-LAST:event_btnVolverMouseClicked

    private void btnCambioAsientoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCambioAsientoMouseClicked

        if (evento.isGratuito()) {
            JOptionPane.showMessageDialog(this, "Los eventos gratuitos no permiten cambio de asiento.");
            return;
        }

        // ================= EVENTO DE PAGO =================
        if (asientosSeleccionados == null || asientosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un asiento en el mapa.");
            return;
        }

        if (asientosSeleccionados.size() != 1) {
            JOptionPane.showMessageDialog(this, "Solo puede cambiar un asiento a la vez.");
            return;
        }

        AsientoEventoDTO asientoActual = reservacion.getBoleto().getAsiento();
        AsientoEventoDTO asientoNuevo = asientosSeleccionados.get(0);

        if (asientoActual != null && asientoActual.getIdAsientoEvento().equals(asientoNuevo.getIdAsientoEvento())) {
            JOptionPane.showMessageDialog(this, "El asiento seleccionado es el mismo que ya tienes.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "Confirmar Cambio de Asiento");

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            System.out.println("idReservacion: " + reservacion.getIdReservacion());
            System.out.println("idAsientoNuevo: " + reservacion.getBoleto().getAsiento().getIdAsientoEvento());
            System.out.println("asientoActual: " + (reservacion.getBoleto().getAsiento() != null
                    ? reservacion.getBoleto().getAsiento().getIdAsientoEvento() : "NULL"));
            boolean exito = coordinador.cambioAsiento(reservacion, asientoNuevo);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Asiento cambiado exitosamente.");
                coordinador.mostrarDetalles(reservacion);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo realizar el cambio de asiento.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cambiar el asiento: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCambioAsientoMouseClicked

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
        int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro de volver? Esto eliminará el proceso de cambio actual.");
        if (opcion == JOptionPane.OK_OPTION) {
            coordinador.mostrarInicio();
        }
    }//GEN-LAST:event_btnVolverActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnlEstadio;
    private javax.swing.JButton btnCambioAsiento;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel iconEvento;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel lblAsiento;
    private javax.swing.JLabel lblAsientoActl;
    private javax.swing.JLabel lblAsientoActual;
    private javax.swing.JLabel lblFechaHora;
    private javax.swing.JLabel lblFila;
    private javax.swing.JLabel lblFilaActual;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblSecc;
    private javax.swing.JLabel lblSecc1;
    private javax.swing.JLabel lblSeccion;
    private javax.swing.JLabel lblSeccionActual;
    private javax.swing.JLabel lblTuSeccion;
    private javax.swing.JLabel lblUbicacion;
    private javax.swing.JTextPane txtInfo;
    // End of variables declaration//GEN-END:variables
}
