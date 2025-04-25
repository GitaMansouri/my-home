package src.Phases.ManagingSmartDevices;

import java.util.*;

public abstract class SmartDevice {
    private final String name;
    private final String protocol;
    private String status = "off";
    private final Map<String, PropertyMetadata> properties = new HashMap<>();

    protected SmartDevice(String name, String protocol) {
        validateName(name);
        validateProtocol(protocol);
        this.name = name;
        this.protocol = protocol;
        initializeProperties();
    }

    public enum DeviceType {
        LIGHT, THERMOSTAT, LOCK, CURTAIN, SENSOR
    }

    public static class PropertyMetadata {
        private final String name;
        private final Class<?> type;
        private final Comparable<?> min;
        private final Comparable<?> max;

        public PropertyMetadata(String name, Class<?> type, Comparable<?> min, Comparable<?> max) {
            this.name = name;
            this.type = type;
            this.min = min;
            this.max = max;
        }

        public String getName() { return name; }
        public Class<?> getType() { return type; }
        public Comparable<?> getMin() { return min; }
        public Comparable<?> getMax() { return max; }
    }

    private void validateName(String name) {
        if (name == null || !name.matches("[a-zA-Z0-9_]{1,20}")) {
            throw new IllegalArgumentException("Invalid device name");
        }
    }

    private void validateProtocol(String protocol) {
        if (!Set.of("WiFi", "Bluetooth", "Zigbee").contains(protocol)) {
            throw new IllegalArgumentException("Invalid protocol");
        }
    }

    protected abstract void initializeProperties();

    public String getName() { return name; }

    public String getProtocol() { return protocol; }

    public String getStatus() { return status; }

    public void setStatus(String status) {
        if ("on".equals(status) || "off".equals(status)) {
            this.status = status;
        }
    }

    public abstract DeviceType getType();

    public Map<String, PropertyMetadata> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public boolean setProperty(String property, String value) {
        PropertyMetadata meta = properties.get(property);
        if (meta == null) return false;

        try {
            if (meta.getType() == String.class) {
                if (!isValidStringValue(meta, value)) return false;
                return setStringProperty(property, value);
            } else if (meta.getType() == Integer.class) {
                int intValue = Integer.parseInt(value);
                if (!isValidRange(meta, intValue)) return false;
                return setNumericProperty(property, intValue);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    protected void addProperty(String name, Class<?> type, Comparable<?> min, Comparable<?> max) {
        properties.put(name, new PropertyMetadata(name, type, min, max));
    }

    protected abstract boolean setStringProperty(String property, String value);

    protected abstract boolean setNumericProperty(String property, int value);

    private boolean isValidStringValue(PropertyMetadata meta, String value) {
        Set<String> validValues = Set.of(meta.getMin().toString(), meta.getMax().toString());
        return validValues.contains(value);
    }

    private boolean isValidRange(PropertyMetadata meta, int value) {
        Comparable<Integer> min = (Comparable<Integer>) meta.getMin();
        Comparable<Integer> max = (Comparable<Integer>) meta.getMax();
        return value >= min.compareTo(value) && value <= max.compareTo(value);
    }

    public abstract String getDeviceInfo();
}