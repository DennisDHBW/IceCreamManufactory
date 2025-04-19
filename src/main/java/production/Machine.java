package production;


import lombok.Data;

@Data
public abstract class Machine {
    protected final String id;
    protected final String model;
    protected final String manufacturer;
    protected double capacity;
    protected MachineStatus status;

    public Machine(String id, String model, String manufacturer) {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
    }


}
