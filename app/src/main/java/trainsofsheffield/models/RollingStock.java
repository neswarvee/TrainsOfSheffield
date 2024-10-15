package trainsofsheffield.models;

import java.math.BigDecimal;

public class RollingStock extends Product{
    private int rollingStockID;
    private BRStandardMark brStandardMark;
    private CompanyMark companyMark;
    private RollingStockType rollingStockType;
    private String era;

    public enum BRStandardMark {
        MARK_1, MARK_2, MARK_3, MARK_4;

        @Override
        public String toString() {
            return switch (this) {
                case MARK_1 -> "Mark 1";
                case MARK_2 -> "Mark 2";
                case MARK_3 -> "Mark 3";
                case MARK_4 -> "Mark 4";
            };
        }
    }

    public enum CompanyMark {
        LMS, LNER, GWR, SR;

        @Override
        public String toString() {
            return switch (this) {
                case SR -> "SR";
                case GWR -> "GWR";
                case LMS -> "LMS";
                case LNER -> "LNER";
            };
        }
    }

    public enum RollingStockType {
        CARRIAGE, WAGON;

        @Override
        public String toString() {
            return switch (this) {
                case WAGON -> "Wagon";
                case CARRIAGE -> "Carriage";
            };
        }
    }


    public RollingStock (int productID, String productCode, String productName, BigDecimal retailPrice,
                         int quantityInStock, String manufacturer, Gauge gauge,
                         int rollingStockID, BRStandardMark brStandardMark, CompanyMark companyMark,
                         RollingStockType rollingStockType, String era) {
        super(productID, productCode, productName, retailPrice, quantityInStock, manufacturer, gauge);
        this.setRollingStockID(rollingStockID);
        this.setBrStandardMark(brStandardMark);
        this.setCompanyMark(companyMark);
        this.setRollingStockType(rollingStockType);
        this.setEra(era);
    }

    public RollingStock(String productName, BigDecimal retailPrice,
                         int quantityInStock, String manufacturer, Gauge gauge,
                         BRStandardMark brStandardMark, CompanyMark companyMark,
                         RollingStockType rollingStockType, String era) {
        super(productName, retailPrice, quantityInStock, manufacturer, gauge);
        this.setBrStandardMark(brStandardMark);
        this.setCompanyMark(companyMark);
        this.setRollingStockType(rollingStockType);
        this.setEra(era);
    }

    public int getRollingStockID() {
        return rollingStockID;
    }

    public void setRollingStockID(int rollingStockID) {
        if (rollingStockID >= 0) {
            this.rollingStockID = rollingStockID;
        } else {
            throw new IllegalArgumentException("Rolling Stock ID is not valid.");
        }
    }

    public BRStandardMark getBrStandardMark () {
        return brStandardMark;
    }

    public void setBrStandardMark (BRStandardMark brStandardMark) {
        this.brStandardMark = brStandardMark;
    }

    public CompanyMark getCompanyMark() {
        return companyMark;
    }

    public void setCompanyMark(CompanyMark companyMark) {
        this.companyMark = companyMark;
    }

    public RollingStockType getRollingStockType() {
        return rollingStockType;
    }

    public void setRollingStockType(RollingStockType rollingStockType) {
        this.rollingStockType = rollingStockType;
    }

    public String getEra() {
        return era;
    }

    public void setEra(String era) {
        this.era = era;
    }
}
