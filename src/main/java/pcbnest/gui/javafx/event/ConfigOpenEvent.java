package pcbnest.gui.javafx.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ConfigOpenEvent extends Event {

    public static final EventType<ConfigOpenEvent> CUSTOM_EVENT_TYPE = new EventType(ANY, "CustomEvent");

    private final String param;

    public ConfigOpenEvent(String configFile) {
        super(CUSTOM_EVENT_TYPE);
        this.param = configFile;
    }

    public void invokeHandler(ConfigOpenEventHandler handler) {
        handler.onEvent(param);
    }

}
