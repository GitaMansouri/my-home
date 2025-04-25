package  src.Phases;

import src.Phases.AutomationRules.AutomationRule;
import src.Phases.AutomationRules.TimeBasedRule;
import src.Phases.ManagingSmartDevices.SmartDevice;
import src.Phases.ManagingSmartDevices.SmartLight;
import src.Phases.ManagingSmartDevices.SmartThermostat;

import java.util.*;

public class SmartHomeSystem {
    private final Map<String, SmartDevice> devices = new LinkedHashMap<>();
    private final List<AutomationRule> rules = new ArrayList<>();

    public String addDevice(SmartDevice.DeviceType type, String name, String protocol) {
        if (devices.containsKey(name)) {
            return "duplicate device name";
        }

        try {
            SmartDevice device;
            switch (type) {
                case LIGHT:
                    device = new SmartLight(name, protocol);
                    break;
                case THERMOSTAT:
                    device = new SmartThermostat(name, protocol);
                    break;
                default:
                    return "unsupported device type";
            }
            devices.put(name, device);
            return "device added successfully";
        } catch (IllegalArgumentException e) {
            return "invalid input: " + e.getMessage();
        }
    }

    public String setDeviceProperty(String deviceName, String property, String value) {
        if (!devices.containsKey(deviceName)) {
            return "device not found";
        }

        SmartDevice device = devices.get(deviceName);

        switch (device.getType()) {
            case LIGHT:
                if (!property.equals("status") && !property.equals("brightness")) {
                    return "invalid property";
                }
                break;

            case THERMOSTAT:
                if (!property.equals("status") && !property.equals("temperature")) {
                    return "invalid property";
                }
                break;

            default:
                return "invalid property";
        }

        if (property.equals("status")) {
            if (!value.equals("on") && !value.equals("off")) {
                return "invalid value";
            }
        }
        else if (property.equals("brightness")) {
            try {
                int brightness = Integer.parseInt(value);
                if (brightness < 0 || brightness > 100) {
                    return "invalid value";
                }
            } catch (NumberFormatException e) {
                return "invalid value";
            }
        }
        else if (property.equals("temperature")) {
            try {
                int temp = Integer.parseInt(value);
                if (temp < 10 || temp > 30) {
                    return "invalid value";
                }
            } catch (NumberFormatException e) {
                return "invalid value";
            }
        }

        if (property.equals("status")) {
            device.setStatus(value);
        }
        else if (property.equals("brightness") && device instanceof SmartLight) {
            ((SmartLight) device).setBrightness(Integer.parseInt(value));
        }
        else if (property.equals("temperature") && device instanceof SmartThermostat) {
            ((SmartThermostat) device).setTemperature(Integer.parseInt(value));
        }

        return "device updated successfully";
    }

    public String removeDevice(String name) {
        if (!devices.containsKey(name)) {
            return "device not found";
        }

        devices.remove(name);
        rules.removeIf(rule -> rule.getDeviceName().equals(name));
        return "device removed successfully";
    }

    public List<String> listDevices() {
        List<String> result = new ArrayList<>();
        if (devices.isEmpty()) {
            result.add("No devices found");
            return result;
        }

        devices.values().forEach(device -> {
            result.add(device.getDeviceInfo());
        });
        return result;
    }

    public String addRule(String deviceName, String time, String action) {
        if (!devices.containsKey(deviceName)) {
            return "device not found";
        }

        if (!action.equals("on") && !action.equals("off")) {
            return "invalid action";
        }

        if (!time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            return "invalid time format";
        }

        for (AutomationRule rule : rules) {
            if (rule.getDeviceName().equals(deviceName) && rule.getTriggerTime().equals(time)) {
                return "duplicate rule";
            }
        }

        rules.add(new TimeBasedRule(deviceName, time, action));
        return "rule added successfully";
    }

    public String checkRulesAtTime(String time) {
        if (!time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            return "invalid time format";
        }

        rules.stream()
                .filter(rule -> rule.getTriggerTime().equals(time))
                .forEach(rule -> {
                    if (devices.containsKey(rule.getDeviceName())) {
                        devices.get(rule.getDeviceName()).setProperty("status", rule.getAction());
                    }
                });

        return "rules checked";
    }

    public List<String> listRules() {
        List<String> result = new ArrayList<>();
        if (rules.isEmpty()) {
            result.add("No rules defined");
            return result;
        }

        rules.forEach(rule -> {
            result.add(String.format("Rule: %s at %s -> %s",
                    rule.getDeviceName(), rule.getTriggerTime(), rule.getAction()));
        });
        return result;
    }
}
