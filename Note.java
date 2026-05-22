import java.util.Date;

public class Note {

    private int id;
    private float valeur;
    private String matiere;
    private Date dateEvaluation;
    private String commentaire;
    private Eleve eleve;

    public Note(int id, float valeur, String matiere, Eleve eleve) {
        this.id = id;
        this.valeur = valeur;
        this.matiere = matiere;
        this.eleve = eleve;
        this.dateEvaluation = new Date();
    }


    public float getNote() { return valeur; }

    public void setNote(float valeur) {
        if (valeur < 0 || valeur > 20) {
            System.out.println("Erreur: Note invalide (0-20)");
            return;
        }
        this.valeur = valeur;
    }

    public String getMention() {
        if (valeur >= 16) return "Tres Bien";
        else if (valeur >= 14) return "Bien";
        else if (valeur >= 12) return "Assez Bien";
        else if (valeur >= 10) return "Passable";
        else return "Insuffisant";
    }

    public int getId() { return id; }
    public String getMatiere() { return matiere; }
    public Date getDateEvaluation() { return dateEvaluation; }
    public String getCommentaire() { return commentaire; }
    public Eleve getEleve() { return eleve; }
    public void setCommentaire(String c) { this.commentaire = c; }

    public void setId(int id) {
        this.id = id;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public void setDateEvaluation(Date dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }
}
