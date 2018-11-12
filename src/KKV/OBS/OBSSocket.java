package KKV.OBS;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.Future;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.HttpClient;

/**
 * 客户端类
 *
 * @author Administrator
 */
@WebSocket(maxTextMessageSize = 64 * 2048)
public class OBSSocket {
  //private staic OBSSocket as = new OBSSocket();

  private WebSocketClient client = new WebSocketClient();
  private Session session;

  public String host;
  public int port;
  public ApiCmd api;

  public OBSSocket(String host, String port) {
    this.port = 0;
    try{
      this.port = Integer.parseInt(port);
    }catch(Exception e){}  
    this.host = host;
    this.api = this.getApi(host, this.port);
  }

  public void disconnect() {
    try {
      //api.StopRecording();
      client.stop();
    } catch (Exception e) {
    }
  }

  public static void main(String[] args) throws Exception {
    //obsSocket.api.StartRecording();    

    for (int i = 0; i < 10000; i++) {
      //obsSocket.api.SetCurrentScene("camera", null);    
      try{
      OBSSocket obsSocket = new OBSSocket("localhost", "4455");    
      obsSocket.api.SetCurrentScene("dwr", null);
      obsSocket.disconnect();
      }catch(Exception e){}
      try {
        Thread.sleep(2000);
      } catch (Exception e) {
      }
      try{
      OBSSocket obsSocket = new OBSSocket("localhost", "4455");    
      obsSocket.api.SetCurrentScene("camera", null);
      obsSocket.disconnect();
      }catch(Exception e){}
      try {
        Thread.sleep(2000);
      } catch (Exception e) {
      }      
    }

    //obsSocket.api.StopRecording();
    //obsSocket.disconnect();
  }

  public ApiCmd getApi(String host, int port) {
    try {
      client.start();
      ClientUpgradeRequest cuq = new ClientUpgradeRequest();
      Future<Session> future = client.connect(this, new URI("ws://" + host + ":" + port), cuq);
      session = future.get();
      return new ApiCmd(session);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return null;
  }

  @OnWebSocketConnect
  public void onConnect(Session session) {
    InetSocketAddress isa = session.getLocalAddress();
    System.out.printf("Connect : %s:%d%n", isa.getAddress(), isa.getPort());
  }

  @OnWebSocketClose
  public void onClose(Session session, int code, String msg) {
    InetSocketAddress isa = session.getLocalAddress();
    System.out.printf("Close %s:%s,  code=%d,cmd=%s %n\n", isa.getAddress(), isa.getPort(), code, msg);
  }

  @OnWebSocketError
  public void OnError(Session session, Throwable throwable) {
    if (session != null) {
      InetSocketAddress isa = session.getLocalAddress();
      System.out.printf("Error %s:%s,  error=%s %n", isa.getAddress(), isa.getPort(), throwable.getMessage());
    } else {
      System.out.println("OBS connection is error");

    }
  }

  public void onMessage(String... msg) {
    System.out.println("Message : " + Arrays.toString(msg));

  }

  public boolean isBlank(String txt) {
    if (txt == null) {
      return true;
    }
    if (txt.trim().equals("")) {
      return true;
    }
    return false;
  }

  public boolean isNotBlank(String txt) {
    return !isBlank(txt);
  }

  @OnWebSocketMessage
  public void onMessage(String msg) {
    if (isBlank(msg)) {
      return;
    }
    JSONObject json = JSON.parseObject(msg);
    String updateType = json.getString("update-type");
    if (isNotBlank(updateType) && null != json) {
      switch (updateType) {
        case "SwitchScenes":
          onSceneSwitch(json.getString("scene-name"));
          return;
        case "ScenesChanged":
          try {
            ApiCmd api = new ApiCmd(session);
            api.GetSceneList(new Function() {
              @Override
              public void call(JSONObject data) {
                OBSSceneCollection obs_scene_coll = JSON.parseObject(data.toJSONString(), OBSSceneCollection.class);
                for (OBSScene obs_scene : obs_scene_coll.getScenes()) {
                  onScenesChanged(obs_scene);
                }
              }
            });
          } catch (Exception e) {
            System.out.println(e.getMessage());
          }
          return;
        case "TransitionListChanged":
          onTransitionListChanged();
          return;
        case "SwitchTransition":
          onSwitchTransition(json.getString("transition-name"));
          return;
        case "StreamStarting":
          onStreamStarting(json.getString("preview-only"));
          return;
        case "StreamStarted":
          onStreamStarted();
          return;
        case "StreamStopping":
          onStreamStopping(json.getString("preview-only"));
          return;
        case "StreamStopped":
          onStreamStopped();
          return;
        case "RecordingStarting":
          onRecordingStarting();
          return;
        case "RecordingStarted":
          onRecordingStarted();
          return;
        case "RecordingStopping":
          onRecordingStopping();
          return;
        case "RecordingStopped":
          onRecordingStopped();
          return;
        case "StreamStatus":
          onStreamStatus(json);
          return;
        case "Exiting":
          onExit();
          return;
        default:
          System.err.printf("Unknown UpdateType: %s %s ", updateType, json.toJSONString());
      }
    }

    String messageId = json.getString("message-id");
    try {
      Function f = ApiCmd.get(messageId);
      if (null != f) {
        f.call(json);
        ApiCmd.remove(messageId);
      }
    } catch (Exception e) {
    }
  }

  public void onAuthenticationSuccess() {
  }

  public void onAuthenticationFailure() {
  }

  /**
   * SwitchScenes
   *
   * @param scene
   */
  public void onSceneSwitch(String scene_name) {
    System.out.println(scene_name);
  }

  public void onScenesChanged(OBSScene os) {
    System.out.println(os.getName());
  }

  private void onSwitchTransition(String transition_name) {
    System.out.println("onSwitchTransition = " + transition_name);
  }

  private void onTransitionListChanged() {
    System.out.println("onTransitionListChanged");
  }

  public void onStreamStarting(String preview_only) {
    System.out.println("onStreamStarting preview_only = " + preview_only);
  }

  public void onStreamStarted() {
    System.out.println("onStreamStarted");
  }

  public void onStreamStopping(String preview_only) {
    System.out.println("onStreamStopping preview_only = " + preview_only);
  }

  public void onStreamStopped() {
    System.out.println("onStreamStopped");
  }

  public void onRecordingStarting() {
    System.out.println("onRecordingStarting");
  }

  public void onRecordingStarted() {
    System.out.println("onRecordingStarted");
  }

  public void onRecordingStopping() {
    System.out.println("onRecordingStopping");
  }

  public void onRecordingStopped() {
    System.out.println("onRecordingStopped");
  }

  public void onStreamStatus(JSONObject json) {
    System.out.println(json.toJSONString());
  }

  public void onExit() {
    System.out.println("onexit");
  }
}
