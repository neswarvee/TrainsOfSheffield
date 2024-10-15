package trainsofsheffield.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class TrainSet extends Product{
    private int trainSetID;
    private String era;
    private Controller controller;
    private HashMap<RollingStock, Integer> mapOfRollingStock;
    private HashMap<Locomotive, Integer> mapOfLocomotives;
    private HashMap<TrackPack, Integer> mapOfTrackPacks;

    public TrainSet (int productID, String productCode, String productName, BigDecimal retailPrice,
                     int quantityInStock, String manufacturer, Gauge gauge,
                     int trainSetID, String era, Controller controller,
                     HashMap<RollingStock, Integer> mapOfRollingStocksAndQuantities,
                     HashMap<Locomotive, Integer> mapOfLocomotivesAndQuantities,
                     HashMap<TrackPack, Integer> mapOfTrackPacksAndQuanities) {
        super(productID, productCode, productName, retailPrice, quantityInStock, manufacturer, gauge);
        this.setTrainSetID(trainSetID);
        this.setEra(era);
        this.setController(controller);
        this.setMapOfRollingStock(mapOfRollingStocksAndQuantities);
        this.setMapOfLocomotives(mapOfLocomotivesAndQuantities);
        this.setMapOfTrackPacks(mapOfTrackPacksAndQuanities);
    }

    public int getTrainSetID() {
        return trainSetID;
    }

    public void setTrainSetID(int trainSetID) {
        if (trainSetID >= 0) {
            this.trainSetID = trainSetID;
        } else {
            throw new IllegalArgumentException("Train Set ID is not valid");
        }
    }

    public String getEra() {
        return era;
    }

    public void setEra(String era) {
        if (!era.equals("") && !era.equals(" ")) {
            this.era = era;
        } else {
            throw new IllegalArgumentException("Era is not valid");
        }
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        if (productID >= 0) {
            this.productID = productID;
        } else {
            throw new IllegalArgumentException("Product ID is not valid");
        }
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public HashMap<RollingStock, Integer> getMapOfRollingStock() {
        return mapOfRollingStock;
    }

    public void setMapOfRollingStock(HashMap<RollingStock, Integer> mapOfRollingStocksAndQuantities) {
        if (mapOfRollingStock != null) {
            this.mapOfRollingStock = mapOfRollingStocksAndQuantities;
        } else {
            throw new IllegalArgumentException("List of Rolling Stock supplied is empty");
        }
    }

    public HashMap<Locomotive, Integer> getMapOfLocomotives() {
        return mapOfLocomotives;
    }

    public void setMapOfLocomotives(HashMap<Locomotive, Integer> mapOfLocomotivesAndQuantities) {
        if (mapOfLocomotives != null) {
            this.mapOfLocomotives = mapOfLocomotivesAndQuantities;
        } else {
            throw new IllegalArgumentException("List of Locomotives supplied is empty");
        }
    }

    public HashMap<TrackPack, Integer> getMapOfTrackPacks() {return mapOfTrackPacks;}

    public void setMapOfTrackPacks(HashMap<TrackPack, Integer> mapOfTrackPacks) {
        this.mapOfTrackPacks = mapOfTrackPacks;
    }
}
