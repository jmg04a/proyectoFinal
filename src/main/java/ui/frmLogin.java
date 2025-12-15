package ui;

import dao.UsuarioDAO;
import modelo.Usuario;
import seguridad.PasswordUtil;
import seguridad.sesion;

import javax.swing.*;
import java.awt.*;

public class frmLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public frmLogin() {
        setTitle("Sistema Clínica - Login");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        btnLogin = new JButton("Iniciar sesión");
        btnLogin.addActionListener(e -> login());

        add(panel, BorderLayout.CENTER);
        add(btnLogin, BorderLayout.SOUTH);
    }

    private void login() {

        String username = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Usuario y contraseña obligatorios");
            return;
        }

        try {
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.login(username, PasswordUtil.sha256(password));

            if (usuario == null) {
                JOptionPane.showMessageDialog(this,
                        "Usuario o contraseña incorrectos");
                return;
            }

            sesion.iniciarSesion(usuario, usuario.getRol());

            JOptionPane.showMessageDialog(this,
                    "Bienvenido " + usuario.getNombre() +
                    "\nRol: " + usuario.getRol());

            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new frmLogin().setVisible(true));
    }
}

