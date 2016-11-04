/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mimicevent;

import colatzcalculator.FXMLDocumentController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import javafx.application.Platform;

/**
 * This class encapsulates the code for a consumer client's logic.
 * @author Mitchell
 */
public class ConsumerProtocol implements Runnable {

    private static final Character   QUIT = 'Q';
    private final BigInteger         two = new BigInteger("2");
    private final int                consumerPort = 8181;
    private final ObjectInputStream  inFromServer;
    private final ObjectOutputStream outToServer;
    private final Socket             clientSocket;
    
    /**
     * Default constructor for constructing the consumer protocol.
     * @throws IOException
     */
    public ConsumerProtocol() throws IOException {
        clientSocket = new Socket("localhost", consumerPort);
        outToServer  = new ObjectOutputStream(clientSocket.getOutputStream());
        inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        
        // Disconnects from the server when the application is exited.
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                try {
                    outToServer.writeObject(ConsumerProtocol.QUIT);
                } catch (IOException e) { 
                    System.out.println("Unexpected Exception: " + e.getMessage()); 
                }
        }});
    }
    
    @Override
    public void run() {
        try {
            while ( true ) {
                MimicEvent me = (MimicEvent) inFromServer.readObject();
                System.out.println("Received: " + me.toString());

                // Manipulate data to process
                BigInteger num = new BigInteger(me.getN(), 2);
                num = num.multiply(two).add(BigInteger.ONE);
                String binaryNum = num.toString(2);
                System.out.println("Executing the event with: " + binaryNum);

                // Call the GUI button click method handler
                FXMLDocumentController controller = FXMLDocumentController.getController();
                Platform.runLater(() -> {
                    controller.handleNextButton(binaryNum);
                });
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found! " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Disconnected from server.");
        }
    }
    
}
