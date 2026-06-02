import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

public class FenetreScolarite extends JFrame {

    public FenetreScolarite() {
        setTitle("Service de la Scolarité - Gestion des Inscriptions");
        setSize(1150, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===================== HEADER =====================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 51, 102));
        headerPanel.setPreferredSize(new Dimension(1150, 80));

        JLabel titleLabel = new JLabel("Service de la Scolarité - Adamaoua", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Agent Scolarité");
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

        tabbedPane.addTab("Accueil", Accueil());
        tabbedPane.addTab("Inscriptions en Attente", GererInscription());
        tabbedPane.addTab("Liste des Élèves", ListeEleves());
        // tabbedPane.addTab("Dossiers", creerDossiersPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ===================== ACCUEIL =====================
    private JPanel Accueil() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(creerStatCard("Inscriptions en attente", "", new Color(255, 140, 0)));
        panel.add(creerStatCard("Dossiers validés", "", new Color(0, 153, 0)));
        panel.add(creerStatCard("Dossiers Rejettés", "", new Color(0, 153, 0)));

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

    // ===================== INSCRIPTIONS EN ATTENTE (PRINCIPAL) =====================
    private JPanel GererInscription() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {"MATRICULE", "NOM", "PRENOM", "CLASSE", "DATE DE NAISSANCE","STATUT DOSSIER"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columns);
        JTable table = new JTable(tableModel);

        String sql = "SELECT * FROM eleves WHERE statut = 'En Attente'";
        try(Connection conn = BaseDonnees.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Object datas[] = {
                    rs.getString("matricule"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("classe"),
                    rs.getString("datenaissance"),
                    rs.getString("statut")
                };
                tableModel.addRow(datas);
                }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des inscriptions : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
      

        
        JScrollPane scroll = new JScrollPane(table);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnValider = new JButton("VALIDER ");
        btnValider.setBackground(new Color(0, 153, 0));
        btnValider.setForeground(Color.WHITE);
        btnValider.setFont(new Font("Arial", Font.BOLD, 14));
        btnValider.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String matricule = table.getValueAt(selectedRow, 0).toString();
                String updateSql = "UPDATE eleves SET statut = 'VALIDER' WHERE matricule = ?";
                try (Connection conn = BaseDonnees.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setString(1, matricule);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Dossier validé !");
                        tableModel.removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la validation du dossier.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la validation du dossier : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un dossier à valider.", "Aucun dossier sélectionné", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton btnRejeter = new JButton(" REFUSER");
        btnRejeter.setBackground(new Color(204, 0, 0));
        btnRejeter.setForeground(Color.WHITE);
        btnRejeter.setFont(new Font("Arial", Font.BOLD, 14));
        btnRejeter.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String matricule = table.getValueAt(selectedRow, 0).toString();
                String updateSql = "UPDATE eleves SET statut = 'REJETER' WHERE matricule = ?";
                try (Connection conn = BaseDonnees.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setString(1, matricule);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Dossier rejeté !");
                        tableModel.removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors du changement de statut.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors du rejet du dossier : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un dossier à rejeter.", "Aucun dossier sélectionné", JOptionPane.WARNING_MESSAGE);
            }
        });

        actionPanel.add(btnValider);
        actionPanel.add(btnRejeter);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ===================== LISTE DES ÉLÈVES =====================
    private JPanel ListeEleves() {
         JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {"MATRICULE", "NOM", "PRENOM", "CLASSE", "DATE DE NAISSANCE","STATUT DOSSIER"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columns);
        JTable table = new JTable(tableModel);

        String sql = "SELECT * FROM eleves ";
        try(Connection conn = BaseDonnees.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

               while (rs.next()) {
                 Object datas[] = {
                    rs.getString("matricule"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("classe"),
                    rs.getString("datenaissance"),
                    rs.getString("statut")
                };
                tableModel.addRow(datas);
               }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des inscriptions : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
      

        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }


    // // ===================== MAIN =====================
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         new FenetreScolarite().setVisible(true);
    //     });
    // }
}