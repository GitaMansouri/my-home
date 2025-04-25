package src.Phases.AutomationRules;

public class TimeBasedRule implements AutomationRule {
    private final String deviceName;
    private final String triggerTime;
    private final String action;

    public TimeBasedRule(String deviceName, String triggerTime, String action) {
        this.deviceName = deviceName;
        this.triggerTime = triggerTime;
        this.action = action;
    }

    @Override
    public String getDeviceName() { return deviceName; }

    @Override
    public String getTriggerTime() { return triggerTime; }

    @Override
    public String getAction() { return action; }
}
