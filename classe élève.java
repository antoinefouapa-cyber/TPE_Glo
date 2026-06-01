public class  Eleve{
    private final string matricule;
    private string nom;
    private string prenom;
    private localDate dateNaissance;
    private Map<String ,List<Double>> notes=new HashMap<>();
public Eleve(String matricule,String nom,String prenom,localDate dateNaissance){
    this.matricule=matricule;
    this.nom=nom;
    this.prenom=prenom;
    this.dateNaissance=dateNaissance;
}
public void ajouterNote(String matiere,double note){
    if(note<0||note>20){
        throw new IllegalArgumentException("Note invalide");
    }
    notes.computeIfAbsent(matiere,k->new ArrayList<>()).add(note);
}
public double moyenneGenerale(){
    return notes.values().stream()
    .flatMap(List::stream)
    .mapToDouble(Double::doublevalue)
    .average()
    .orElse(0.0);
}
public String getNomCompolet(){
    return prenom +"+ nom;
}
}