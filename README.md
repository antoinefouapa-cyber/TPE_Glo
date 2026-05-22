classe parent
private int idParent;
private String nom;
private String prenom;
private String telephone;
private String email;
private String motDePasse;
private String adresse;

public Parent(int idParent, String nom, String prenom, String telephone,
              String email, String motDePasse, String adresse) {
    this.idParent = idParent;
    this.nom = nom;
    this.prenom = prenom;
    this.telephone = telephone;
    this.email = email;
    this.motDePasse = motDePasse;
    this.adresse = adresse;
}

public void creerCompte() {
    System.out.println("Compte parent créé avec succès pour : " + nom + " " + prenom);
}

public boolean seConnecter(String email, String motDePasse) {
    return this.email.equals(email) && this.motDePasse.equals(motDePasse);
}

public void soumettreDossier(String nomEleve) {
    System.out.println("Le parent " + nom + " a soumis le dossier de l'élève : " + nomEleve);
}

public void consulterInscription(String nomEleve, String statut) {
    System.out.println("Inscription de " + nomEleve + " : " + statut);
}

public void consulterNotes(String nomEleve, double note) {
    System.out.println("Note de " + nomEleve + " : " + note + "/20");
}

public String getNomComplet() {
    return nom + " " + prenom;
}
