package com.imby.server.business.web.webSocket;

import com.imby.server.inf.IServerMonitoringListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>
 * 与前端传输服务器信息的websocket
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/6/14 20:18
 */
@Component
@ServerEndpoint("/server-websocket")
@Slf4j
public class ServerWebSocket implements IServerMonitoringListener {

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 定义Websocket容器，储存Session
     */
    private static final CopyOnWriteArraySet<ServerWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * <p>
     * 建立连接
     * </p>
     *
     * @param session 与某个客户端的连接会话
     * @author 皮锋
     * @custom.date 2020/6/14 20:27
     */
    @OnOpen
    public void opOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("WebSocket连接成功，当前会话数为：{}", webSocketSet.size());
    }

    /**
     * <p>
     * 关闭连接
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/6/14 20:27
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("WebSocket退出成功，当前会话数为：{}", webSocketSet.size());
    }

    /**
     * <p>
     * 接收消息
     * </p>
     *
     * @param message 消息
     * @author 皮锋
     * @custom.date 2020/6/14 20:27
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("WebSocket收到消息：{}", message);
    }

    /**
     * <p>
     * 群发消息
     * </p>
     *
     * @param message 消息
     * @author 皮锋
     * @custom.date 2020/6/14 20:34
     */
    public void sendMessage(String message) {
        //遍历储存的Websocket
        for (ServerWebSocket webSocket : webSocketSet) {
            //发送
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("WebSocket群发消息异常！", e);
            }
        }
    }

    /**
     * <p>
     * 服务端收到了来自代理端或者客户端的新的服务器信息包，唤醒执行回调方法。
     * </p>
     *
     * @param param 回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    @Override
    public void wakeUp(Object... param) {
        this.sendMessage("");
    }
}
