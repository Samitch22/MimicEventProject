/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventqueueserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Mitchell
 */
public class EventQueueServer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        final int producerPort = 8080;
        final int consumerPort = 8181;
        final MimicEventQueue q = new MimicEventQueue();

        // Producer Listening Thread
        new Thread() {
            @Override
            public void run() {
                try {
                    ServerSocket pServerSocket = new ServerSocket(producerPort);
                    while(true) {
                        System.out.println("Waiting for new producer connection...");
                        Socket pSocket = pServerSocket.accept();
                        new Thread(new ServerProducerProtocol(pSocket, q)).start();
                        System.out.println("New producer connection created.");
                    }
                } catch (IOException ex) {
                    System.out.println("Unexpected Exception with producer: " + ex.getMessage());
                }
            }
        }.start();
        
        // Consumer Listening Thread
        new Thread() {
            @Override
            public void run() {
                try {
                    ServerSocket cServerSocket = new ServerSocket(consumerPort);
                    while(true) {
                        System.out.println("Waiting for new consumer connection...");
                        Socket cSocket = cServerSocket.accept();
                        new Thread(new ServerConsumerProtocol(cSocket, q)).start();
                        System.out.println("New consumer connection created.");
                    }
                } catch (IOException ex) {
                    System.out.println("Unexpected Exception with consumer: " + ex.getMessage());
                }
            }
        }.start();
    }  
}
