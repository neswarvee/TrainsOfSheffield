package trainsofsheffield.models;

import java.math.BigDecimal;

public class Locomotive extends Product{
    private int locomotiveID;
    private DccCode dccCode;
    private String era;
    public enum DccCode {
        ANALOGUE, DCC_READY, DCC_FITTED, DCC_SOUND;

        @Override
        public String toString() {
            return switch (this) {
                case ANALOGUE -> "Analogue";
                case DCC_READY -> "DCC-Ready";
                case DCC_SOUND -> "DCC-Sound";
                case DCC_FITTED -> "DCC-Fitted";
            };
        }
    }

    public Locomotive(int productID, String productCode, String productName, BigDecimal retailPrice,
                      int quantityInStock, String manufacturer, Gauge gauge,
                      int locomotiveID, DccCode dccCode, String era) {
        super(productID, productCode, productName, retailPrice, quantityInStock, manufacturer, gauge);
        this.setLocomotiveID(locomotiveID);
        this.setDccCode(dccCode);
        this.setEra(era);
    }

    public Locomotive(String productName, BigDecimal retailPrice,
                      int quantityInStock, String manufacturer, Gauge gauge, 
                      DccCode dccCode, String era) {
        super(productName, retailPrice, quantityInStock, manufacturer, gauge);
        this.setDccCode(dccCode);
        this.setEra(era);
    }

    public int getLocomotiveID() {
        return locomotiveID;
    }

    public void setLocomotiveID(int locomotiveID) {
        if (locomotiveID >= 0) {
            this.locomotiveID = locomotiveID;
        } else {
            throw new IllegalArgumentException("Locomotive ID is not valid.");
        }
    }

    public DccCode getDccCode() {
        return dccCode;
    }

    public void setDccCode(DccCode dccCode) {
        this.dccCode = dccCode;
    }

    public String getEra() {
        return era;
    }

    public void setEra(String era) {
        this.era = era;
    }
}
