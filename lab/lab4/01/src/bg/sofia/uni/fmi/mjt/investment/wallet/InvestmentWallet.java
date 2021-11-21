package bg.sofia.uni.fmi.mjt.investment.wallet;

import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.Acquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.AssetAcquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.InsufficientResourcesException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.OfferPriceException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.UnknownAssetException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.WalletException;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteService;

import java.util.*;

public class InvestmentWallet implements Wallet {

    private double cashBalance;
    private QuoteService quoteService;
    private List<Acquisition> acquisitions;
    private Map<Asset, Double> assetsToCount;

    public InvestmentWallet(QuoteService quoteService) {
        this.quoteService = quoteService;

        cashBalance = 0;
        acquisitions = new ArrayList<>();
        assetsToCount = new HashMap<>();
    }

    @Override
    public double deposit(double cash) {
        if (cash < 0) {
            throw new IllegalArgumentException();
        }

        return cashBalance += cash;
    }

    @Override
    public double withdraw(double cash) throws InsufficientResourcesException {
        if (cash < 0) {
            throw new IllegalArgumentException();
        }

        if (cash > cashBalance) {
            throw new InsufficientResourcesException();
        }

        return cashBalance -= cash; //check
    }

    @Override
    public Acquisition buy(Asset asset, int quantity, double maxPrice) throws WalletException {
        if (quantity < 0 || maxPrice < 0 || asset == null) {
            throw new IllegalArgumentException();
        }

        if (quoteService.getQuote(asset) == null) {
            throw new UnknownAssetException();
        }

        double askPrice = quoteService.getQuote(asset).askPrice();

        if (askPrice > maxPrice) {
            throw new OfferPriceException();
        }

        if (cashBalance < askPrice * quantity) {
            throw new InsufficientResourcesException();
        }

        double prisePerUnit = Math.min(askPrice, maxPrice);

        Double assetsCount = assetsToCount.getOrDefault(asset, 0.0);
        assetsToCount.put(asset, assetsCount + quantity);

        Acquisition acquisition = new AssetAcquisition(asset, quantity, prisePerUnit);
        acquisitions.add(acquisition);

        cashBalance -= prisePerUnit * maxPrice;

        return acquisition;
    }

    @Override
    public double sell(Asset asset, int quantity, double minPrice) throws WalletException {
        if (quantity < 0 || minPrice < 0 || asset == null) {
            throw new IllegalArgumentException();
        }

        if (assetsToCount.get(asset) == null || assetsToCount.get(asset) < quantity) {
            throw new InsufficientResourcesException();
        }

        if (quoteService.getQuote(asset) == null) {
            throw new UnknownAssetException();
        }

        double bidPrice = quoteService.getQuote(asset).bidPrice();

        if (bidPrice < minPrice) {
            throw new OfferPriceException();
        }

        Double assetsCount = assetsToCount.get(asset);
        assetsToCount.put(asset, assetsCount - quantity);

        double profit = Math.max(bidPrice, minPrice) * quantity;
        cashBalance += profit;

        return profit;
    }

    @Override
    public double getValuation() {
        double sum = 0;

        for (Asset a : assetsToCount.keySet()) {
            sum += assetsToCount.get(a) * quoteService.getQuote(a).bidPrice();
        }

        return sum;
    }

    @Override
    public double getValuation(Asset asset) throws UnknownAssetException {
        if (asset == null) {
            throw new IllegalArgumentException();
        }

        if (!assetsToCount.containsKey(asset) ||
                assetsToCount.get(asset) == 0 ||
                quoteService.getQuote(asset) == null) {
            throw new UnknownAssetException();
        }

        return assetsToCount.get(asset) * quoteService.getQuote(asset).bidPrice();
    }

    @Override
    public Asset getMostValuableAsset() {
        double maxValue = -1;
        Asset maxAsset = null;

        for (Asset a : assetsToCount.keySet()) {
            double valuation = assetsToCount.get(a) * quoteService.getQuote(a).bidPrice();

            if (valuation > maxValue) {
                maxValue = valuation;
                maxAsset = a;
            }
        }

        return maxAsset;
    }

    @Override
    public Collection<Acquisition> getAllAcquisitions() {
        return List.copyOf(acquisitions);
    }

    @Override
    public Set<Acquisition> getLastNAcquisitions(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        if (n == 0) {
            List<Acquisition> emptyList = new ArrayList<>();
            return Set.copyOf(emptyList);
        }

        if (n >= acquisitions.size()) {
            return Set.copyOf(acquisitions);
        }

        int size = acquisitions.size();

        List<Acquisition> l = acquisitions.subList(size - n, size);
        return Set.copyOf(l);
    }
}
