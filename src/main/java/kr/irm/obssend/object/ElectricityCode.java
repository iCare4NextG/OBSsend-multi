package kr.irm.obssend.object;

public enum ElectricityCode {
    SWITCH1("Switch 1", "I1001", "", "99IRM"),
    COUNTDOWN1("Countdown 1", "I1002", "", "99IRM"),
    ADDELE("Add Electricity", "I1003", "", "99IRM"),
    CURCURRENT("Current Current", "I1004", "mA", "99IRM"),
    CURPOWER("Current Power", "I1005", "W", "99IRM"),
    CURVOLTAGE("Current Voltage", "I1006", "V", "99IRM"),
    RELAYSTATUS("Relay Status", "I1007", "", "99IRM"),
    CYCLETIME("Cycle Time", "I1008", "", "99IRM"),
    RANDOMTIME("Random Time", "I1009", "", "99IRM"),
    DEVICEID("Device ID", "I1010", "", "99IRM"),
    POWERAMOUNT("Power Amount", "I1011", "kwH", "99IRM");

    private String display;
    private String code;
    private String unit;
    private String system;


    ElectricityCode(String display, String code, String unit, String system) {
        this.display = display;
        this.code = code;
        this.unit = unit;
        this.system = system;
    }

    public String getDisplay()
    {
        return display;
    }

    public void setDisplay(String display)
    {
        this.display = display;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getSystem()
    {
        return system;
    }

    public void setSystem(String system)
    {
        this.system = system;
    }

}
