package playlist.com.data;

import java.time.LocalDate;


/**
 *  class that represents an attached account to a main account. Every pseudonym is unique, it extends the account class.
 * @version 1.0
 * @author alaeddinecheniour
 */
public class AttachedAccount extends Account{


    Account attachedTo;

    public AttachedAccount(String pseudo, String mdp, LocalDate birthDay, Account attachedTo) {
        super(pseudo, mdp, birthDay);
        this.attachedTo = attachedTo;
    }

    @Override
    public void addFamilyMember(Account account) {return;}

    public boolean attachedTo(Account acc) {
        return this.attachedTo.equals(acc);
    }


    @Override
    public void deleteAccount() {
        Data.deleteAccount(this);
        emptyAccount();
        if(this.attachedTo!=null) {
            this.attachedTo.removeFamilyMember(this);
        }
    }
}
