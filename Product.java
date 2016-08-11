package trade;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty pname;
    private final SimpleDoubleProperty price;
    private final SimpleIntegerProperty units;
    private final SimpleDoubleProperty amount;
    private final SimpleStringProperty fname;

    public Product(int id, String pname, Double price, int units, double amount, String fname) {
        this.id = new SimpleIntegerProperty(id);
        this.pname = new SimpleStringProperty(pname);
        this.price = new SimpleDoubleProperty(price);
        this.units = new SimpleIntegerProperty(units);
        this.amount = new SimpleDoubleProperty(amount);
        this.fname = new SimpleStringProperty(fname);
    }

//    getter methods
    public int getId() {
        return id.get();
    }

    public String getPname() {
        return pname.get();
    }

    public Double getPrice() {
        return price.get();
    }

    public int getUnits() {
        return units.get();
    }
    
    public Double getAmount() {
        this.amount.bind(this.price.multiply(this.units));
        return amount.get();
    }
    
    public String getFname() {
        return fname.get();
    }
    
//    Setter methods
    public void setId(int id) {
        this.id.set(id);
    }

    public void setPname(String pname) {
        this.pname.set(pname);
    }

    public void setPrice(double pPrice) {
        this.price.set(pPrice);
    }
    
    public void setUnits(int units) {
        this.units.set(units);
    }
    
    public void setAmount(double amount) {
        this.amount.set(amount);
    }
    
    public void setFname(String fname) {
        this.fname.set(fname);
    }
    
//    properties
    public SimpleIntegerProperty idProperty(){
        return id;
    }
    
    public SimpleStringProperty pnameProperty(){
       return pname;
    }
    
    public SimpleDoubleProperty priceProperty(){
        return price;
    }
    
    public SimpleIntegerProperty unitsProperty(){
        return units;
    }
    
    public SimpleDoubleProperty amountProperty(){
        return amount;
    }
    
    public SimpleStringProperty fnameProperty(){
       return fname;
    }
}
