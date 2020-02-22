package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class Server {
    private Vector<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        clients = new Vector<>();
        authService = new SimpleAuthService();
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(8189);
            System.out.println("Сервер запустился");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(socket, this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(String fromWhom, String toWhom, String msg) {
        if (toWhom.equals("all")) {
            for (ClientHandler c : clients) {
                if(fromWhom.equals(c.getNick())) {
                   String[] noName = msg.split(" : ");
                   c.sendMsg(noName[1]);
                }else {
                    c.sendMsg(msg);
                }
            }
        } else {
            for (ClientHandler c : clients) {
                if(fromWhom.equals(c.getNick())){
                    c.sendMsg(msg);
                }
                if (toWhom.equals(c.getNick())) {
                    c.sendMsg(toWhom + " : " + msg);
                }
            }
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}
