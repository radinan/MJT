package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Fiat implements Asset {

    private String id;
    private String name;

    public Fiat(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public AssetType getType() {
        return AssetType.FIAT;
    }
}
