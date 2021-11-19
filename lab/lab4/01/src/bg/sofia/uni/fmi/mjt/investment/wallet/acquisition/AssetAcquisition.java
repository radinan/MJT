package bg.sofia.uni.fmi.mjt.investment.wallet.acquisition;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;

import java.time.LocalDateTime;

public class AssetAcquisition implements Acquisition{
    private Asset asset;
    private int quantity;
    private LocalDateTime timestamp;

    public AssetAcquisition(Asset asset, int quantity) {
        this.asset = asset;
        this.quantity = quantity;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public double getPrice() {
        return 0;
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
