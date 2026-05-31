import java.util.List;

public class Parent extends Utilisateurs{
    private List<Eleves> enfants = new ArrayList<>();    
    public Parent(String nom, String prenom, String email, String motDePasse, Role role){  
        super(nom, prenom, email, motDePasse, Role.PARENT); 
    }  
     public List<Eleves> getEleves(){ 
         return enfants; 
    }
}