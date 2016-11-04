/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventqueueserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Mitchell
 */
public class ServerConsumerProtocol implements Runnable {

    private static final Character   QUIT = 'Q';
    private final Socket             clientConnection;
    private final ObjectInputStream  inFromClient;
    private final ObjectOutputStream outToClient;
    private final MimicEventQueue    q;
    
    public ServerConsumerProtocol(Socket s, MimicEventQueue q) throws IOException
    {
        clientConnection = s;
        this.q = q;
        inFromClient = new ObjectInputStream(clientConnection.getInputStream());
        outToClient = new ObjectOutputStream(clientConnection.getOutputStream());
    }
    
    @Override
    public void run() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Object in = (Character) inFromClient.readObject();
                    if ( in.equals(ServerConsumerProtocol.QUIT) ) {
                        clientConnection.close();
                        System.out.println("Consumer client disconnected.");
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("Unexpected Exception: " + ex.getMessage());
                }
            }
        }.start();
        
        try {
            System.out.println("Consumer client connected!");
            while ( ! clientConnection.isClosed() ) {
                while( q.isEmpty() ) {}
                    if ( !clientConnection.isClosed() )
                    outToClient.writeObject(q.pop());
            }
        } catch (IOException ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
        }
    }
    
}
