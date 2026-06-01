import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame{
      private JTextField txtnom, txtprenom, txtmail;
      private JPasswordField txtmotpass, txtconfPass;
      private JComboBox<Role> txtrole;


      public Login(){
            setTitle("Creation Compte");
	        setSize(550, 550);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setResizable(false);

	        JPanel mainpanel = new JPanel(new GridBagLayout());
	        mainpanel.setBackground(Color.WHITE);
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(10, 15,10,15);
	        gbc.fill = GridBagConstraints.HORIZONTAL;

	        JLabel titre = new JLabel("Creation d'un Compte ", SwingConstants.CENTER);
	        titre.setFont(new Font("Arial", Font.BOLD, 20));
	        titre.setForeground(new Color(0, 51,102));
	        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
	        mainpanel.add(titre, gbc);

	        
	        gbc.gridwidth = 1;
	        gbc.fill = GridBagConstraints.NONE;
	        gbc.anchor = GridBagConstraints.WEST;
	        gbc.gridy = 1; gbc.gridx = 0;
	        mainpanel.add(new JLabel("Nom :"),gbc);
	        txtnom = new JTextField(20);
	        gbc.gridx = 1;
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        mainpanel.add(txtnom,gbc);

	        gbc.gridy = 2; gbc.gridx = 0;
	        mainpanel.add(new JLabel("Prenom :"),gbc);
	        txtprenom = new JTextField(20);
	        gbc.gridx = 1;
	        mainpanel.add(txtprenom,gbc);

	        gbc.gridy = 3; gbc.gridx = 0;
	        mainpanel.add(new JLabel("Email :"),gbc);
	        txtmail = new JTextField(20);
	        gbc.gridx = 1;
	        mainpanel.add(txtmail,gbc);

	        gbc.gridy = 4; gbc.gridx = 0;
	        mainpanel.add(new JLabel("Mot de Passe :"),gbc);
	        txtmotpass = new JPasswordField(20);
	        gbc.gridx = 1;
	        mainpanel.add(txtmotpass,gbc);

	        gbc.gridy = 5; gbc.gridx = 0;
	        mainpanel.add(new JLabel("Confirmer Mot de Passe :"),gbc);
	        txtconfPass = new JPasswordField(20);
	        gbc.gridx = 1;
	        mainpanel.add(txtconfPass,gbc);

	        gbc.gridy = 6; gbc.gridx = 0;
	        mainpanel.add(new JLabel("Qui Etes vous ? :"),gbc);
	        txtrole = new JComboBox<>(Role.values());
	        gbc.gridx = 1;
	        mainpanel.add(txtrole,gbc);

	        JButton btncon = new JButton("Creer Compte");
	        btncon.setBackground(new Color(40,167,69));
	        btncon.setForeground  (Color.WHITE);
	        btncon.setFont(new Font("Arial", Font.BOLD, 14));
	        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
	        mainpanel.add(btncon, gbc);

	        btncon.addActionListener(e ->creerCompte());

	        setContentPane(mainpanel);
	      }


		  private static final Object BD_LOCK = new Object();
	      private void creerCompte(){
	        String nom = txtnom.getText();
	        String prenom = txtprenom.getText();
	        String mail = txtmail.getText();
	        String mdp = new String(txtmotpass.getPassword());
	        String cfpss = new String(txtconfPass.getPassword());
	        Role role = (Role) txtrole.getSelectedItem();

	        if(nom.trim().isEmpty() || prenom.trim().isEmpty() || mail.trim().isEmpty() || mdp.trim().isEmpty()){
	            JOptionPane.showMessageDialog(this,"tous les champs sont obligatoire !",
	                "Erreur", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        
	        if(!mdp.equals(cfpss)){
	            JOptionPane.showMessageDialog(this, "Les mots de passes sont pas identique !",
	             "Erreur", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
			if (mdp.length() < 8) {
	            JOptionPane.showMessageDialog(this, "La longueur max du mot de passe est de 8 caracteres !",
	             "Erreur", JOptionPane.ERROR_MESSAGE);
	             return;
			}

           synchronized (BD_LOCK){
			 String sql = "insert into utilisateurs (nom, prenom, email,mot_passe, role) values(?,?,?,?,?)";
            try(Connection conn = BaseDonnees.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
                    pstmt.setString(1, nom);
                    pstmt.setString(2, prenom);
                    pstmt.setString(3, mail);
                    pstmt.setString(4, mdp);
                    pstmt.setString(5, role.name());

                    int row = pstmt.executeUpdate();
					if (row > 0) {
						JOptionPane.showMessageDialog(this, "Felicitation ! vous avez actuelement un Compte!",
	             "Success", JOptionPane.INFORMATION_MESSAGE);
				 txtnom.setText("");
				 txtprenom.setText("");
				 txtmail.setText("");
				 txtmotpass.setText("");
				 txtconfPass.setText("");
					}
					
                }catch (SQLException e){
                    if (e.getMessage() != null  && e.getMessage().contains("UNIQUE")) {
                        JOptionPane.showMessageDialog(this, "Cet email existe deja !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }else{
                        e.printStackTrace();
						JOptionPane.showMessageDialog(this, "Erreur lors de la creation de compte"+ e.getMessage(), 
						"Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    
        }
		   }
	}
}
