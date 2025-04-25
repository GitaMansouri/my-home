package src.Phases.ManagingSmartDevices;
public class SmartThermostat extends SmartDevice {
    private int temperature;

    public SmartThermostat(String name, String protocol) {
        super(name, protocol);
        this.temperature = 20;
    }

    @Override
    protected void initializeProperties() {
        addProperty("status", String.class, "on", "off");
        addProperty("temperature", Integer.class, 10, 30);
    }

    @Override
    public DeviceType getType() {
        return DeviceType.THERMOSTAT;
    }

    @Override
    protected boolean setStringProperty(String property, String value) {
        if ("status".equals(property)) {
            setStatus(value);
            return true;
        }
        return false;
    }

    @Override
    protected boolean setNumericProperty(String property, int value) {
        if ("temperature".equals(property)) {
            temperature = value;
            return true;
        }
        return false;
    }

    @Override
    public String getDeviceInfo() {
        return String.format("Thermostat: %s | Status: %s | Temperature: %dC | Protocol: %s",
                getName(), getStatus(), temperature, getProtocol());
    }
}