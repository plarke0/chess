package websocket;

import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) {

    }

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) {

    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) {

    }
}
