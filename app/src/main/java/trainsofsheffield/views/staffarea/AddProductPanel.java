package trainsofsheffield.views.staffarea;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import trainsofsheffield.models.Controller.ControllerType;
import trainsofsheffield.models.Locomotive.DccCode;
import trainsofsheffield.models.Product.EraCode;
import trainsofsheffield.models.Product.Gauge;
import trainsofsheffield.models.Product.ProductCategory;
import trainsofsheffield.models.RollingStock.BRStandardMark;
import trainsofsheffield.models.RollingStock.CompanyMark;
import trainsofsheffield.models.RollingStock.RollingStockType;

public class AddProductPanel extends JPanel {
    private JTextField productNameField;
    private JTextField retailPriceField;
    private JTextField quantityInStockField;
    private JTextField manufacturerField;
    private JComboBox<Gauge> gaugeComboBox;
    private JComboBox<CompanyMark> companyMarkComboBox;
    private JComboBox<RollingStockType> rollingStockTypeComboBox;
    private JComboBox<BRStandardMark> bRStandardMarkComboBox;
    private JComboBox<DccCode> dccComboBox;
    private JComboBox<EraCode> eraComboBox;
    private JComboBox<Boolean> isDigitalComboBox;
    private JComboBox<ControllerType> controllerTypeComboBox;

    public AddProductPanel(ProductCategory category) {

        // productName
        add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        add(productNameField);

        // retailPrice
        add(new JLabel("Retail Price:"));
        retailPriceField = new JTextField();
        add(retailPriceField);

        // quantityInStock
        add(new JLabel("Quantity in Stock:"));
        quantityInStockField = new JTextField();
        add(quantityInStockField);

        // manufacturer
        add(new JLabel("Manufacturer:"));
        manufacturerField = new JTextField();
        add(manufacturerField);

        // gauge
        add(new JLabel("Gauge:"));
        gaugeComboBox = new JComboBox<>(Gauge.values());
        add(gaugeComboBox);

        switch (category) {
            case LOCOMOTIVES:
                setLayout(new GridLayout(8, 2));

                // DCC code
                add(new JLabel("DDC Code:"));
                dccComboBox = new JComboBox<>(DccCode.values());
                add(dccComboBox);

                // Era code
                add(new JLabel("Era Code:"));
                eraComboBox = new JComboBox<>(EraCode.values());
                add(eraComboBox);
                break;
                
            case ROLLING_STOCKS:
                setLayout(new GridLayout(10, 2));

                // BR Standard Mark
                add(new JLabel("BR Standard Mark:"));
                bRStandardMarkComboBox = new JComboBox<>(BRStandardMark.values());
                add(bRStandardMarkComboBox);

                // Company Mark
                add(new JLabel("Company Mark:"));
                companyMarkComboBox = new JComboBox<>(CompanyMark.values());
                add(companyMarkComboBox);

                // Rolling Stock Type
                add(new JLabel("Rolling Stock Type:"));
                rollingStockTypeComboBox = new JComboBox<>(RollingStockType.values());
                add(rollingStockTypeComboBox);

                // Era code
                add(new JLabel("Era Code:"));
                eraComboBox = new JComboBox<>(EraCode.values());
                add(eraComboBox);
                break;

            case CONTROLLERS:
                setLayout(new GridLayout(8, 2));

                // Is digital
                add(new JLabel("'True' if the controller digital, 'False' otherwise: "));
                Boolean[] values = {true, false};
                isDigitalComboBox = new JComboBox<>(values);
                add(isDigitalComboBox);

                // Controller type
                add(new JLabel("Controller Type: "));
                controllerTypeComboBox = new JComboBox<>();
                add(controllerTypeComboBox);

                // Update controllerTypeComboBox when isDigitalComboBox changes
                isDigitalComboBox.addItemListener(e -> {
                    controllerTypeComboBox.removeAllItems();
                    if ((Boolean) isDigitalComboBox.getSelectedItem()) {
                        // If digital, add DCC and DCC_ELITE
                        controllerTypeComboBox.addItem(ControllerType.DCC);
                        controllerTypeComboBox.addItem(ControllerType.DCC_ELITE);
                    } else {
                        // If not digital, add STANDARD
                        controllerTypeComboBox.addItem(ControllerType.STANDARD);
                    }
                });

                // Trigger the item listener to populate controllerTypeComboBox initially
                isDigitalComboBox.setSelectedIndex(0);
                break;

            default: 
                setLayout(new GridLayout(5, 2));
                break;
        }
    }

    public String getProductName() {
        return productNameField.getText();
    }
    
    public String getRetailPrice() {
        return retailPriceField.getText();
    }
    
    public String getQuantityInStock() {
        return quantityInStockField.getText();
    }
    
    public String getManufacturer() {
        return manufacturerField.getText();
    }
    
    public Gauge getGauge() {
        return (Gauge) gaugeComboBox.getSelectedItem();
    }
    
    public CompanyMark getCompanyMark() {
        return (CompanyMark) companyMarkComboBox.getSelectedItem();
    }
    
    public RollingStockType getRollingStockType() {
        return (RollingStockType) rollingStockTypeComboBox.getSelectedItem();
    }
    
    public BRStandardMark getBRStandardMark() {
        return (BRStandardMark) bRStandardMarkComboBox.getSelectedItem();
    }
    
    public DccCode getDccCode() {
        return (DccCode) dccComboBox.getSelectedItem();
    }
    
    public EraCode getEraCode() {
        return (EraCode) eraComboBox.getSelectedItem();
    }
    
    public Boolean getIsDigital() {
        return (Boolean) isDigitalComboBox.getSelectedItem();
    } 
    
    public ControllerType getControllerType() {
        return (ControllerType) controllerTypeComboBox.getSelectedItem();
    }
}
