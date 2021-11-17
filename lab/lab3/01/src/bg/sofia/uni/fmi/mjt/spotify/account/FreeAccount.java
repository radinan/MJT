package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class FreeAccount extends Account{

    String email;


    public FreeAccount(String email, Library library) {
        this.email = email;
        this.library = library;
    }

    @Override
    public int getAdsListenedTo() {
        return totalListenedContent / 5;
    }

    @Override
    public AccountType getType() {
        return AccountType.FREE;
    }
}
