package src;

import java.util.*;
import src.Phases.*;
import src.Phases.ManagingSmartDevices.SmartDevice;

import java.util.Scanner;

import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SmartHomeSystem system = new SmartHomeSystem();

        int q = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < q; i++) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0];

            try {
                String result = processCommand(system, command, parts);
                if (result != null) {
                    System.out.println(result);
                }
            } catch (Exception e) {
                System.out.println("invalid input");
            }
        }

        scanner.close();
    }

    private static String processCommand(SmartHomeSystem system, String command, String[] parts) {
        switch (command) {
            case "add_device":
                if (parts.length != 4) return "invalid input";
                String type = parts[1];
                String name = parts[2];
                String protocol = parts[3];

                SmartDevice.DeviceType deviceType;
                try {
                    deviceType = SmartDevice.DeviceType.valueOf(type.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return "invalid input";
                }

                return system.addDevice(deviceType, name, protocol);

            case "set_device":
                if (parts.length != 4) return "invalid input";
                return system.setDeviceProperty(parts[1], parts[2], parts[3]);

            case "remove_device":
                if (parts.length != 2) return "invalid input";
                return system.removeDevice(parts[1]);

            case "list_devices":
                List<String> devices = system.listDevices();
                if (devices.isEmpty()) {
                    System.out.println();
                } else {
                    devices.forEach(System.out::println);
                }
                return null;

            case "add_rule":
                if (parts.length != 4) return "invalid input";
                return system.addRule(parts[1], parts[2], parts[3]);

            case "check_rules":
                if (parts.length != 2) return "invalid input";
                return system.checkRulesAtTime(parts[1]);

            case "list_rules":
                List<String> rules = system.listRules();
                if (rules.isEmpty()) {
                    System.out.println();
                } else {
                    rules.forEach(System.out::println);
                }
                return null;

            default:
                return "invalid command";
        }
    }
}