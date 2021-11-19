package bg.sofia.uni.fmi.mjt.investment.wallet.quote;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;

import java.util.HashMap;
import java.util.Map;

public class QuoteClass implements QuoteService {

    private Map<Asset, Quote> assetToQuote;

    public QuoteClass() {
        assetToQuote = new HashMap<>();
    }


    @Override
    public Quote getQuote(Asset asset) {
        if (asset == null){
            throw new IllegalArgumentException();
        }
        return assetToQuote.get(asset);
    }
}
