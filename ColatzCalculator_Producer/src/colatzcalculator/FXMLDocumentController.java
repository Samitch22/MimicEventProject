/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colatzcalculator;


import mimicevent.MimicEvent;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import mimicevent.ProducerProtocol;

/**
 * This controller controls the main, and only, scene for the
 * Collatz calculator. It has a single event handler, handleButtonAction which
 * handles events for all buttons on the form.
 * 
 * @author RRM
 * @author Mitchell
 */
public class FXMLDocumentController implements Initializable {
    
    private static FXMLDocumentController controller;
    private ProducerProtocol              producerProtocol;
    private final boolean                 isProducer = true;
    
    @FXML
    private TextField txtNBase10;
    
    @FXML
    private TextField txtNBinary;
    
    @FXML
    private TextArea txtFldHistory;
    
    @FXML
    private Button btnNextN;
    

        /**
     * The initialize method is used to setup the controller for routing events
     * as well as creating attributes that might be needed to help with 
     * handling events
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = this;
        try {
            producerProtocol = new ProducerProtocol();
        } catch (IOException ex) {
            System.out.println("Unexpected Exception creating client: " + ex.getMessage());
        }
    }    

    
    
    
    
    /**
     * This method is the sole event handler for any of the buttons on main page.
     * It accomplishes this by identifying the button that generated the onAction
     * event, casting the getSource method of the event to a Button and passing
     * that button to the correct helper method.
     * 
     * @param event 
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {

        Button b =(Button)event.getSource();
        
        if ( b.getText().compareTo("Next N") == 0)
        {
            handleNextButton(txtNBinary.getText());
        }
        else if ( b.getText().compareTo("Clear") == 0)
        {
            handleClearButton(b);
        }
    }
    
    
    
    
   /**
    * This method encapsulates all necessary code to handle the
    * ClearButton's onAction event
    * @param b 
    */
    private void handleClearButton(Button b)
    {
        txtNBase10.clear();
        txtNBinary.clear();
        txtFldHistory.clear();        
    }
    
    
    /**
     * This method encapsulates all the necessary code to
     * handle the NextButton's onAction event.
     * 
     * @param b 
     */
    public void handleNextButton(String b)
    {
        if ( this.isProducer == true ) {
            MimicEvent me = new MimicEvent(txtNBinary.getText());
            producerProtocol.send(me);
        }
        
        final BigInteger four = new BigInteger("4");
        final BigInteger three = new BigInteger("3");
        final BigInteger two = new BigInteger("2");
        BigInteger N = new BigInteger(b, 2);
        txtNBase10.setText(N.toString());
        txtFldHistory.setText(txtFldHistory.getText()+b+"\n");

        switch (N.mod(four).intValueExact()){

            case 0: N = N.multiply(three).divide(four);
            break;
            case 2: N = N.subtract(two).divide(four);
            break;
            case 1:
            case 3: N =  N.multiply(three).add(BigInteger.ONE).divide(two);
            break;
        }

        txtNBinary.setText(N.toString(2));
        txtNBase10.setText(N.toString());
    }
    
    public static FXMLDocumentController getController() {
        return controller;
    }
}
