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

/**
 *
 * @author jose
 */
public class jDialogHistorial extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(jDialogHistorial.class.getName());
    private int idHistorial = 0;
    private conexionBaseDatos conexion;
    
    /**
     * Creates new form jDialogPacientes
     */
    public jDialogHistorial(java.awt.Frame parent, boolean modal) {
        
        super(parent, modal);
        initComponents();
    }
    
    public jDialogHistorial(java.awt.Frame parent, boolean modal,int id) {
        super(parent, modal);
        initComponents();
        
        this.idHistorial = id; // Guardamos el ID
        
        try {
            conexion = new conexionBaseDatos(); // Preparamos la conexión
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        }

        // Lógica para decidir qué mostrar
        if (idHistorial > 0) {
            // MODO EDICIÓN
            lblTitulo.setText("EDITAR HISTORIAL");
            btnGuardar.setText("Actualizar");
            cargarDatosParaEditar(idHistorial); // Llenamos los campos
        } else {
            // MODO CREACIÓN
            lblTitulo.setText("NUEVO HISTORIAL");
            btnGuardar.setText("Guardar");
            // Los campos aparecen vacíos por defecto
        }
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
private class ItemCombo {
        int id;
        String texto;
        public ItemCombo(int id, String texto) { this.id = id; this.texto = texto; }
        @Override
        public String toString() { return texto; }
    }
    private void cargarDatosParaEditar(int id) {
String sql = "SELECT FECHA_CONSULTA, DIAGNOSTICO, NOTAS, ID_PACIENTE, ID_DOCTOR " +
                 "FROM historial_clinico " +
                 "WHERE id_historial = ?";        
        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                // Asumiendo que tus variables se llaman txtNombre, txtTelefono, etc.
               // 📅 Fecha de consulta
            java.sql.Date fechaBD = rs.getDate("FECHA_CONSULTA");
            if (fechaBD != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                txtFechaConsulta.setText(sdf.format(fechaBD));
            }

            // 🩺 Diagnóstico
            txtDiagnostico.setText(rs.getString("DIAGNOSTICO"));

            // 📝 Notas
            txtNotas.setText(rs.getString("NOTAS"));

            // 👤 Paciente
            int idPaciente = rs.getInt("ID_PACIENTE");
            llenarCombos();

            // 
            int idDoctor = rs.getInt("ID_DOCTOR");
            
            // ---------------------------
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
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
        txtidHistorial = new javax.swing.JTextField();
        txtDiagnostico = new javax.swing.JTextField();
        txtNotas = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtFechaConsulta = new javax.swing.JTextField();
        jButCancelar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbPaciente = new javax.swing.JComboBox<>();
        cmbDoctor = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("ID-Historial");

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

        txtFechaConsulta.setText("25/12/2000");
        txtFechaConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaConsultaActionPerformed(evt);
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
                                .addComponent(jLabel2)
                                .addComponent(jLabel1))
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNotas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtidHistorial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFechaConsulta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDiagnostico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(cmbPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(17, 17, 17)
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
                        .addComponent(txtidHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtFechaConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDiagnostico, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNotas, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
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
            
    // --- 1. VALIDACIONES ---
    if (txtFechaConsulta.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "La fecha de consulta es obligatoria.");
        return;
    }

    if (txtDiagnostico.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El diagnóstico es obligatorio.");
        return;
    }

    if (cmbPaciente.getSelectedIndex() == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un paciente.");
        return;
    }

    if (cmbDoctor.getSelectedIndex() == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un doctor.");
        return;
    }

    // --- 2. OBTENER DATOS ---
    String diagnostico = txtDiagnostico.getText().trim();
    String notas = txtNotas.getText().trim();

    
    int idDoctor = ((jDialogHistorial.ItemCombo) cmbDoctor.getSelectedItem()).id;
    int idPaciente = ((jDialogHistorial.ItemCombo) cmbPaciente.getSelectedItem()).id;

    // --- 3. FECHA ---
    java.sql.Date fechaSQL;
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fecha = sdf.parse(txtFechaConsulta.getText().trim());
        fechaSQL = new java.sql.Date(fecha.getTime());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/MM/yyyy.");
        return;
    }

    // --- 4. INSERT O UPDATE ---
    String sql;

    if (idHistorial == 0) {
        // 🟢 INSERT
        sql = "INSERT INTO historial_clinico " +
              "(id_historial, fecha_consulta, diagnostico, notas, id_paciente, id_doctor) " +
              "VALUES (seq_historial.NEXTVAL, ?, ?, ?, ?, ?)";
    } else {
        // 🔵 UPDATE
        sql = "UPDATE historial_clinico SET " +
              "fecha_consulta = ?, diagnostico = ?, notas = ?, id_paciente = ?, id_doctor = ? " +
              "WHERE id_historial = ?";
    }

    // --- 5. EJECUTAR ---
    try (Connection conn = conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setDate(1, fechaSQL);
        ps.setString(2, diagnostico);
        ps.setString(3, notas);
        ps.setInt(4, idPaciente);
        ps.setInt(5, idDoctor);

        if (idHistorial > 0) {
            ps.setInt(6, idHistorial);
        }

        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Historial guardado correctamente.");
        dispose();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
                "Error al guardar historial: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }


        
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtFechaConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaConsultaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaConsultaActionPerformed

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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtDiagnostico;
    private javax.swing.JTextField txtFechaConsulta;
    private javax.swing.JTextField txtNotas;
    private javax.swing.JTextField txtidHistorial;
    // End of variables declaration//GEN-END:variables
}
