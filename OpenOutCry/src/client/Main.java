/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import library.MagicalItemInfoRemote;
import library.ApprenticeBuyerRemote;
import library.EveryoneRemote;
import library.MainRemote;
import library.WizardSellerRemote;

/**
 *
 * @author karthik
 */
public class Main {
    static MainRemote mr = null;
    static int count = 0;
    int costValue = 0; // Indicates Cost C for seller or Value V for buyer
    int quantity = 0; // Indicates the quantity QS for seller or QB for buyer

    /**
    * To register a user. Get the user details and create his account on server.
    */
    static EveryoneRemote registerUser(String host, int portNumber) {
        Registry registry = null;
        EveryoneRemote er = null;
        
        //TODO: Make server call to register().
        try {
            Random random = new Random();
            count=random.nextInt(10000);
            System.err.println("Host:"+host);
            System.err.println("Port Number:"+portNumber);
            //registry = LocateRegistry.getRegistry(portNumber);
            mr = (MainRemote) Naming.lookup("rmi://"+host+":"+portNumber+"/Main");
            er = mr.register("User-"+count);
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.toString());
            e.printStackTrace();
        }
        //TODO: Set costValue and quantity based on server's results after register()
        return er;
    }

    /**
    * ApprenticeBuyers place a bid on the market.
    */
    static boolean placeBid(ApprenticeBuyerRemote abr, int magicalItemNumber, int price, int quantity, long msec) {
        //TODO: Place bid on the market
        // RMI Call to server. Provide Magical Item , price and Qty
        // Validate Magical Item , price and Qty before making server call
        try {
            return abr.bid(price, quantity, magicalItemNumber, msec);
        }catch (Exception e) {
            System.out.println("Error: Reading from cmd line - " + e.toString());
                                e.printStackTrace();
        }
        return false;
    }
    
    /**
    * WizardSellers place a trade on the market.
    */
    static boolean placeTrade(WizardSellerRemote wsr, int magicalItemNumber, int price, int quantity, long m) {
        //TODO: Sellers place a trade on the market
        // RMI Call to server. Provide Magical Item , price and Qty
        // Validate Magical Item , price and Qty before making call
        try {
            return wsr.trade(price, quantity, magicalItemNumber, m);
        }catch (Exception e) {
            System.out.println("Error: Reading from cmd line - " + e.toString());
                                e.printStackTrace();
        }
        return false;
    }

    /**
    * Obtain current score to be displayed
    */
    static int currentScore(EveryoneRemote er) {
        //TODO: Obtain my score from the server
        int score = 0;
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
        return score;
    }

    /**
    * Obtain current commodity which the buyer or seller is dealing with
    */
    static MagicalItemInfoRemote currentCommodity(EveryoneRemote er) {
        //TODO: Obtain my current goal from server
        MagicalItemInfoRemote miir;
        try {
            if(er.isWizard())
                miir = ((WizardSellerRemote)er).getTargetCommodityInfo();
            else
                miir = ((ApprenticeBuyerRemote)er).getTargetCommodityInfo();                
            return miir;
         } catch (Exception e) {
            System.out.println("Error getting list of magical items: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    static int targetCost(EveryoneRemote er) {
        //TODO: Obtain my current goal from server
        int cost;
        try {
            if(er.isWizard())
                cost = ((WizardSellerRemote)er).getTargetCost();
            else
                cost = ((ApprenticeBuyerRemote)er).getTargetCost();                
            return cost;
         } catch (Exception e) {
            System.out.println("Error getting list of magical items: " + e.toString());
            e.printStackTrace();
        }
        return -1;
    }

    static int targetQuantity(EveryoneRemote er) {
        //TODO: Obtain my current goal from server
        int qty;
        try {
            if(er.isWizard())
                qty = ((WizardSellerRemote)er).getTargetQuantity();
            else
                qty = ((ApprenticeBuyerRemote)er).getTargetQuantity();                
            return qty;
         } catch (Exception e) {
            System.out.println("Error getting list of magical items: " + e.toString());
            e.printStackTrace();
        }
        return -1;
    }

    
   /**
    * Obtain all current trades to be displayed on UI.
    */
    static void listAllMagicalItems(MainRemote mr) {
        //TODO: Obtain a list of all magical items
        try {
            MagicalItemInfoRemote[] magicalItemInfo;
            magicalItemInfo = mr.getAllMagicalItems();
            for(int i= 0; i < magicalItemInfo.length; i++){
                System.out.println("Name:" + magicalItemInfo[i].getName()+ " Symbol:"+magicalItemInfo[i].getSymbol());
            }
        } catch (Exception e) {
            System.out.println("Error getting list of magical items: " + e.toString());
            e.printStackTrace();
        }

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
        EveryoneRemote er= null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int price, quantity, magicalItemNumber = 0;
        long msec;
        String host = (args.length < 1) ? null : args[0];
        try {
            er = registerUser(host,Integer.parseInt(args[1]));
            isWizard = er.isWizard();
            
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        while(choice != 0) {
            System.out.println("/n/n************************************************");
            System.out.println(" Enter your choice:");
            System.out.println("1: Place bid");
            System.out.println("2: Place trade");
            System.out.println("3: View score");
            System.out.println("4: View Magical Items");
            System.out.println("5: View my Goal/Target");
            System.out.println("0: Exit");
            try {
            choice = Integer.parseInt(br.readLine());
            switch(choice) {
                case 1: if(!isWizard)
                        {
                            System.out.println("Place bid: Enter Magical Item, price, quantity & time for bid to last(mins)");
                            magicalItemNumber = Integer.parseInt(br.readLine());
                            price = Integer.parseInt(br.readLine());
                            quantity = Integer.parseInt(br.readLine());
                            msec = Integer.parseInt(br.readLine());
                            msec*=60*1000; 
                            if(placeBid((ApprenticeBuyerRemote)er, magicalItemNumber, price, quantity, msec)) {
                                System.out.println("Bid placed successfully");
                            } else {
                                System.out.println("Bid could not be placed");
                            }
                        }
                        else {
                            System.out.println("Cannot place bid. You are not an Apprentice");
                        }
                        break;
                case 2: if(isWizard) {
                            System.out.println("Place Trade: Enter Magical Item, price, quantity & time for bid to last(ms)");
                            magicalItemNumber = Integer.parseInt(br.readLine());
                            price = Integer.parseInt(br.readLine());
                            quantity = Integer.parseInt(br.readLine());
                            msec = Integer.parseInt(br.readLine());;
                            if(placeTrade((WizardSellerRemote)er, magicalItemNumber, price, quantity, msec)) {
                                System.out.println("Trade placed successfully");
                            } else {
                                System.out.println("Trade could not be placed");
                            }
                        }
                        else {
                            System.out.println("Cannot place trade. You are not a Wizard");
                        }
                        break;
                case 3: System.out.println("Score: " + currentScore(er));
                        break;
                case 4: listAllMagicalItems(mr);
                        break;
                case 5: MagicalItemInfoRemote miir = currentCommodity(er);
                        int tcost=targetCost(er), tquantity=targetQuantity(er);
                        if(er.isWizard())
                            System.out.println("You have a goal of selling "+tquantity+" "+miir.getName()+"["+miir.getIndex()+"]"+" and your target selling price is "+tcost);
                        else
                            System.out.println("You have a goal of buying "+tquantity+" "+miir.getName()+"["+miir.getIndex()+"]"+" and your buying price target is "+tcost);
                        System.out.println(miir.getSymbol());
                        System.out.println(miir.getPicture());
                        break;
                case 0: choice = 0;
                        System.exit(0);
                default: break;
            }
            } catch (Exception e) {
                                System.out.println("Error: " + e.toString());
                                e.printStackTrace();
                            }
        }
    }
}
