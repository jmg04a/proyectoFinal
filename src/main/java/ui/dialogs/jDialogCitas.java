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
import java.text.SimpleDateFormat;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import seguridad.sesion;

/**
 *
 * @author jose
 */
public class jDialogCitas extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(jDialogCitas.class.getName());
    private int idCitaActual = 0;
    private conexionBaseDatos conexion;
    
    /**
     * Creates new form jDialogPacientes
     */
    public jDialogCitas(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public jDialogCitas(java.awt.Frame parent, boolean modal,int id) {
        super(parent, modal);
        initComponents();
        this.idCitaActual = id;

        try {
            conexion = new conexionBaseDatos();
            llenarCombos(); // Cargar Doctores y Pacientes
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        }

        if (idCitaActual > 0) {
            lblTitulo.setText("EDITAR CITA"); // Asegúrate de tener un JLabel lblTitulo
            btnGuardar.setText("Actualizar");
            cargarDatosParaEditar(idCitaActual);
        } else {
            lblTitulo.setText("NUEVA CITA");
            btnGuardar.setText("Agendar");
            // Seleccionar estado por defecto
            cmbEstado.setSelectedItem("PROGRAMADA");
        }
    }

    private class ItemCombo {
        int id;
        String texto;
        public ItemCombo(int id, String texto) { this.id = id; this.texto = texto; }
        @Override
        public String toString() { return texto; }
    }
    
    private void llenarCombos() {
        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Llenar Combo Doctores
            // Hacemos JOIN con Usuario para mostrar el nombre real del doctor
            String sqlDoc = "SELECT d.id_doctor, u.nombre, d.especialidad " +
                            "FROM Doctor d JOIN Usuario u ON d.id_usuario = u.id_usuario " +
                            "ORDER BY u.nombre";
            
            DefaultComboBoxModel modelDoc = new DefaultComboBoxModel();
            try (ResultSet rs = stmt.executeQuery(sqlDoc)) {
                while (rs.next()) {
                    String etiqueta = rs.getString("nombre") + " (" + rs.getString("especialidad") + ")";
                    modelDoc.addElement(new ItemCombo(rs.getInt("id_doctor"), etiqueta));
                }
            }
            cmbDoctor.setModel(modelDoc);

            // 2. Llenar Combo Pacientes
            String sqlPac = "SELECT id_paciente, nombre FROM Paciente ORDER BY nombre";
            DefaultComboBoxModel modelPac = new DefaultComboBoxModel();
            try (ResultSet rs = stmt.executeQuery(sqlPac)) {
                while (rs.next()) {
                    modelPac.addElement(new ItemCombo(rs.getInt("id_paciente"), rs.getString("nombre")));
                }
            }
            cmbPaciente.setModel(modelPac);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando listas: " + e.getMessage());
        }
    }
    
    private void cargarDatosParaEditar(int id) {
        // Obtenemos los datos, formateando la fecha a texto para mostrarla
        String sql = "SELECT TO_CHAR(fecha, 'DD/MM/YYYY HH24:MI') as fecha_txt, " +
                     "estado, id_doctor, id_paciente FROM Cita WHERE id_cita = " + id;

        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                // Poner la fecha en el campo de texto
                txtFecha.setText(rs.getString("fecha_txt"));
                
                // Seleccionar Estado
                cmbEstado.setSelectedItem(rs.getString("estado"));

                // Seleccionar Doctor en el Combo
                int idDocBD = rs.getInt("id_doctor");
                seleccionarEnCombo(cmbDoctor, idDocBD);

                // Seleccionar Paciente en el Combo
                int idPacBD = rs.getInt("id_paciente");
                seleccionarEnCombo(cmbPaciente, idPacBD);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar cita: " + e.getMessage());
        }
    }
    
    private void seleccionarEnCombo(JComboBox combo, int idBuscado) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            ItemCombo item = (ItemCombo) combo.getItemAt(i);
            if (item.id == idBuscado) {
                combo.setSelectedIndex(i);
                break;
            }
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

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        jButCancelar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        cmbDoctor = new javax.swing.JComboBox<>();
        cmbPaciente = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText("Doctor");

        jLabel3.setText("Estado");

        jLabel4.setText("Paciente");

        btnGuardar.setText("Insertar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel5.setText("Fecha");

        txtFecha.setText("25/12/2000");
        txtFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaActionPerformed(evt);
            }
        });

        jButCancelar.setText("Cancelar");
        jButCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButCancelarActionPerformed(evt);
            }
        });

        lblTitulo.setText("Titulo ");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PROGRAMADA", "CANCELADA", "ATENDIDA" }));

        cmbDoctor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbPaciente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(lblTitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(28, 28, 28)
                                    .addComponent(jButCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel2))
                                            .addGap(152, 152, 152)))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(0, 165, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmbDoctor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(97, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:
        
        this.dispose();
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
    // 1. Validaciones
        if (txtFecha.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha y hora son obligatorias.\nFormato: dd/mm/yyyy HH:mm (Ej: 25/12/2023 14:30)");
            return;
        }

        if (cmbDoctor.getSelectedItem() == null || cmbPaciente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un Doctor y un Paciente.");
            return;
        }

        // 2. Obtener Datos
        String fechaTexto = txtFecha.getText().trim();
        // Usamos TO_DATE con formato de 24 horas y minutos
        String fechaSQL = "TO_DATE('" + fechaTexto + "', 'DD/MM/YYYY HH24:MI')";
        
        String estado = cmbEstado.getSelectedItem().toString(); // "PROGRAMADA", "CANCELADA", etc.
        
        // Extraer IDs de los objetos ItemCombo
        int idDoctor = ((ItemCombo) cmbDoctor.getSelectedItem()).id;
        int idPaciente = ((ItemCombo) cmbPaciente.getSelectedItem()).id;
        
        // 3. Obtener Usuario que registra (Auditoría/Seguridad)
        int idUsuarioRegistra = 1; // Valor por defecto porsiacaso
        if (sesion.haySesion()) {
            idUsuarioRegistra = sesion.getUsuario().getIdUsuario();
        } else {
            System.out.println("ADVERTENCIA: No hay sesión activa. Usando ID 1 por defecto.");
        }

        String sql = "";

        // 4. Decidir Query (Sin '')
        if (idCitaActual == 0) {
            // --- INSERTAR ---
            sql = "INSERT INTO Cita (id_cita, fecha, estado, id_doctor, id_paciente, id_usuario) VALUES (" +
                  "seq_cita.NEXTVAL, " + 
                  fechaSQL + ", " +
                  "'" + estado + "', " +
                  idDoctor + ", " +
                  idPaciente + ", " +
                  idUsuarioRegistra + ")";
        } else {
            // --- ACTUALIZAR ---
            // Nota: Usualmente no cambiamos el 'id_usuario' (quien la creó), solo modificamos datos
            sql = "UPDATE Cita SET " +
                  "fecha = " + fechaSQL + ", " +
                  "estado = '" + estado + "', " +
                  "id_doctor = " + idDoctor + ", " +
                  "id_paciente = " + idPaciente + " " +
                  "WHERE id_cita = " + idCitaActual;
        }

        // 5. Ejecutar
        if (conexion.ejecutarSQL(sql)) {
            JOptionPane.showMessageDialog(this, "Cita guardada correctamente.");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar.\nVerifica:\n1. Formato de fecha (dd/mm/yyyy HH:mm)\n2. Que el doctor no tenga otra cita a esa misma hora.");
        }  
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaActionPerformed

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
                jDialogCitas dialog = new jDialogCitas(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> cmbDoctor;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbPaciente;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtFecha;
    // End of variables declaration//GEN-END:variables
}
