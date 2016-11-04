/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mimicevent;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class encapsulates the code for a producer client's logic.
 * @author Mitchell
 */
public class ProducerProtocol {

    private static final Character   QUIT = 'Q';
    private final int                producerPort = 8080;
    private final Socket             clientSocket;
    private final ObjectOutputStream outToServer;

    /**
     * Default constructor for constructing the producer protocol.
     * @throws IOException
     */
    public ProducerProtocol() throws IOException {
        clientSocket = new Socket("localhost", producerPort);
        outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        
        // Disconnects from the server when the application is exited.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    outToServer.writeObject(ProducerProtocol.QUIT);
                    System.out.println("Disconnected from the server.");
                } catch (IOException e) { 
                    System.out.println("Unexpected Exception: " + e.getMessage()); }
        }});
    }
    
    /**
     * Sends a Mimic Event to the server.
     * @param me
     */
    public void send(MimicEvent me) {
        try {
            outToServer.writeObject(me);
        } catch (IOException ex) {
            System.out.println("Error sending event: " + ex.getMessage());
        }
    }
    
}
