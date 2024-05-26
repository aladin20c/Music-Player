package playlist.com.data;

import java.time.LocalDate;


/**
 * Main account class, extends account. it can have several attached accounts attached to it
 * @version 1.0
 * @author alaeddinecheniour
 */
public class MainAccount extends Account{


    MainAccount(String pseudo, String mdp, LocalDate birthDay) {super(pseudo, mdp, birthDay);}

    public int attachAFamilyMember(String pseudo, String mdp, int year,int month,int day){
        int a = canSignUp( pseudo,  mdp,  year, month, day);
        if(a==0 || a == 4){
            super.addFamilyMember( new AttachedAccount(pseudo,mdp,LocalDate.of(year, month, day),this) );
            return 0;
        }
        return a;
    }


}
