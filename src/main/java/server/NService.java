package server;

import shared.NEvenement;
import shared.NPersonne;

import java.util.List;

/**
 * Created by naleite on 14/12/11.
 */
public interface NService {

    public List<NEvenement> allEvenement();

     // public List<NEvenement> evenementsOnePerson(String nom);
    public List<NPersonne> allPersonne() ;

    public List<NEvenement> evenementsOnePerson(long id);

    public void ajoutePersonne(String nom);

    public String createEve(long id,
                            String depart,
                            String dest);


    public String deleteEvById(long id);
}
