package org.peerless;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

public class ChatBootServer {
  public static String Room = "room";
  public static void main (String[] args) throws InterruptedException {
    final String CREATE_ROOM = "createRoom";
    final String MESSAGE = "message";
    final String New_MESSAGE = "newMessage";
    final String TYPING = "typing";

    Configuration config = new Configuration();
    config.setHostname("localhost");
    config.setPort(8080);

    final SocketIOServer server = new SocketIOServer(config);

    server.addEventListener(CREATE_ROOM, String.class, new DataListener<String>() {
      @Override
      public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
        client.joinRoom(data);
      }
    });

    server.addEventListener(MESSAGE, String.class, new DataListener<String>() {
      @Override
      public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
        String[] parts = data.split("\"}");
        server.getRoomOperations(parts[1]).sendEvent(New_MESSAGE, parts[0]+"\"}");
      }
    });

    server.addEventListener(TYPING, String.class, new DataListener<String>() {
      @Override
      public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
        String[] parts = data.split("\"}");
        System.out.println(parts[0]+" "+parts[1]);
        server.getRoomOperations(parts[1]).sendEvent(TYPING, parts[0]);
        System.out.println(parts[0]+" "+parts[1]);
      }
    });

    server.start();
    try {
      Thread.sleep(Integer.MAX_VALUE);
    } catch (Exception e) {
      //TODO: handle exception
      e.printStackTrace();
    }
    server.stop();
  }
} 

