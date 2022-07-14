package de.thb.sparefood_app.ui.chat;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import java.io.IOException;

public class ChatClient extends AsyncTask<Void, Void, Void> {

    //MOVE
    private WebSocket webSocket;
    private final Gson gson = new Gson();
    private static final String BEARER_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdC9zcGFyZWZvb2QiLCJ1cG4iOiJsdWVkcmlja0B0aC1icmFuZGVuYnVyZy5kZSIsImV4cCI6MTY1Nzg0NzA5MCwiZ3JvdXBzIjpbIlVzZXIiXSwiYmlydGhkYXRlIjoiMTk5OS0wOC0xMiIsImlhdCI6MTY1Nzc4NzA5MCwianRpIjoiYzk1NTNmYWQtMWZkYi00MTU4LTkzNGQtNjY3YjMyZTk0OGMwIn0.hChOkeDrfKx05DNGqifdiRaE9jyx7jxMbYy4UFS6dtxnInZ-xuHQ9riOPTICwXWG6NXo8GWHvwnFgewL6JPMU-ZQjIUItbvhtt2-Y053wLa4vZJT4Px-sD0uLKxxIuO9o8wep5h74CcRex36nxt6QnxMxzZXrAx9ynqDLEyvOg8hH4yzNdDBzZysia-GFey6xEo6pB_w2cZmmpi6zogBcRf2lDdmGUD0v8xwd39S3LGk73EDaepZYBrak4UAUZIKxB-KkqifmdVIP6edL0daFBbykM6Pgel-u4rEUZ5oNtaSWidpvUHcN4-KzCle7Kbg_fFdMaVECQsKLSh1eKP_pw";
    //    private static final String BEARER_TOKEN_TOBIAS = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdC9zcGFyZWZvb2QiLCJ1cG4iOiJ0cm9tcGVsbEB0aC1icmFuZGVuYnVyZy5kZSIsImV4cCI6MTY1NzcwOTEyMSwiZ3JvdXBzIjpbIlVzZXIiXSwiYmlydGhkYXRlIjoiMTk5OS0wOC0xMiIsImlhdCI6MTY1NzY0OTEyMSwianRpIjoiYjEzMjJmOTItMmRlNC00OWM4LTlhM2EtOWVjOTliOWEzNmY2In0.OM9N1jtqwrD1TRgITBgnXwbVduOHF_jSHoIePq7ZWuHPyW0Gu9sGZReTpRmgMlcnDdB6VRVytGiHJMcs9l5cuJkk4OG72gggc5OH4KQjxpS-ojOskKjdgRNyeoTibxIalxAhCGhfY_fci2hAswOvCMWaPODGkVBtU0H2pU3VMaWOC5qUoPiBy-ER1_tI13F6GeQyBzct1EIY6-Dr2VjU8mGhAQ-RKz6FFIUTiT2N7cCVVKhagbCz4572nKYbm6BqcUBVGKc5iRqaYvVfD_ERGXOzFniqXZOtPzFgMnh35RVkcMaX9zli61AHnxByPI-Pxjb1zxOgaDMqrj02TPTJUA";
    private static final String CHAT_SERVER_URL = "http://10.0.2.2:8080/chat";
    private String input;

    public ChatClient() throws IOException {
//        this.webSocket = new WebSocketFactory().createSocket(CHAT_SERVER_URL);
//
//        // we can add more listeners if we want, but onMessage is the most important one
//        webSocket.addListener(
//                new WebSocketAdapter() {
//                    @Override
//                    public void onTextMessage(WebSocket websocket, String message) throws Exception {
//                        Message messageObject = gson.fromJson(message, Message.class);
////                        System.out.println("RECEIVED FROM SERVER: " + messageObject);
//                        Log.d("CHAT MESSAGE", "RECEIVED FROM SERVER: " + messageObject);
//                    }
//                });
//
//        try {
//            // Connect to the server and perform an opening handshake.
//            // This method blocks until the opening handshake is finished.
//            webSocket.addHeader("Authorization", "Bearer " + BEARER_TOKEN);
//            Log.d("Header", "attached");
//            webSocket.connect();
//        } catch (Exception e) {
////            System.out.println(e);
//            Log.d("EXCEPTION", "" + e);
//        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        input = "Hello Chat!";
        try {
            this.webSocket = new WebSocketFactory().createSocket(CHAT_SERVER_URL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // we can add more listeners if we want, but onMessage is the most important one
        webSocket.addListener(
                new WebSocketAdapter() {
                    @Override
                    public void onTextMessage(WebSocket websocket, String message) throws Exception {
                        Message messageObject = gson.fromJson(message, Message.class);
//                        System.out.println("RECEIVED FROM SERVER: " + messageObject);
                        Log.d("CHAT MESSAGE", "RECEIVED FROM SERVER: " + messageObject + messageObject.getContent());
                    }
                });

        try {
            // Connect to the server and perform an opening handshake.
            // This method blocks until the opening handshake is finished.
            webSocket.addHeader("Authorization", "Bearer " + BEARER_TOKEN);
            Log.d("Header", "attached");
            webSocket.connect();
        } catch (Exception e) {
//            System.out.println(e);
            Log.d("EXCEPTION", "" + e);
        }

        sendMessage();

        return null;
    }

    public void sendMessage() {
        // read out whatever the user typed

        Message message = new de.thb.sparefood_app.ui.chat.Message(BEARER_TOKEN, "luedrick@th-brandenburg.de", "Hello Chatttt");
//        message.setContent(messageContent);
//        message.setBearerToken(BEARER_TOKEN);
//        message.setRecipient("luedrick@th-brandenburg.de");

        webSocket.sendText(gson.toJson(message));
    }

    public void testMethod() {
        try {
            ChatClient chatClient = new ChatClient();
            chatClient.sendMessage();
        } catch (IOException e) {
            Log.d("Exception", "Chatfragment Failed");
            e.printStackTrace();
        }
    }

}
