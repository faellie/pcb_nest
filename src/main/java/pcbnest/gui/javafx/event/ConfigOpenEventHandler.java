package pcbnest.gui.javafx.event;

import javafx.event.EventHandler;

public abstract class ConfigOpenEventHandler implements EventHandler<ConfigOpenEvent> {

    public abstract void onEvent(String param0);

    @Override
    public void handle(ConfigOpenEvent event) {
        event.invokeHandler(this);
    }
}
