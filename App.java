import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

    public App() {
        setTitle("Système d'Inscription en Ligne - Établissements Scolaires");
        setSize(720, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(240, 248, 255));

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);

        // ===================== TEXTE DE BIENVENUE =====================
        JLabel welcomeLabel = new JLabel("<html><center>Bienvenue sur la plateforme<br>d'inscription en ligne</center></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(0, 51, 102));

        JLabel subtitle = new JLabel("<html><center>Établissements Scolaires Privés de l'Adamaoua<br>Année Scolaire 2025 - 2026</center></html>", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(new Color(60, 60, 60));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(subtitle, gbc);

        // ===================== BOUTONS =====================
        gbc.gridy = 2;
        gbc.insets = new Insets(40, 20, 15, 20);
        gbc.gridwidth = 1;

        JButton btnSeConnecter = new JButton("Se Connecter");
        btnSeConnecter.setFont(new Font("Arial", Font.BOLD, 18));
        btnSeConnecter.setPreferredSize(new Dimension(280, 60));
        btnSeConnecter.setBackground(new Color(0, 102, 204));
        btnSeConnecter.setForeground(Color.WHITE);

        JButton btnCreerCompte = new JButton("Créer un Compte");
        btnCreerCompte.setFont(new Font("Arial", Font.BOLD, 18));
        btnCreerCompte.setPreferredSize(new Dimension(280, 60));
        btnCreerCompte.setBackground(new Color(0, 153, 0));
        btnCreerCompte.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(btnSeConnecter, gbc);

        gbc.gridx = 1;
        mainPanel.add(btnCreerCompte, gbc);
        

        btnSeConnecter.addActionListener(e -> {
            new Connexion().setVisible(true);
            dispose();
        });
        btnCreerCompte.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        add(mainPanel, BorderLayout.CENTER);

        // Pied de page
        JLabel footer = new JLabel(" Projets Génie Logiciel dans la region de  l'Adamaoua", SwingConstants.CENTER);
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setForeground(Color.GRAY);
        add(footer, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new App().setVisible(true);
        });
    }
}