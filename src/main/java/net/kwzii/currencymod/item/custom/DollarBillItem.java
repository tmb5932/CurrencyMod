package net.kwzii.currencymod.item.custom;

/**
 * Class for Dollar Bill type custom items
 * @author Travis Brown
 */
public class DollarBillItem extends FuelItem {
    private final int value;

    /**
     * Constructor for DollarBillItem class
     * @param pProperties the properties of the bill
     * @param value the value of the bill
     */
    public DollarBillItem(Properties pProperties, int value) {
        super(pProperties, 400);
        this.value = value;
    }

    /**
     * Getter for the value of the bill
     * @return value
     */
    public int getValue() {
        return value;
    }
}
