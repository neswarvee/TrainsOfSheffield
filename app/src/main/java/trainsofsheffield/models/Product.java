package trainsofsheffield.models;

import java.math.BigDecimal;

public abstract class Product {
    protected int productID;
    protected String productCode;
    protected String productName;
    protected BigDecimal retailPrice;
    protected int quantityInStock;
    protected String manufacturer;
    protected Gauge gauge;
    protected ProductCategory productCategory;

    private final static String[] ProductCategoriesList =
        {
                "Train Sets", "Track Packs", "Locomotives",
                "Rolling Stocks", "Tracks", "Controllers"
        };

    public enum Gauge {
        OO_GAUGE,
        TT_GAUGE,
        N_GAUGE;

        public String toString() {
            return switch (this) {
                case OO_GAUGE -> "OO Gauge";
                case TT_GAUGE -> "TT Gauge";
                case N_GAUGE -> "N Gauge";
            };
        }
    }

    public enum ProductCategory {
        TRAIN_SETS,
        TRACK_PACKS,
        LOCOMOTIVES,
        ROLLING_STOCKS,
        TRACKS,
        CONTROLLERS;

        public String toString() {
            return switch (this) {
                case TRAIN_SETS -> "TrainSets";
                case TRACK_PACKS -> "TrackPacks";
                case LOCOMOTIVES -> "Locomotives";
                case ROLLING_STOCKS -> "RollingStocks";
                case TRACKS -> "Tracks";
                case CONTROLLERS -> "Controllers";
            };
        }

        static public ProductCategory stringToEnum(String string) {
            return switch (string) {
                case "Train Sets", "TrainSets" -> TRAIN_SETS;
                case "Track Packs", "TrackPacks" -> TRACK_PACKS;
                case "Rolling Stocks", "RollingStocks" -> ROLLING_STOCKS;
                case "Locomotives" -> LOCOMOTIVES;
                case "Tracks" -> TRACKS;
                case "Controllers" -> CONTROLLERS;

                default -> throw new IllegalArgumentException("Can't convert this string to ProductCategory Enum");
            };
        }
    }

    public enum EraCode {
        ERA_1("Era 1"),
        ERA_1_2("Era 1-2"),
        ERA_2("Era 2"),
        ERA_2_3("Era 2-3"),
        ERA_3("Era 3"),
        ERA_3_4("Era 3-4"),
        ERA_4("Era 4"),
        ERA_4_5("Era 4-5"),
        ERA_5("Era 5"),
        ERA_5_6("Era 5-6"),
        ERA_6("Era 6"),
        ERA_6_7("Era 6-7"),
        ERA_7("Era 7"),
        ERA_7_8("Era 7-8"),
        ERA_8("Era 8"),
        ERA_8_9("Era 8-9"),
        ERA_9("Era 9"),
        ERA_9_10("Era 9-10"),
        ERA_10("Era 10"),
        ERA_10_11("Era 10-11"),
        ERA_11("Era 11");
    
        private final String description;
    
        EraCode(String description) {
            this.description = description;
        }
    
        @Override
        public String toString() {
            return description;
        }
    }

    public Product (int productID, String productCode, String productName, BigDecimal retailPrice,
                    int quantityInStock, String manufacturer, Gauge gauge) {
        this.productID = productID;
        this.productCode = productCode;
        this.productName = productName;
        this.retailPrice = retailPrice;
        this.quantityInStock = quantityInStock;
        this.manufacturer = manufacturer;
        this.gauge = gauge;
    }

    public Product (int productID, String productName, BigDecimal retailPrice,
                    int quantityInStock, String manufacturer, Gauge gauge) {
        this.productID = productID;
        this.productName = productName;
        this.retailPrice = retailPrice;
        this.quantityInStock = quantityInStock;
        this.manufacturer = manufacturer;
        this.gauge = gauge;
    }
    
    public Product (String productName, BigDecimal retailPrice,
                    int quantityInStock, String manufacturer, Gauge gauge) {
        this.productName = productName;
        this.retailPrice = retailPrice;
        this.quantityInStock = quantityInStock;
        this.manufacturer = manufacturer;
        this.gauge = gauge;
    }

    public static String[] getProductCategoriesList() {
        return ProductCategoriesList;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        if (productID > 0) {
            this.productID = productID;
        } else {
            throw new IllegalArgumentException("Product ID provided is not valid.");
        }
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        if (productCodeIsValid(productCode)){
            this.productCode = productCode;
        } else {
            throw new IllegalArgumentException("Product Code is invalid");
        }
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (!productName.equals("") && !productName.equals(" ")) {
            this.productName = productName;
        } else {
            throw new IllegalArgumentException("The product name is invalid");
        }
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        if (retailPriceIsValid(retailPrice)) {
            this.retailPrice = retailPrice;
        } else {
            throw new IllegalArgumentException("The retail price is invalid");
        }
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        if (quantityInStock >= 0) {
            this.quantityInStock = quantityInStock;
        } else {
            throw new IllegalArgumentException("The \"QuantityInStock\" is invalid");
        }
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        if (!manufacturer.equals("") && !manufacturer.equals(" ")) {
            this.manufacturer = manufacturer;
        } else {
            throw new IllegalArgumentException("The manufacturer name is invalid");
        }
    }

    public Gauge getGauge() {
        return gauge;
    }

    public void setGauge(Gauge gauge) {
        this.gauge = gauge;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public boolean productCodeIsValid(String productCode) {
        boolean isValid = false;

        if (productCode.length() >= 4 && productCode.length() <= 6) {
            char firstLetter = productCode.charAt(0);
            if (firstLetter == 'R' || firstLetter == 'C' || firstLetter == 'L'
                    || firstLetter == 'S' || firstLetter == 'M' || firstLetter == 'P') {
                String substring = productCode.substring(1, productCode.length()-1);
                try {
                    //check if the substring is an integer by attempting to convert to one
                    //if no exception is caught, then the substring is an integer value, and it's therefore validated
                    int intCheck = Integer.parseInt(substring);
                    isValid = true;
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Invalid product code - not all characters after the first are integers");
                }
            } else {
                throw new IllegalArgumentException("Invalid product code - first character not an allowed one");
            }
        } else {
            throw new IllegalArgumentException("Invalid product code - code not between 4 and 6 characters long");
        }

        return isValid;
    }

    public boolean retailPriceIsValid(BigDecimal retailPrice) {
        return retailPrice != null && retailPrice.compareTo(BigDecimal.ZERO) >= 0;
    }
}
