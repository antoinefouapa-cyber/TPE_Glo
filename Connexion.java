
import javax.swing.*;
import java.awt.*;
import java.sql.*;


public class Connexion extends JFrame {
    private JTextField txtmail;
    private JPasswordField txtmotpass;
    private JLabel msglabel;
   // private AuthService authservice;

    public Connexion(){
       // initialiserUtilisateurs();

        setTitle("Systeme d'inscription en ligne");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10,10,10);

        JLabel titre = new JLabel("Connexion au Systeme Scolaire");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titre, gbc);

        // revenir à une seule colonne pour les champs suivants
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        JLabel email = new JLabel("Email :");
        email.setForeground(new Color(0,51,204));
        add(email, gbc);
        txtmail = new JTextField(20);
        gbc.gridx = 1;
        // permettre au champ texte de s'etirer horizontalement
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(txtmail, gbc);

        //gbc.gridheight = 1;
        gbc.gridy = 2; gbc.gridx = 0;
        add(new JLabel("Mot-Passe :"), gbc);
        txtmotpass = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(txtmotpass, gbc);

        msglabel = new JLabel("", SwingConstants.CENTER);
        msglabel.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy =4; gbc.gridwidth = 2;
        add(msglabel, gbc);

        JButton btncon = new JButton("Se Connecter");
        btncon.setBackground(new Color(0,123,255));
        btncon.setForeground  (Color.WHITE);
        btncon.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(btncon, gbc);

        btncon.addActionListener(e -> gererConnexion());
        // ajuster la taille selon les composants et recentrer
        pack();
        setLocationRelativeTo(null);
    }


    private void gererConnexion(){
        String email = txtmail.getText().trim();
        String mdp = new String(txtmotpass.getPassword());
        if (email.isEmpty() || mdp.isEmpty()) {
            msglabel.setText("Ces Champs doivent etre remplir");
            msglabel.setForeground(Color.RED);
            return;
        }
        Connection conn = BaseDonnees.getConnection();
        if (conn == null) {
            msglabel.setText("Connexion a la base donnees Echouer !");
            msglabel.setForeground(Color.RED);
            return;
        }
        try{
            PreparedStatement ps =conn.prepareStatement(
                "SELECT * FROM utilisateurs WHERE email = ? AND mot_passe = ?"
            );
            ps.setString(1, email);
            ps.setString(2, mdp);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                String role = rs.getString("role");
                dispose();

                switch (role) {
                    case "PARENT":
                        new FenetreParent().setVisible(true);
                        break;
                    case "ENSEIGNANT":
                        new FenetreEnseignant().setVisible(true);
                        break;
                    case "SCOLARITE":
                        new FenetreScolarite().setVisible(true);
                        break;
                    default:
                        break;
                }
            }else{
                msglabel.setText("Email ou Mot de Passe incorret");
                msglabel.setForeground(Color.RED);
                txtmotpass.setText("");
            }
            rs.close(); ps.close(); conn.close();
        }catch (SQLException e){
            msglabel.setText("Erreur BD: " + e.getMessage());
            msglabel.setForeground(Color.RED);
        }
    }


    // // public static void main(String[] args){
    // //     SwingUtilities.invokeLater(() ->{
    // //          new Connexion().setVisible(true);
            
    // //     });
    // }
 
}
