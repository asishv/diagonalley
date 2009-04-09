/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.ApprenticeBuyerRemote;
import server.EveryoneRemote;
import server.MainRemote;
import server.WizardSellerRemote;

/**
 *
 * @author karthik
 */
public class Main {
    String[] userNames = {"Harry", "Ron"};
    String role = null; // Determine if I am buyer or seller
    int costValue = 0; // Indicates Cost C for seller or Value V for buyer
    int quantity = 0; // Indicates the quantity QS for seller or QB for buyer

    /**
    * To register a user. Get the user details and create his account on server.
    */
    void registerUser() {
        //TODO: Make server call to register().

        // Figure out my role: WizardSeller or ApprenticeBuyer
        

        //TODO: Set costValue and quantity based on server's results after register()
    }

    /**
    * ApprenticeBuyers place a bid on the market.
    */
    static void placeBid(ApprenticeBuyerRemote abr) {
        //TODO: Place bid on the market
        // RMI Call to server. Provide Magical Item , price and Qty
        // Validate Magical Item , price and Qty before making server call
        
    }
    
    /**
    * WizardSellers place a trade on the market.
    */
    static void placeTrade(WizardSellerRemote wsr) {
        //TODO: Sellers place a trade on the market
        // RMI Call to server. Provide Magical Item , price and Qty
        // Validate Magical Item , price and Qty before making call
    }

    /**
    * Obtain current score to be displayed
    */
    static void currentScore(EveryoneRemote er) {
        //TODO: Obtain my score from the server
        int score;
        try {
            if(er.isWizard()){
                score =((WizardSellerRemote)er).getScore();
            }
            else {
                score = ((ApprenticeBuyerRemote)er).getScore();
            }
        }
        catch(Exception e) {
            System.out.println("Error getting score: "+e.toString());
            e.printStackTrace();
        }

    }

    /**
    * Obtain current goal to be displayed
    */
    void currentGoal() {
        //TODO: Obtain my current goal from server
    }

    /**
    * Obtain all current bids to be displayed on UI.
    */
    void currentBids() {
        //TODO: Obtain all current bids on the market
        // This is to be displayed for open out cry purposes
    }

    /**
    * Obtain all current trades to be displayed on UI.
    */
    void currentTrades() {
        //TODO: Obtain all the current trades on the market
        // This is to be displayed for open out cry purposes
    }

    /**
    * Obtain all current trades to be displayed on UI.
    */
    static void listAllMagicalItems() {
        //TODO: Obtain a list of all magical items
    }

    /**
    * Current inventory list
    */
    void currentInventoryList() {
        //TODO: Display list of items in my current inventory list
    }

    /**
    * Future Inventory List
    */
    void futureInventoryList() {
        //TODO: Display List of items in my future inventory list
    }
    /**
    * Main function used to test the client and RMI
    */
    public static void main(String[] args) {
        int choice = -1;
        boolean isWizard = false;
        Registry registry;
        MainRemote mr;
        EveryoneRemote er = null;
        String host = (args.length < 1) ? null : args[0];
        try {
            registry = LocateRegistry.getRegistry(host);
            mr = (MainRemote) registry.lookup("Main");
            er = mr.register(host);
            isWizard = er.isWizard();
            
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        while(choice != 0) {
            System.out.println("/n Enter your choice:");
            System.out.println("1: Place bid");
            System.out.println("2: Place trade");
            System.out.println("3: View score");
            System.out.println("4: View Magical Items");
            System.out.println("0: Exit");

            switch(choice) {
                case 1: if(!isWizard) {
                            placeBid((ApprenticeBuyerRemote)er);
                        }
                        else {
                            System.out.println("Cannot place bid. You are not an Apprentice");
                        }
                        break;
                case 2: if(isWizard) {
                            placeTrade((WizardSellerRemote)er);
                        }
                        else {
                            System.out.println("Cannot place trade. You are not a Wizard");
                        }
                        break;
                case 3: currentScore(er);
                        break;
                case 4: listAllMagicalItems();
                        break;
                case 0: choice = 0;
                        break;
                default: break;
            }
        }
    }
}
