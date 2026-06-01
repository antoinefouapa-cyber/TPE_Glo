import javax.swing.*;
// import javax.swing.table.DefaultTableModel;
import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FenetreEnseignant extends JFrame {

    public FenetreEnseignant() {
        setTitle("Espace Enseignant - Saisie des Notes");
        setSize(1150, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===================== HEADER =====================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 51));
        headerPanel.setPreferredSize(new Dimension(1150, 80));

        JLabel titleLabel = new JLabel("Espace Enseignant", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("ENSEIGNANT");
        userLabel.setForeground(Color.WHITE);
        JButton logoutBtn = new JButton("Déconnexion");
        logoutBtn.addActionListener(e -> dispose());

        userPanel.add(userLabel);
        userPanel.add(logoutBtn);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(userPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // ===================== TABBED PANE =====================
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        tabbedPane.addTab("Accueil", createAccueilPanel());
        // tabbedPane.addTab("Mes Classes", createMesClassesPanel());
        tabbedPane.addTab("Saisie & Modification des Notes", SaisieNotes());
        // tabbedPane.addTab("Mes Cours", createMesCoursPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ===================== ACCUEIL =====================
    private JPanel createAccueilPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(creerStatCard("Classes enseignées", "3", new Color(0, 102, 51)));
        panel.add(creerStatCard("Notes saisies", "87%", new Color(0, 153, 0)));
        panel.add(creerStatCard("Élèves suivis", "124", new Color(0, 102, 204)));
        panel.add(creerStatCard("Prochain devoir", "Math - 6ème A", new Color(255, 140, 0)));

        return panel;
    }

    private JPanel creerStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(color, 3));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 42));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ===================== SAISIE & MODIFICATION DES NOTES =====================
    private JPanel SaisieNotes() {
        String valeur = JOptionPane.showInputDialog(this, "Entrez le matricule de l'élève pour saisir/modifier ses notes :");
            if (valeur == null || valeur.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Matricule invalide. Veuillez réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return null;
            }else{
                String sqlCheck = "SELECT COUNT(*) FROM eleves WHERE matricule = ?";
                try (Connection conn1= BaseDonnees.getConnection();
                    PreparedStatement pstmtCheck = conn1.prepareStatement(sqlCheck)){
                    pstmtCheck.setString(1, valeur.trim());
                    ResultSet rsCheck = pstmtCheck.executeQuery();
                    if (rsCheck.next() && rsCheck.getString(1).equals("0")) {
                       JOptionPane.showMessageDialog(this, "Aucun élève trouvé avec ce matricule !", "Erreur", JOptionPane.ERROR_MESSAGE);
                       rsCheck.close();
                       return null;
                    }else{
                       JPanel panel = new JPanel(new GridBagLayout());
                       panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                       GridBagConstraints gbc = new GridBagConstraints();
                       gbc.insets = new Insets(10, 10, 10, 10);
                       gbc.anchor = GridBagConstraints.WEST;

                        // Champs de saisie pour les notes
                       gbc.gridx = 0; gbc.gridy = 0;
                       panel.add(new JLabel("Matière :"), gbc);
                       gbc.gridx = 1;
                       JTextField txtmatiere = new JTextField(20);
                       panel.add(txtmatiere, gbc);

                       gbc.gridx = 0; gbc.gridy = 1;
                       panel.add(new JLabel("Note :"), gbc);
                       gbc.gridx = 1;
                       JTextField txtNote = new JTextField(20);
                       panel.add(txtNote, gbc);

                       gbc.gridx = 0; gbc.gridy = 2;
                       panel.add(new JLabel("Coefficient :"), gbc);
                       gbc.gridx = 1;
                       JTextField txtcoef = new JTextField(20);
                       panel.add(txtcoef, gbc);

                       gbc.gridx = 1; gbc.gridy = 4;
                       JButton btnsauv = new JButton("Sauvegarder ");
                       btnsauv.setBackground(new Color(0, 153, 0));
                       btnsauv.setForeground(Color.WHITE);
                       btnsauv.setFont(new Font("Arial", Font.BOLD, 14));
                       String sql = "INSERT INTO notes (matricule, matiere, note, coefficient,apreciation) VALUES (?, ?, ?, ?, ?)";
                       btnsauv.addActionListener(e -> {
                            if (txtmatiere.getText().isEmpty() || txtNote.getText().isEmpty()
                                || txtcoef.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !" , "Erreur", JOptionPane.ERROR_MESSAGE);
                            } else {
                                try (Connection conn2 = BaseDonnees.getConnection();
                                    PreparedStatement pstmt = conn2.prepareStatement(sql)){
                                    pstmt.setString(1, valeur);
                                    pstmt.setString(2, txtmatiere.getText());
                                    pstmt.setString(3, txtNote.getText());
                                    pstmt.setInt(4, Integer.parseInt(txtcoef.getText()));
                                    pstmt.setString(5, Apreciation(Float.parseFloat(txtNote.getText())));
                                    pstmt.executeUpdate();
                                    JOptionPane.showMessageDialog(this, "Notes enregistrées avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);

                                } catch (SQLException ex) {
                                    JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                                }   
                            }
                         });
                          panel.add(btnsauv, gbc);
                          return panel;
                        }
                    }catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }
     }
    
     private String Apreciation(float note){
        if (note >= 16) {
            return "Excellent";
        } else if (note >= 14) {
            return "Très Bien";
        } else if (note >= 12) {
            return "Bien";
        } else if (note >= 10) {
            return "Assez Bien";
        } else if (note >= 8) {
            return "Passable";
        } else {
            return "Insuffisant";
        }
     }
    

    // ===================== MAIN =====================
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         new FenetreEnseignant().setVisible(true);
    //     });
    // }
}