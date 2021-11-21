package bg.sofia.uni.fmi.mjt.investment.wallet.acquisition;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteService;

import java.time.LocalDateTime;

public class AssetAcquisition implements Acquisition {
    private Asset asset;
    private int quantity;
    private LocalDateTime timestamp;
    private double price;

    public AssetAcquisition(Asset asset, int quantity, double price) {
        this.asset = asset;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public Asset getAsset() {
        return asset;
    }

}
