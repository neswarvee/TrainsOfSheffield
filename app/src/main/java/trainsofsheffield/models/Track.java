package trainsofsheffield.models;

import java.math.BigDecimal;

public class Track extends Product{
    private int trackID;

    public Track (int productID, String productCode, String productName, BigDecimal retailPrice,
                  int quantityInStock, String manufacturer, Gauge gauge,
                  int trackID) {
        super(productID, productCode, productName, retailPrice, quantityInStock, manufacturer, gauge);
        this.setTrackID(trackID);
    }

    public Track (int productID, String productName, BigDecimal retailPrice,
                  int quantityInStock, String manufacturer, Gauge gauge) {
        super(productID, productName, retailPrice, quantityInStock, manufacturer, gauge);
    }

    public Track (String productName, BigDecimal retailPrice,
                  int quantityInStock, String manufacturer, Gauge gauge) {
        super(productName, retailPrice, quantityInStock, manufacturer, gauge);
    }

    public int getTrackID() {
        return trackID;
    }

    public void setTrackID(int trackID) {
        if (trackID >= 0) {
            this.trackID = trackID;
        } else {
            throw new IllegalArgumentException("Track ID is not valid.");
        }
    }
}
