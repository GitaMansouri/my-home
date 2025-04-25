package src.Phases.ManagingSmartDevices;

public class SmartLight extends SmartDevice {
    private int brightness;

    public SmartLight(String name, String protocol) {
        super(name, protocol);
        this.brightness = 50;
    }

    @Override
    protected void initializeProperties() {
        addProperty("status", String.class, "on", "off");
        addProperty("brightness", Integer.class, 0, 100);
    }

    @Override
    public DeviceType getType() {
        return DeviceType.LIGHT;
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
        if ("brightness".equals(property)) {
            brightness = value;
            return true;
        }
        return false;
    }

    @Override
    public String getDeviceInfo() {
        return String.format("%s %s %d%% %s",
                getName(), getStatus(), brightness, getProtocol());
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
}