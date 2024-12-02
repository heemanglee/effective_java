package example.effective_java.item03.bads.field;

public class Concert {

    private boolean lightsOn;
    private boolean mainStateOpen;

    private Elvis elvis;

    public Concert(Elvis elvis) {
        this.elvis = elvis;
    }

    // 클라이언트 코드
    public void perform() {
        lightsOn = true;
        mainStateOpen = true;
        elvis.sing();
    }

    public boolean isLightsOn() {
        return lightsOn;
    }

    public boolean isMainStateOpen() {
        return mainStateOpen;
    }
}
