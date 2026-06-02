import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
// import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreParent extends JFrame {

    
    private JTabbedPane tabbedPane;

    public FenetreParent() {
        setTitle("Espace Parents - Inscription en ligne");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===================== HEADER =====================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setPreferredSize(new Dimension(1000, 80));

        JLabel titleLabel = new JLabel("Tableau de Bord - Espace Parents", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Salut " + "|" +  "Bienvenue");
        userLabel.setForeground(Color.WHITE);
        JButton logoutBtn = new JButton("Déconnexion");
        logoutBtn.addActionListener(e -> dispose());

        userPanel.add(userLabel);
        userPanel.add(logoutBtn);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(userPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // ===================== TABBED PANE =====================
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        tabbedPane.addTab("Accueil", createAccueil());
        tabbedPane.addTab("Mes Enfants", voirStatutEnfants());
        tabbedPane.addTab("Consulter les Notes", voirNotes());
        tabbedPane.addTab("Soumettre un Dossier", SoumettreDossier());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ===================== PANNEAU ACCUEIL =====================
    private JPanel createAccueil() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcome = new JLabel("Bienvenue dans votre espace parent !", SwingConstants.CENTER);
        panel.add(welcome);

        JPanel actions = new JPanel(new GridLayout(1, 3, 15, 15));
        actions.add(creerActionButton("Consulter Notes", e -> tabbedPane.setSelectedIndex(2)));
        actions.add(creerActionButton("Soumettre Dossier", e -> tabbedPane.setSelectedIndex(3)));
        actions.add(creerActionButton("Mes Enfants", e -> tabbedPane.setSelectedIndex(1)));

        panel.add(actions);
        return panel;
    }

    // ===================== PANNEAU MES ENFANTS =====================
    private JPanel voirStatutEnfants() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Liste des enfants
        try (Connection conn = BaseDonnees.getConnexion()) {
            String sql = "SELECT nom, prenom, datenaissance, classe, statut FROM eleves WHERE matriculeparent = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 1); // ID parent (à adapter selon l'authentification)
            ResultSet rs = pstmt.executeQuery();

            DefaultListModel<String> listModel = new DefaultListModel<>();
            while (rs.next()) {
                String enfantInfo = rs.getString("nom") + " " + rs.getString("prenom") + 
                                    " - " + rs.getString("classe") + 
                                    " - " + rs.getInt("datenaissance") + 
                                    " - " + rs.getString("statut");
                listModel.addElement(enfantInfo);
            }
            JList<String> listEnfants = new JList<>(listModel);
            panel.add(new JScrollPane(listEnfants));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des enfants : "
                    + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        return panel;
    }

    // ===================== PANNEAU CONSULTER NOTES =====================
    private JPanel voirNotes() {
        String valeur = JOptionPane.showInputDialog(this, "Entrez le matricule de l'enfant pour voir ses notes :");
        if(valeur == null || valeur.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Matricule invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }else {
            // Vérifier si le matricule existe dans la BD
            try (Connection conn = BaseDonnees.getConnexion()) {
                String sqlCheck = "SELECT COUNT(*) FROM eleves WHERE matricule = ?";
                PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
                pstmtCheck.setString(1, valeur.trim());
                ResultSet rsCheck = pstmtCheck.executeQuery();
                if (rsCheck.next() && rsCheck.getString(1).equals("0")) {
                    JOptionPane.showMessageDialog(this, "Aucun élève trouvé avec ce matricule !", "Erreur", JOptionPane.ERROR_MESSAGE);
             
                }else{
                    JPanel panel = new JPanel(new BorderLayout(10, 10));
                    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    topPanel.add(new JLabel("Notes de l'élève : " + valeur));

                    JButton btnActualiser = new JButton("Actualiser");
                    topPanel.add(btnActualiser);

                    // Tableau des notes
                    String[] columns = {"Matière", "Note ", "Coefficient", "Total", "Appréciation"};
                    String sql2 = "SELECT matiere, note, coefficient, total, apprecation FROM notes WHERE matriculeeleve = ?";
                        try (Connection connn = BaseDonnees.getConnexion()) {
                           PreparedStatement pstmt = connn.prepareStatement(sql2);
                           pstmt.setString(1, valeur.trim()); // Matricule de l'élève (à adapter selon la sélection)
                           ResultSet rs = pstmt.executeQuery();

                           DefaultTableModel model = new DefaultTableModel(columns, 0);
                           while (rs.next()) {
                              Object[] row = {
                                rs.getString("matiere"),
                                rs.getFloat("note"),
                                rs.getInt("coefficient"),
                                rs.getFloat("total"),
                                rs.getString("apprecation")
                                };
                                model.addRow(row);
                           }
                        JTable tableNotes = new JTable(model);
                        JScrollPane scrollPane = new JScrollPane(tableNotes);
                        panel.add(scrollPane, BorderLayout.CENTER);

                        } catch (SQLException e) {
                           JOptionPane.showMessageDialog(this, "Erreur lors du chargement des notes : "
                                 + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                        return panel;
                        }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la vérification du matricule : "
                        + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                return new JPanel(); // Retourne un panneau vide
            }
        }
        return new JPanel();// retourne un panneau vide si le matricule est invalide ou si l'utilisateur annule l'entrée
        
    }

    // ===================== PANNEAU SOUMETTRE DOSSIER =====================
    private JPanel SoumettreDossier() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Sélection enfant
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nom de l'enfant :"), gbc);
        gbc.gridx = 1;
        JTextField txtNomEnfant = new JTextField(20);
        panel.add(txtNomEnfant, gbc);


          gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Prenom de l'enfant :"), gbc);
        gbc.gridx = 1;
        JTextField txtPrenomEnfant = new JTextField(20);
        panel.add(txtPrenomEnfant, gbc);
      
          gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Date de naissance :"), gbc);
        gbc.gridx = 1;
        JTextField txtdateEnfant = new JTextField(20);
        panel.add(txtdateEnfant, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Classe :"), gbc);
        JComboBox<Classe> comboType = new JComboBox<>(Classe.values());
        panel.add(comboType, gbc);

        // Bouton Soumettre
        gbc.gridx = 1; gbc.gridy = 4;
        JButton btnSoumettre = new JButton("Soumettre le Dossier");
        btnSoumettre.setBackground(new Color(0, 153, 0));
        btnSoumettre.setForeground(Color.WHITE);
        btnSoumettre.setFont(new Font("Arial", Font.BOLD, 14));
        String sql = "INSERT INTO eleves (matricule, nom, prenom, datenaissance, classe, statut) VALUES (?, ?, ?, ?, ?, ?)";
        String matricule = creerMatricule(txtNomEnfant.getText(), txtPrenomEnfant.getText());
        btnSoumettre.addActionListener(e -> {
            if (txtNomEnfant.getText().isEmpty() || txtPrenomEnfant.getText().isEmpty()
                 || txtdateEnfant.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !" , "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                try (Connection conn = BaseDonnees.getConnexion()) {
                    
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, matricule);
                    pstmt.setString(2, txtNomEnfant.getText());
                    pstmt.setString(3, txtPrenomEnfant.getText());
                    pstmt.setInt(4, Integer.parseInt(txtdateEnfant.getText()));
                    pstmt.setString(5, comboType.getSelectedItem().toString());
                    pstmt.setString(6, "En Attente");
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Dossier soumis avec succès !");
                    JOptionPane.showMessageDialog(this, "Matricule de l'enfant : " + matricule + "\nVeiller le conserver jalousement.", "Information", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la soumission du dossier : "
                    + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }   
            }
        });

        panel.add(btnSoumettre, gbc);

        return panel;
    }

    private JButton creerActionButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(200, 80));
        btn.addActionListener(listener);
        return btn;
    }
    private String creerMatricule(String nom, String prenom) {
        String initiales = (prenom.charAt(0) + "" + nom.charAt(0)).toUpperCase();
        String numero = String.format("%04d", (int)(Math.random() * 10000));
        return "A" + initiales + numero;
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FenetreParent().setVisible(true);
        });
    }
}