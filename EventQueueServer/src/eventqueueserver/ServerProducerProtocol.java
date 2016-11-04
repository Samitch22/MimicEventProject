/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventqueueserver;

import mimicevent.MimicEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Mitchell
 */
public class ServerProducerProtocol implements Runnable {
    
    private final Socket clientConnection;
    //private final BufferedReader reader;
    private final ObjectInputStream inFromClient;
    private final ObjectOutputStream outToClient;
    private final MimicEventQueue q;
    
    public ServerProducerProtocol(Socket s, MimicEventQueue q) throws IOException
    {
        clientConnection = s;
        this.q = q;
        //reader =  new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
        inFromClient = new ObjectInputStream(clientConnection.getInputStream());
        outToClient = new ObjectOutputStream(clientConnection.getOutputStream());
    }

    @Override
    public void run() {
        Object in;
        Object obj;
        try {
            System.out.println("Producer client connected!");
            while ( ! clientConnection.isClosed() ) {
                in = inFromClient.readObject();
                if ( in.equals('Q') )
                    break;
                obj = (MimicEvent) in;
                System.out.println("In from client: " + obj.toString());
                
                q.push(obj);
            }
            clientConnection.close();
            System.out.println("Producer client disconnected.");
        } catch (IOException ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found! " + ex.getMessage());
        }
    }
}
