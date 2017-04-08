package own.jadezhang.learning.apple.controller.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by Zhang Junwei on 2017/4/7 0007.
 */
public class WebsocketMessageHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketMessageHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("the webSocketSession is open. " , session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.debug("the webSocketSession receive a message. " , session , message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.debug("the webSocketSession has error. " , session ,exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.debug("the webSocketSession is closed. " , session);
    }

}
