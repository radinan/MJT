package bg.sofia.uni.fmi.mjt.spotify;

import bg.sofia.uni.fmi.mjt.spotify.account.Account;
import bg.sofia.uni.fmi.mjt.spotify.account.AccountType;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlayableNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.StreamingServiceException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class Spotify implements StreamingService{

    private final Account[] accounts;
    private final Playable[] playableContent;

    public Spotify(Account[] accounts, Playable[] playableContent) {
        this.accounts = accounts;
        this.playableContent = playableContent;
    }

    @Override
    public void play(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException {
        if (account == null || isTitleInvalid(title)) {
            throw new IllegalArgumentException();
        }

        if (!containsAccount(account)) {
            throw new AccountNotFoundException();
        }

        Playable content = getContent(title);
        if (content == null) {
            throw new PlayableNotFoundException();
        }
        content.play();
        account.listen(content);

    }

    @Override
    public void like(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException, StreamingServiceException {
        if (account == null || isTitleInvalid(title)) {
            throw new IllegalArgumentException();
        }

        if (!containsAccount(account)) {
            throw new AccountNotFoundException();
        }

        Playable content = getContent(title);
        if (content == null) {
            throw new PlayableNotFoundException();
        }

        try {
            account.getLibrary().getLiked().add(content);
        } catch (PlaylistCapacityExceededException e) {
            throw new StreamingServiceException();
        }

    }

    @Override
    public Playable findByTitle(String title) throws PlayableNotFoundException {
        if (isTitleInvalid(title)) {
            throw new IllegalArgumentException();
        }

        Playable content = getContent(title);
        if (content == null) {
            throw new PlayableNotFoundException();
        }
        return content;
    }

    @Override
    public Playable getMostPlayed() {
        int maxPlays = 0;
        Playable maxPlayable = null;

        for (Playable p : playableContent) {
            if (p.getTotalPlays() > maxPlays) {
                maxPlayable = p;
            }
        }

        return maxPlayable;
    }

    @Override
    public double getTotalListenTime() {
        double totalListenTime = 0;

        for (Account a : accounts) {
            totalListenTime += a.getTotalListenTime();
        }

        return totalListenTime;
    }

    @Override
    public double getTotalPlatformRevenue() {
        double totalRevenue = 0;
        for (Account a : accounts) {
            if (a.getType() == AccountType.FREE) {
                totalRevenue += (double)a.getAdsListenedTo() * 0.10;
            } else if (a.getType() == AccountType.PREMIUM) {
                totalRevenue += 25.00;
            }
        }

        return totalRevenue;
    }

    private boolean containsAccount (Account account) {
        for (Account a : accounts) {
            if (a.equals(account)) {
                return true;
            }
        }

        return false;
    }

    private Playable getContent(String title) {
        for (Playable p : playableContent) {
            if (p.getTitle().equals(title)) {
                return p;
            }
        }

        return null;
    }

    private boolean isTitleInvalid(String title) {
        return title == null || title.isEmpty();
    }

}
