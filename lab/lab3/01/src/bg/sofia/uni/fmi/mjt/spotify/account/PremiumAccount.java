package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class PremiumAccount extends Account{

    private String email;

    public PremiumAccount(String email, Library library) {
        this.email = email;
        this.library = library;
    }

    @Override
    public int getAdsListenedTo() {
        return 0;
    }

    @Override
    public AccountType getType() {
        return AccountType.PREMIUM;
    }

}
