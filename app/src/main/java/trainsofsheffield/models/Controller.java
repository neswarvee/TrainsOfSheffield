package trainsofsheffield.models;

import java.math.BigDecimal;

public class Controller extends Product{
    private int controllerID;
    private boolean isDigital;
    private ControllerType controllerType;
    public enum ControllerType {
        STANDARD, DCC, DCC_ELITE;

        @Override
        public String toString() {
            return switch (this) {
                case STANDARD -> "Standard Controller";
                case DCC -> "DCC Controller";
                case DCC_ELITE -> "DCC Elite Controller";
            };
        }
    }

    public Controller(int productID, String productCode, String productName, BigDecimal retailPrice,
                      int quantityInStock, String manufacturer, Gauge gauge,
                      int controllerID, boolean isDigital, ControllerType controllerType) {
        super(productID, productCode, productName, retailPrice, quantityInStock, manufacturer, gauge);
        this.setControllerID(controllerID);
        this.setDigital(isDigital);
        this.setProductID(productID);
        this.setControllerType(controllerType);
    }

    public Controller(String productName, BigDecimal retailPrice,
                      int quantityInStock, String manufacturer, Gauge gauge,
                      boolean isDigital, ControllerType controllerType) {
        super(productName, retailPrice, quantityInStock, manufacturer, gauge);
        this.setDigital(isDigital);
        this.setControllerType(controllerType);
    }

    public int getControllerID() {
        return controllerID;
    }

    public void setControllerID(int controllerID) {
        if (isValidControllerID(controllerID)) {
            this.controllerID = controllerID;
        } else {
            throw new IllegalArgumentException("Controller ID is not valid.");
        }
    }

    public boolean getDigital() {
        return isDigital;
    }

    public void setDigital(boolean isDigital) {
        this.isDigital = isDigital;
    }

    public ControllerType getControllerType() {
        return controllerType;
    }

    public void setControllerType(ControllerType controllerType) {
        this.controllerType = controllerType;
    }

    private boolean isValidControllerID(int controllerID) {
        return controllerID >= 0;
    }
}
