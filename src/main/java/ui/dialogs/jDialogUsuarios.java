/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.dialogs;

import clases.conexionBaseDatos;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import seguridad.PasswordUtil;

/**
 *
 * @author jose
 */
public class jDialogUsuarios extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(jDialogUsuarios.class.getName());
    private int idUsuarioActual = 0;
    private conexionBaseDatos conexion;
    
    /**
     * Creates new form jDialogPacientes
     */
    public jDialogUsuarios(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public jDialogUsuarios(java.awt.Frame parent, boolean modal, int id) {
        super(parent, modal);
        initComponents();
        this.idUsuarioActual = id;

        try {
            conexion = new conexionBaseDatos();
            llenarComboRoles(); // Cargar lista de roles disponibles
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        }

        // Configurar Combo de Estado manualmente si no lo hiciste en diseño
        if (cmbEstado.getItemCount() == 0) {
            cmbEstado.addItem("ACTIVO");
            cmbEstado.addItem("INACTIVO");
        }

        if (idUsuarioActual > 0) {
            lblTitulo.setText("EDITAR USUARIO");
            btnGuardar.setText("Actualizar");
            // El username suele ser único, a veces se bloquea su edición
            // txtUsername.setEditable(false); 
            cargarDatosParaEditar(idUsuarioActual);
        } else {
            lblTitulo.setText("NUEVO USUARIO");
            btnGuardar.setText("Guardar");
        }
    }
    
    private class ItemCombo {
        int id;
        String texto;
        public ItemCombo(int id, String texto) { this.id = id; this.texto = texto; }
        @Override
        public String toString() { return texto; }
    }
    
    private void llenarComboRoles() {
        String sql = "SELECT id_rol, nombre_rol FROM Rol ORDER BY nombre_rol";
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        // --- IMPORTANTE: Limpiar el combo antes de llenarlo ---
        // Esto borra el "1" o "Item 1" que pone NetBeans por defecto
        cmbRol.removeAllItems(); 
        // -----------------------------------------------------
        
        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                model.addElement(new ItemCombo(rs.getInt("id_rol"), rs.getString("nombre_rol")));
            }
            cmbRol.setModel(model);
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando roles: " + e.getMessage());
        }
    }
    
    private void cargarDatosParaEditar(int id) {
        // Necesitamos el Rol ID para seleccionarlo en el combo
        String sql = "SELECT u.nombre, u.username, u.estado, ur.id_rol " +
                     "FROM Usuario u " +
                     "LEFT JOIN Usuario_Rol ur ON u.id_usuario = ur.id_usuario " +
                     "WHERE u.id_usuario = " + id;

        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                txtNombre.setText(rs.getString("nombre"));
                txtUsername.setText(rs.getString("username"));
                
                // Estado
                cmbEstado.setSelectedItem(rs.getString("estado"));

                // Seleccionar Rol en el Combo
                int idRolBD = rs.getInt("id_rol");
                for (int i = 0; i < cmbRol.getItemCount(); i++) {
                    Object objeto = cmbRol.getItemAt(i);
                   // Preguntamos: "¿Este objeto es realmente de tipo ItemCombo?"
                    if (objeto instanceof ItemCombo) {
                        // 3. Si es verdad, ahora sí es seguro convertirlo (Casting)
                        ItemCombo item = (ItemCombo) objeto;

                        if (item.id == idRolBD) {
                            cmbRol.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuario: " + e.getMessage());
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButCancelar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        cmbRol = new javax.swing.JComboBox<>();
        txtNombre = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Nombre");

        jLabel2.setText("Estado");

        jLabel3.setText("Username");

        jLabel4.setText("Contraseña");

        btnGuardar.setText("Insertar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel5.setText("Rol");

        jButCancelar.setText("Cancelar");
        jButCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButCancelarActionPerformed(evt);
            }
        });

        lblTitulo.setText("Titulo ");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));

        txtPassword.setText("jPasswordField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(163, 163, 163)
                .addComponent(lblTitulo)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(118, 118, 118)
                                .addComponent(cmbRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 65, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtUsername, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbEstado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(53, 53, 53))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jButCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblTitulo)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(158, 158, 158)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:
        
        this.dispose();
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        
        
    // 1. Validaciones de Texto
    if (txtNombre.getText().trim().isEmpty() || txtUsername.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nombre y Usuario son obligatorios.");
        return;
    }

    // 2. Obtener Contraseña correctamente (Desde JPasswordField)
    char[] passArray = txtPassword.getPassword();
    String password = new String(passArray); // Convertimos char[] a String

    // Validar contraseña solo si es usuario nuevo
    if (idUsuarioActual == 0 && password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debes asignar una contraseña al nuevo usuario.");
        return;
    }

    // 3. Obtener Datos Generales
    String nombre = txtNombre.getText().trim();
    String username = txtUsername.getText().trim();
    
    // Validación segura del Estado
    if (cmbEstado.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, "Selecciona un estado.");
        return;
    }
    String estado = cmbEstado.getSelectedItem().toString();
    
    // 4. Obtener Rol (VALIDACIÓN SEGURA DE TIPOS)
    Object itemSeleccionado = cmbRol.getSelectedItem();
    
    if (itemSeleccionado == null) {
        JOptionPane.showMessageDialog(this, "Selecciona un Rol.");
        return;
    }

    int idRolSeleccionado = 0;
    
    // Verificamos si el objeto es realmente de tu clase ItemCombo
    if (itemSeleccionado instanceof ItemCombo) {
        idRolSeleccionado = ((ItemCombo) itemSeleccionado).id;
    } else {
        // Si entra aquí, es porque se seleccionó el "Item 1" basura de NetBeans
        JOptionPane.showMessageDialog(this, "Error: El rol seleccionado no es válido (Recarga la ventana).");
        return;
    }

    // 5. Lógica de Base de Datos (Transacción)
    Connection conn = null;

    try {
        conn = conexion.getConnection();
        conn.setAutoCommit(false); // INICIAR TRANSACCIÓN
        
        Statement stmt = conn.createStatement();

        // --- CASO 1: INSERTAR ---
        if (idUsuarioActual == 0) {
            String passHash = PasswordUtil.sha256(password);

            // A. Obtener ID manual
            ResultSet rsSeq = stmt.executeQuery("SELECT seq_usuario.NEXTVAL FROM DUAL");
            rsSeq.next();
            int nuevoId = rsSeq.getInt(1);
            rsSeq.close();

            // B. Insertar Usuario
            String sqlUser = "INSERT INTO Usuario (id_usuario, nombre, username, password_hash, estado) VALUES (" +
                    nuevoId + ", '" + nombre + "', '" + username + "', '" + passHash + "', '" + estado + "')";
            stmt.executeUpdate(sqlUser);

            // C. Insertar Relación Rol
            String sqlRol = "INSERT INTO Usuario_Rol (id_usuario_rol, id_usuario, id_rol) VALUES (" +
                    "seq_usuario_rol.NEXTVAL, " + nuevoId + ", " + idRolSeleccionado + ")";
            stmt.executeUpdate(sqlRol);
        } 
        
        // --- CASO 2: ACTUALIZAR ---
        else {
            // A. Actualizar Usuario
            String sqlUser = "UPDATE Usuario SET nombre='" + nombre + "', username='" + username + "', estado='" + estado + "'";
            
            // Solo cambiamos la contraseña si el campo NO está vacío
            if (!password.isEmpty()) {
                String nuevoHash = PasswordUtil.sha256(password);
                sqlUser += ", password_hash='" + nuevoHash + "'";
            }
            
            sqlUser += " WHERE id_usuario=" + idUsuarioActual;
            stmt.executeUpdate(sqlUser);

            // B. Actualizar Relación Rol
            // Primero intentamos actualizar
            String sqlRolUpdate = "UPDATE Usuario_Rol SET id_rol=" + idRolSeleccionado + " WHERE id_usuario=" + idUsuarioActual;
            int filasAfectadas = stmt.executeUpdate(sqlRolUpdate);
            
            // Si devuelve 0 es porque el usuario no tenía rol asignado (error de datos antiguos), así que hacemos insert
            if (filasAfectadas == 0) {
                 stmt.executeUpdate("INSERT INTO Usuario_Rol (id_usuario_rol, id_usuario, id_rol) VALUES (seq_usuario_rol.NEXTVAL, " + idUsuarioActual + ", " + idRolSeleccionado + ")");
            }
        }

        conn.commit(); // CONFIRMAR CAMBIOS
        JOptionPane.showMessageDialog(this, "Usuario guardado exitosamente.");
        this.dispose();

    } catch (Exception e) {
        // SI FALLA, DESHACER TODO (ROLLBACK)
        try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        e.printStackTrace(); // Imprimir error en consola para depurar
        JOptionPane.showMessageDialog(this, "Error grave al guardar: " + e.getMessage());
    } finally {
        try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
    }

        
    }//GEN-LAST:event_btnGuardarActionPerformed

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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                jDialogUsuarios dialog = new jDialogUsuarios(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbRol;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
