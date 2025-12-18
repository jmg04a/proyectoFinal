/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.dialogs;

import clases.conexionBaseDatos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author jose
 */
public class jDialogHistorial extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(jDialogHistorial.class.getName());
    
    private int idHistorialActual = 0;
    private conexionBaseDatos conexion;
    
    /**
     * Creates new form jDialogPacientes
     */
    public jDialogHistorial(java.awt.Frame parent, boolean modal) {
        
        super(parent, modal);
        initComponents();
    }
    
    public jDialogHistorial(java.awt.Frame parent, boolean modal, int id) {
        super(parent, modal);
        initComponents();
        this.idHistorialActual = id;

        try {
            conexion = new conexionBaseDatos();
            llenarCombos(); // Cargar Pacientes y Doctores
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        }

        if (idHistorialActual > 0) {
            lblTitulo.setText("EDITAR HISTORIAL");
            btnGuardar.setText("Actualizar");
            cargarDatosParaEditar(idHistorialActual);
        } else {
            lblTitulo.setText("NUEVO HISTORIAL");
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
    
    private void llenarCombos() {
        // Limpiar basura de NetBeans
        cmbPaciente.removeAllItems();
        cmbDoctor.removeAllItems();

        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement()) {

            // A. Llenar Pacientes
            String sqlPac = "SELECT id_paciente, nombre FROM Paciente ORDER BY nombre";
            try (ResultSet rs = stmt.executeQuery(sqlPac)) {
                DefaultComboBoxModel modelPac = new DefaultComboBoxModel();
                while (rs.next()) {
                    modelPac.addElement(new ItemCombo(rs.getInt("id_paciente"), rs.getString("nombre")));
                }
                cmbPaciente.setModel(modelPac);
            }

            // B. Llenar Doctores (Hacemos JOIN con Usuario para ver el nombre real)
            String sqlDoc = "SELECT d.id_doctor, u.nombre, d.especialidad " +
                            "FROM Doctor d JOIN Usuario u ON d.id_usuario = u.id_usuario " +
                            "ORDER BY u.nombre";
            try (ResultSet rs = stmt.executeQuery(sqlDoc)) {
                DefaultComboBoxModel modelDoc = new DefaultComboBoxModel();
                while (rs.next()) {
                    String etiqueta = rs.getString("nombre") + " - " + rs.getString("especialidad");
                    modelDoc.addElement(new ItemCombo(rs.getInt("id_doctor"), etiqueta));
                }
                cmbDoctor.setModel(modelDoc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando listas: " + e.getMessage());
        }
    }
    
    
    private void cargarDatosParaEditar(int id) {
        // Traemos fecha formateada y los IDs para seleccionar en los combos
        String sql = "SELECT TO_CHAR(fecha_consulta, 'DD/MM/YYYY') as fecha_txt, " +
                     "diagnostico, notas, id_paciente, id_doctor " +
                     "FROM Historial_Clinico WHERE id_historial = " + id;

        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                txtFecha.setText(rs.getString("fecha_txt"));
                txtDiagnostico.setText(rs.getString("diagnostico"));
                txtNotas.setText(rs.getString("notas")); // JDBC lee CLOB pequeños como String automáticamente

                // Seleccionar Paciente
                int idPacBD = rs.getInt("id_paciente");
                seleccionarEnCombo(cmbPaciente, idPacBD);

                // Seleccionar Doctor
                int idDocBD = rs.getInt("id_doctor");
                seleccionarEnCombo(cmbDoctor, idDocBD);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage());
        }
    }

    // Método helper para seleccionar en combo por ID
    private void seleccionarEnCombo(JComboBox combo, int idBuscado) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object obj = combo.getItemAt(i);
            if (obj instanceof ItemCombo) {
                if (((ItemCombo) obj).id == idBuscado) {
                    combo.setSelectedIndex(i);
                    break;
                }
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
        txtDiagnostico = new javax.swing.JTextField();
        txtNotas = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        jButCancelar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbPaciente = new javax.swing.JComboBox<>();
        cmbDoctor = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText("Diagnostico");

        jLabel3.setText("Notas");

        jLabel4.setText("ID-Paciente");

        btnGuardar.setText("Insertar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel5.setText("Fecha Consulta");

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

        jLabel6.setText("ID-Doctor");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jButCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(63, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel2))
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNotas, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addComponent(txtFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addComponent(txtDiagnostico, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addComponent(cmbDoctor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbPaciente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(26, 26, 26))))
            .addGroup(layout.createSequentialGroup()
                .addGap(163, 163, 163)
                .addComponent(lblTitulo)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDiagnostico, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNotas, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:
        
        this.dispose();
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
    
        // A. Validaciones
        if (txtFecha.getText().trim().isEmpty() || txtDiagnostico.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fecha y Diagnóstico son obligatorios.");
            return;
        }

        if (cmbPaciente.getSelectedItem() == null || cmbDoctor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona Paciente y Doctor.");
            return;
        }

        // B. Obtener Datos
        String fechaTexto = txtFecha.getText().trim();
        String diagnostico = txtDiagnostico.getText().trim();
        String notas = txtNotas.getText().trim(); // JTextArea
        
        // Manejo seguro de Combos
        int idPaciente = 0;
        int idDoctor = 0;
        
        try {
            idPaciente = ((ItemCombo) cmbPaciente.getSelectedItem()).id;
            idDoctor = ((ItemCombo) cmbDoctor.getSelectedItem()).id;
        } catch (ClassCastException e) {
             JOptionPane.showMessageDialog(this, "Error: Selecciona Paciente/Doctor válidos.");
             return;
        }

        // C. Preparar Fecha (Oracle)
        String fechaSQL = "TO_DATE('" + fechaTexto + "', 'DD/MM/YYYY')";

        String sql = "";

        // D. Decidir Query (INSERT / UPDATE)
        if (idHistorialActual == 0) {
            // INSERT
            sql = "INSERT INTO Historial_Clinico (id_historial, fecha_consulta, diagnostico, notas, id_paciente, id_doctor) VALUES (" +
                  "seq_historial.NEXTVAL, " +
                  fechaSQL + ", " +
                  "'" + diagnostico + "', " +
                  "'" + notas + "', " +
                  idPaciente + ", " +
                  idDoctor + ")";
        } else {
            // UPDATE
            sql = "UPDATE Historial_Clinico SET " +
                  "fecha_consulta = " + fechaSQL + ", " +
                  "diagnostico = '" + diagnostico + "', " +
                  "notas = '" + notas + "', " +
                  "id_paciente = " + idPaciente + ", " +
                  "id_doctor = " + idDoctor + " " +
                  "WHERE id_historial = " + idHistorialActual;
        }

        // E. Ejecutar
        if (conexion.ejecutarSQL(sql)) {
            JOptionPane.showMessageDialog(this, "Historial guardado correctamente.");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar (Verifica el formato de fecha).");
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
                jDialogHistorial dialog = new jDialogHistorial(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> cmbPaciente;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtDiagnostico;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtNotas;
    // End of variables declaration//GEN-END:variables
}
