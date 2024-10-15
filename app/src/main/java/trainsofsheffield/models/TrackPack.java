package trainsofsheffield.models;

import java.math.BigDecimal;
import java.util.HashMap;

public class TrackPack extends Product{
    private int trackPackID;
    private HashMap<Track, Integer> mapOfTracks;

    public TrackPack (int productID, String productCode, String productName, BigDecimal retailPrice,
                      int quantityInStock, String manufacturer, Gauge gauge,
                      int trackPackID, HashMap<Track, Integer> trackAndQuantityMap) {
        super(productID, productCode, productName, retailPrice, quantityInStock, manufacturer, gauge);
        this.setTrackPackID(trackPackID);
        this.setMapOfTracks(trackAndQuantityMap);
    }

    public TrackPack (String productName, BigDecimal retailPrice,
                      int quantityInStock, String manufacturer, Gauge gauge, 
                      HashMap<Track, Integer> trackAndQuantityMap) {
        super(productName, retailPrice, quantityInStock, manufacturer, gauge);
        this.setTrackPackID(trackPackID);
        this.setMapOfTracks(trackAndQuantityMap);
    }

    public int getTrackPackID() {
        return trackPackID;
    }

    public void setTrackPackID(int trackPackID) {
        if (trackPackID >= 0) {
            this.trackPackID = trackPackID;
        } else {
            throw new IllegalArgumentException("Track Pack ID is not valid");
        }
    }

    public HashMap<Track, Integer> getMapOfTracks() {
        return mapOfTracks;
    }

    public void setMapOfTracks(HashMap<Track, Integer> trackAndQuantityList) {
        if (trackAndQuantityList != null) {
            mapOfTracks = trackAndQuantityList;
        } else {
            throw new IllegalArgumentException("List of Tracks supplied is empty");
        }
    }


}
