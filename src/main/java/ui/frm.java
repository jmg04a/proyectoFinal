/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import clases.conexionBaseDatos;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.GestorPermisos;
import modelo.Permisos;
import modelo.Usuario;
import ui.dialogs.*;

/**
 *
 * @author jose
 */
public class frm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frm.class.getName());
private String permisosHex;   // Ej: "FF"
private DefaultTableModel modelo;
private conexionBaseDatos conexion;
    /**
     * Creates new form frm
     */
    
    public frm() throws SQLException {
    initComponents();
    this.permisosHex = permisosHex;
    conexion = new conexionBaseDatos();
    configurarPermisos(); 
    }
    public frm(String permisosHexRecibidos) throws SQLException {
        initComponents();
        this.permisosHex = permisosHexRecibidos; // Guardamos los permisos
        conexion = new conexionBaseDatos();
        
        // 1. Primero configuramos QUÉ tablas puede ver en la lista
        configurarMenuTablas();
        
        // 2. Evaluamos los botones para la primera tabla que aparezca por defecto
        if (cmbTablas.getItemCount() > 0) {
            cmbTablas.setSelectedIndex(0);
            evaluarAcciones();
        } else {
            // Si no tiene permisos de nada, bloqueamos todo
            bloquearTodo();
        }
        
    }
    
    private void evaluarAcciones() {
        String tablaSeleccionada = (String) cmbTablas.getSelectedItem();
        
        if (tablaSeleccionada == null) return;

        // Por defecto apagamos todo y encendemos según corresponda
        boolean puedeCrear = false;
        boolean puedeEditar = false;
        boolean puedeEliminar = false;
        boolean puedeBuscar = false; // jButton4

        switch (tablaSeleccionada) {
            case "Pacientes" -> {
                // VER: Todos los que vean la tabla pueden buscar
                puedeBuscar = true; 
                
                // EDITAR: Permiso explícito de editar paciente O gestionar citas
                if (GestorPermisos.tienePermiso(permisosHex, Permisos.PACIENTE_EDITAR) ||
                    GestorPermisos.tienePermiso(permisosHex, Permisos.CITA_GESTIONAR)) {
                    puedeCrear = true;
                    puedeEditar = true;
                    puedeEliminar = true; // Asumimos borrar también, si no, quita esta línea
                }
            }

            case "Citas" -> {
                puedeBuscar = true;
                // Solo quien tenga CITA_GESTIONAR puede mover datos
                if (GestorPermisos.tienePermiso(permisosHex, Permisos.CITA_GESTIONAR)) {
                    puedeCrear = true;
                    puedeEditar = true;
                    puedeEliminar = true;
                }
            }
            
            case "Doctores" -> {
                puedeBuscar = true;
                // Usualmente solo Admin gestiona doctores, o CITA_GESTIONAR
                if (GestorPermisos.tienePermiso(permisosHex, Permisos.USUARIO_GESTIONAR)) {
                    puedeCrear = true; puedeEditar = true; puedeEliminar = true;
                }
            }

            case "Historial Clinico" -> {
                puedeBuscar = true;
                // Requerimiento: Historial Editor solo CREAR y BUSCAR
                if (GestorPermisos.tienePermiso(permisosHex, Permisos.HISTORIAL_EDITAR)) {
                    puedeCrear = true;
                    puedeEditar = false; // "Historial clínico no se borra ni edita legalmente" (sugerencia)
                    puedeEliminar = false;
                }
            }

            case "Usuarios" -> {
                puedeBuscar = true;
                if (GestorPermisos.tienePermiso(permisosHex, Permisos.USUARIO_GESTIONAR)) {
                    puedeCrear = true;
                    puedeEditar = true;
                    puedeEliminar = true;
                }
            }

            case "Auditoria" -> {
                // Requerimiento: Solo buscar
                puedeBuscar = true;
                puedeCrear = false;
                puedeEditar = false;
                puedeEliminar = false;
            }
        }

        // Aplicamos los estados a los botones
        btnCrear.setEnabled(puedeCrear);
        btnActualizar.setEnabled(puedeEditar); // Asumo que btnActualizar es editar
        btnEliminar.setEnabled(puedeEliminar);
        jButton4.setEnabled(puedeBuscar);      // Botón de Buscar
        
        // Cargar los datos en la tabla visualmente
        cargarDatosTabla(tablaSeleccionada);
    }
    
    private void bloquearTodo() {
        btnCrear.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        jButton4.setEnabled(false);
    }
    
    // Método auxiliar para cargar el SQL (Movi tu lógica del switch aquí)
    private void cargarDatosTabla(String tabla) {
    String sql = "";
    
    // Usamos Text Blocks (""") para que el SQL sea legible en Java
    switch (tabla) {
        
        case "Usuarios" -> sql = """
            SELECT u.id_usuario, u.nombre, u.username, u.estado, r.nombre_rol AS Rol
            FROM aplicacion.usuario u
            JOIN aplicacion.usuario_rol ur ON u.id_usuario = ur.id_usuario
            JOIN aplicacion.rol r ON ur.id_rol = r.id_rol
            ORDER BY u.nombre ASC
        """;

        case "Pacientes" -> sql = """
            SELECT id_paciente, nombre, telefono, correo, fecha_nacimiento
            FROM aplicacion.paciente
            ORDER BY nombre ASC
        """;

        case "Citas" -> sql = """
            SELECT c.id_cita, 
                   TO_CHAR(c.fecha, 'DD/MM/YYYY HH24:MI') AS Fecha_Hora,
                   p.nombre AS Paciente, 
                   u_doc.nombre AS Doctor, -- Ojo: Nombre del usuario asociado al doctor
                   c.estado
            FROM aplicacion.cita c
            INNER JOIN aplicacion.paciente p ON c.id_paciente = p.id_paciente
            INNER JOIN aplicacion.doctor d ON c.id_doctor = d.id_doctor
            INNER JOIN aplicacion.usuario u_doc ON d.id_usuario = u_doc.id_usuario
            ORDER BY c.fecha DESC
        """;

        case "Doctores" -> sql = """
            SELECT d.id_doctor, 
                   u.nombre AS Nombre_Doctor, 
                   d.especialidad, 
                   d.cedula_profesional, 
                   d.horario
            FROM aplicacion.doctor d
            INNER JOIN aplicacion.usuario u ON d.id_usuario = u.id_usuario
            ORDER BY u.nombre ASC
        """;

        case "Historial Clinico" -> sql = """
            SELECT h.id_historial, 
                   p.nombre AS Paciente, 
                   u_doc.nombre AS Doctor, 
                   TO_CHAR(h.fecha_consulta, 'DD/MM/YYYY') AS Fecha,
                   h.diagnostico
            FROM aplicacion.historial_clinico h
            INNER JOIN aplicacion.paciente p ON h.id_paciente = p.id_paciente
            INNER JOIN aplicacion.doctor d ON h.id_doctor = d.id_doctor
            INNER JOIN aplicacion.usuario u_doc ON d.id_usuario = u_doc.id_usuario
            ORDER BY h.fecha_consulta DESC
        """;

        case "Auditoria" -> sql = """
            SELECT a.id_auditoria,
                   u.username AS Usuario_Responsable,
                   a.accion, 
                   a.tabla_afectada, 
                   TO_CHAR(a.fecha_hora, 'DD/MM/YYYY HH24:MI:SS') AS Fecha_Hora
            FROM aplicacion.auditoria a
            INNER JOIN aplicacion.usuario u ON a.id_usuario = u.id_usuario
            ORDER BY a.fecha_hora DESC
        """;
    }

    if (!sql.isEmpty()) {
        modelo = conexion.getTableModel(sql);
        jTable1.setModel(modelo);
    }
}
    
    private void configurarMenuTablas() {
        cmbTablas.removeAllItems(); // Limpiamos la lista por defecto

        // --- PACIENTES ---
        // Se muestra si tiene permiso de VER paciente, EDITAR paciente, o VER/GESTIONAR citas
        if (GestorPermisos.tienePermiso(permisosHex, Permisos.PACIENTE_VER) || 
            GestorPermisos.tienePermiso(permisosHex, Permisos.PACIENTE_EDITAR) ||
            GestorPermisos.tienePermiso(permisosHex, Permisos.CITA_VER) ||
            GestorPermisos.tienePermiso(permisosHex, Permisos.CITA_GESTIONAR)) {
            cmbTablas.addItem("Pacientes");
        }

        // --- CITAS ---
        if (GestorPermisos.tienePermiso(permisosHex, Permisos.CITA_VER) || 
            GestorPermisos.tienePermiso(permisosHex, Permisos.CITA_GESTIONAR)) {
            cmbTablas.addItem("Citas");
        }
        
        // --- DOCTORES (Requerimiento de CITA_VER) ---
        if (GestorPermisos.tienePermiso(permisosHex, Permisos.CITA_VER) ||
            GestorPermisos.tienePermiso(permisosHex, Permisos.CITA_GESTIONAR)) {
            cmbTablas.addItem("Doctores");
        }

        // --- HISTORIAL MÉDICO ---
        if (GestorPermisos.tienePermiso(permisosHex, Permisos.HISTORIAL_VER) || 
            GestorPermisos.tienePermiso(permisosHex, Permisos.HISTORIAL_EDITAR)) {
            cmbTablas.addItem("Historial Clinico");
        }

        // --- USUARIOS ---
        if (GestorPermisos.tienePermiso(permisosHex, Permisos.USUARIO_GESTIONAR)) {
            cmbTablas.addItem("Usuarios");
        }

        // --- AUDITORÍA ---
        if (GestorPermisos.tienePermiso(permisosHex, Permisos.AUDITORIA_VER)) {
            cmbTablas.addItem("Auditoria");
        }
    }
    
    private void configurarPermisos() {
        // Usamos tu GestorPermisos. Asegúrate que importaste: import modelo.GestorPermisos; import modelo.Permisos;
        
        // Ejemplo: Solo habilitar botón CREAR si tiene permiso de GESTIONAR USUARIOS
        boolean puedeGestionar = GestorPermisos.tienePermiso(permisosHex, Permisos.USUARIO_GESTIONAR);
        
        btnCrear.setEnabled(puedeGestionar);
        btnEliminar.setEnabled(puedeGestionar);
        btnActualizar.setEnabled(puedeGestionar);
        
        // Opcional: Deshabilitar opciones del ComboBox si no tiene permisos
        // (Esto es visual, para seguridad real debes filtrar en el switch del evento)
        if (!GestorPermisos.tienePermiso(permisosHex, Permisos.AUDITORIA_VER)) {
             cmbTablas.removeItem("Auditoría");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        cmbTablas = new javax.swing.JComboBox<>();
        btnCrear = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButRefrescar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        cmbTablas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Usuarios", "Pacientes", "Citas", "Auditoria" }));
        cmbTablas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTablasActionPerformed(evt);
            }
        });

        btnCrear.setText("Crear");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButRefrescar.setText("Refrescar tabla");
        jButRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButRefrescarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButRefrescar))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton4))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnCrear))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnActualizar))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnEliminar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(cmbTablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(cmbTablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jButRefrescar)
                        .addGap(84, 84, 84)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCrear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnActualizar)
                        .addGap(27, 27, 27)
                        .addComponent(btnEliminar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbTablasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTablasActionPerformed
        // TODO add your handling code here:
        
        evaluarAcciones();
        
    }//GEN-LAST:event_cmbTablasActionPerformed

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        // TODO add your handling code here:
        
            String tablaActual = (String) cmbTablas.getSelectedItem();
        if (tablaActual == null) return;

        // Usamos un try-catch por si falla al abrir alguna ventana
        try {
            switch (tablaActual) {
                case "Pacientes" -> {
                    new jDialogPacientes(this, true, 0).setVisible(true);
                }
                case "Citas" -> {
                    // Pasamos 0 para nueva cita
                    new jDialogCitas(this, true, 0).setVisible(true);
                }
                case "Doctores" -> {
                    new jDialogDoctores(this, true, 0).setVisible(true);
                }
                case "Historial Clinico" -> {
                    new jDialogHistorial(this, true, 0).setVisible(true);
                }
                case "Usuarios" -> {
                    new jDialogUsuarios(this, true, 0).setVisible(true);
                }
                case "Auditoria" -> {
                    JOptionPane.showMessageDialog(this, "No se pueden crear registros de auditoría manualmente.");
                    return; // Salimos para no refrescar innecesariamente
                }
            }

            // Al cerrar la ventana (dispose), refrescamos la tabla automáticamente
            cargarDatosTabla(tablaActual);

        } catch (Exception e) {
            System.out.println("Error al abrir ventana de creación: " + e.getMessage());
        }
    }//GEN-LAST:event_btnCrearActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        
            // 1. Validar que haya una fila seleccionada
    int filaSeleccionada = jTable1.getSelectedRow();
    if (filaSeleccionada == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Por favor, selecciona el registro que deseas eliminar.");
        return;
    }

    String tablaActual = (String) cmbTablas.getSelectedItem();
    if (tablaActual == null) return;

    // 2. Protección: No permitir borrar Auditoría
    if (tablaActual.equals("Auditoria")) {
        javax.swing.JOptionPane.showMessageDialog(this, "No es posible eliminar registros de auditoría por seguridad.");
        return;
    }

    // 3. Obtener el ID (Columna 0)
    // Lo guardamos como String para concatenarlo al SQL, pero es seguro porque viene de la tabla interna
    String idSeleccionado = jTable1.getValueAt(filaSeleccionada, 0).toString();

    // 4. Preguntar confirmación al usuario
    int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
            this, 
            "¿Estás seguro de eliminar el registro con ID " + idSeleccionado + " de " + tablaActual + "?\nEsta acción no se puede deshacer.",
            "Confirmar Eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
    );

    if (confirmacion != javax.swing.JOptionPane.YES_OPTION) {
        return; // Si dice NO o cierra la ventana, no hacemos nada
    }

    // 5. Construir el SQL de borrado según la tabla
    String sql = "";
    
    switch (tablaActual) {
        case "Pacientes" -> 
            sql = "DELETE FROM aplicacion.paciente WHERE id_paciente = " + idSeleccionado;

        case "Usuarios" -> 
            sql = "DELETE FROM aplicacion.usuario WHERE id_usuario = " + idSeleccionado;

        case "Citas" -> 
            sql = "DELETE FROM aplicacion.cita WHERE id_cita = " + idSeleccionado;

        case "Doctores" -> 
            sql = "DELETE FROM aplicacion.doctor WHERE id_doctor = " + idSeleccionado;

        case "Historial Clinico" -> 
            sql = "DELETE FROM aplicacion.historial_clinico WHERE id_historial = " + idSeleccionado;
    }

    // 6. Ejecutar el borrado
    // Usamos tu método 'ejecutarSQL' de la clase conexionBaseDatos
    if (!sql.isEmpty()) {
        boolean exito = conexion.ejecutarSQL(sql);
        
        if (exito) {
            javax.swing.JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");
            // 7. Refrescar la tabla para que desaparezca el registro borrado
            cargarDatosTabla(tablaActual);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error al eliminar.\nPosible causa: Este registro está siendo usado en otra tabla (tiene datos relacionados).",
                "Error de Base de Datos", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jButRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButRefrescarActionPerformed
        // TODO add your handling code here:
        
        String tablaActual = (String) cmbTablas.getSelectedItem();
    
    // 2. Volver a ejecutar la carga de datos
    if (tablaActual != null) {
        cargarDatosTabla(tablaActual);
        System.out.println("Tabla " + tablaActual + " actualizada correctamente.");
    }
        
    }//GEN-LAST:event_jButRefrescarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        
            // 1. Validar selección
    int filaSeleccionada = jTable1.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona un registro para editar.");
        return;
    }

    String tablaActual = (String) cmbTablas.getSelectedItem();
    if (tablaActual == null) return;

    try {
        // 2. Obtener el ID (Asumimos SIEMPRE que la columna 0 es el ID)
        // Convertimos a String primero y luego a Int para evitar errores de cast
        String valorId = jTable1.getValueAt(filaSeleccionada, 0).toString();
        int idSeleccionado = Integer.parseInt(valorId);

        // 3. Abrir el diálogo correspondiente pasando el ID
        switch (tablaActual) {
            case "Pacientes" -> {
                new jDialogPacientes(this, true, idSeleccionado).setVisible(true);
            }
            case "Citas" -> {
                new jDialogCitas(this, true, idSeleccionado).setVisible(true);
            }
            case "Doctores" -> {
                new jDialogDoctores(this, true, idSeleccionado).setVisible(true);
            }
            case "Historial Clinico" -> {
                // Nota: A veces el historial no se permite editar por ley, pero si tienes permiso:
                new jDialogHistorial(this, true, idSeleccionado).setVisible(true);
            }
            case "Usuarios" -> {
                new jDialogUsuarios(this, true, idSeleccionado).setVisible(true);
            }
            case "Auditoria" -> {
                JOptionPane.showMessageDialog(this, "Los registros de auditoría son inmutables.");
                return;
            }
        }

        // 4. Refrescar la tabla al volver
        cargarDatosTabla(tablaActual);

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Error al leer el ID del registro.");
    } catch (Exception e) {
        System.out.println("Error al abrir ventana de edición: " + e.getMessage());
    }

        
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
            String tablaActual = (String) cmbTablas.getSelectedItem();
    if (tablaActual == null) return;

    // Pedir texto al usuario
    String texto = JOptionPane.showInputDialog(this, "Buscar en " + tablaActual + ":");

    // Si cancela o deja vacío, recargamos la tabla original y salimos
    if (texto == null || texto.trim().isEmpty()) {
        cargarDatosTabla(tablaActual);
        return;
    }

    // Limpiamos el texto y lo ponemos en minúsculas para búsqueda flexible
    String busqueda = texto.trim().toLowerCase();
    String sql = "";

    // Construimos el SQL con JOINS (igual que en cargarDatosTabla) pero agregando WHERE
    switch (tablaActual) {
        case "Pacientes" -> sql = """
            SELECT id_paciente, nombre, telefono, correo, fecha_nacimiento
            FROM aplicacion.paciente
            WHERE LOWER(nombre) LIKE '%""" + busqueda + "%' OR telefono LIKE '%" + busqueda + "%'";

        case "Usuarios" -> sql = """
             SELECT u.id_usuario, u.nombre, u.username, u.estado, r.nombre_rol AS Rol
             FROM aplicacion.usuario u
             JOIN aplicacion.usuario_rol ur ON u.id_usuario = ur.id_usuario
             JOIN aplicacion.rol r ON ur.id_rol = r.id_rol
             WHERE LOWER(u.nombre) LIKE '%""" + busqueda + "%' OR LOWER(u.username) LIKE '%" + busqueda + "%'";

        case "Citas" -> sql = """
            SELECT c.id_cita, 
                   TO_CHAR(c.fecha, 'DD/MM/YYYY HH24:MI') AS Fecha_Hora,
                   p.nombre AS Paciente, 
                   u_doc.nombre AS Doctor,
                   c.estado
            FROM aplicacion.cita c
            INNER JOIN aplicacion.paciente p ON c.id_paciente = p.id_paciente
            INNER JOIN aplicacion.doctor d ON c.id_doctor = d.id_doctor
            INNER JOIN aplicacion.usuario u_doc ON d.id_usuario = u_doc.id_usuario
            WHERE LOWER(p.nombre) LIKE '%""" + busqueda + "%' OR LOWER(u_doc.nombre) LIKE '%" + busqueda + "%'";

        case "Doctores" -> sql = """
            SELECT d.id_doctor, 
                   u.nombre AS Nombre_Doctor, 
                   d.especialidad, 
                   d.cedula_profesional
            FROM aplicacion.doctor d
            INNER JOIN aplicacion.usuario u ON d.id_usuario = u.id_usuario
            WHERE LOWER(u.nombre) LIKE '%""" + busqueda + "%' OR LOWER(d.especialidad) LIKE '%" + busqueda + "%'";

        case "Historial Clinico" -> sql = """
            SELECT h.id_historial, 
                   p.nombre AS Paciente, 
                   u_doc.nombre AS Doctor, 
                   TO_CHAR(h.fecha_consulta, 'DD/MM/YYYY') AS Fecha,
                   h.diagnostico
            FROM aplicacion.historial_clinico h
            INNER JOIN aplicacion.paciente p ON h.id_paciente = p.id_paciente
            INNER JOIN aplicacion.doctor d ON h.id_doctor = d.id_doctor
            INNER JOIN aplicacion.usuario u_doc ON d.id_usuario = u_doc.id_usuario
            WHERE LOWER(p.nombre) LIKE '%""" + busqueda + "%' OR LOWER(h.diagnostico) LIKE '%" + busqueda + "%'";

        case "Auditoria" -> sql = """
            SELECT a.id_auditoria,
                   u.username AS Usuario_Responsable,
                   a.accion, 
                   a.tabla_afectada, 
                   TO_CHAR(a.fecha_hora, 'DD/MM/YYYY HH24:MI:SS') AS Fecha_Hora
            FROM aplicacion.auditoria a
            INNER JOIN aplicacion.usuario u ON a.id_usuario = u.id_usuario
            WHERE LOWER(u.username) LIKE '%""" + busqueda + "%' OR LOWER(a.accion) LIKE '%" + busqueda + "%'";
    }

    // Ejecutamos la búsqueda
    if (!sql.isEmpty()) {
        modelo = conexion.getTableModel(sql);
        jTable1.setModel(modelo);
    }

        
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new frm().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(frm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JComboBox<String> cmbTablas;
    private javax.swing.JButton jButRefrescar;
    private javax.swing.JButton jButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
