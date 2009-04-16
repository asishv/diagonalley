/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.util.Random;
import library.BidTradeArgs;
import library.MagicalItemInfo;
import library.EveryoneRef;
import library.ExecutorRemote;
import library.MainRemote;
import library.UserStats;


class RandomUser extends Thread
{
    String host;
    int port;
    public RandomUser(String host, int port)
    {
        this.host=host;
        this.port=port;
    }
    public void run()
    {
        EveryoneRef er=Main.registerUser(host, port);
        while(!isInterrupted())
        {
            Main.currentScore(er);
            Random random1 = new Random();
            int op=random1.nextInt(2);
            int item=random1.nextInt(20);
            int price=random1.nextInt(1000);
            int quantity=random1.nextInt(1000);
            int msec=random1.nextInt(10000);
            switch(op)
            {
                case 0:                    
                    Main.placeBid(er.getID(), item, price, quantity, msec);
                    break;
                case 1:
                    Main.placeTrade(er.getID(), item, price, quantity, op);
                    break;
            }
        }
    }
}

/**
 *
 * @author karthik
 */
public class Main {
    static MainRemote mr = null;
    static int count = 0;
    int costValue = 0; // Indicates Cost C for seller or Value V for buyer
    int quantity = 0; // Indicates the quantity QS for seller or QB for buyer
    static String hostName;
    static int port;
    /**
    * To register a user. Get the user details and create his account on server.
    */
    static EveryoneRef registerUser(String host, int portNumber, String name) {
        EveryoneRef er = null;
        
        //TODO: Make server call to register().
        try {
            System.err.println("Host:"+host);
            System.err.println("Port Number:"+portNumber);
            //registry = LocateRegistry.getRegistry(portNumber);
            mr = (MainRemote) Naming.lookup("rmi://"+host+":"+portNumber+"/Main");
            er = mr.register(name);
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.toString());
            e.printStackTrace();
        }
        //TODO: Set costValue and quantity based on server's results after register()
        return er;
    }
    
    static EveryoneRef registerUser(String host, int portNumber) {
        Random random = new Random();
        int userNo=random.nextInt(10000);
        String name="User-"+userNo;
        return registerUser(host, portNumber, name);
    }

    /**
    * ApprenticeBuyers place a bid on the market.
    */
    static boolean placeBid(int userID, int magicalItemNumber, int price, int quantity, long msec) {
        //TODO: Place bid on the market
        // RMI Call to server. Provide Magical Item , price and Qty
        // Validate Magical Item , price and Qty before making server call
        Boolean result;
        try {
            ExecutorRemote ex = (ExecutorRemote) Naming.lookup("rmi://"+hostName+":"+port+"/Executor");
            BidTradeArgs bid=new BidTradeArgs(price, quantity, msec);
            result=(Boolean)ex.invoke(userID, magicalItemNumber, 3, bid);
            return result.booleanValue();
        }catch (Exception e) {
            System.out.println("Error: Reading from cmd line - " + e.toString());
                                e.printStackTrace();
        }
        return false;
    }
    
    /**
    * WizardSellers place a trade on the market.
    */
    static boolean placeTrade(int userID, int magicalItemNumber, int price, int quantity, long m) {
        //TODO: Sellers place a trade on the market
        // RMI Call to server. Provide Magical Item , price and Qty
        // Validate Magical Item , price and Qty before making call
        Boolean result;
        try {
            ExecutorRemote ex = (ExecutorRemote) Naming.lookup("rmi://"+hostName+":"+port+"/Executor");
            BidTradeArgs trade=new BidTradeArgs(price, quantity, m);
            result=(Boolean)ex.invoke(userID, magicalItemNumber, 0, trade);
            return result.booleanValue();
        }catch (Exception e) {
            System.out.println("Error: Reading from cmd line - " + e.toString());
                                e.printStackTrace();
        }
        return false;
    }
    
    static boolean modifyBid(int userID, int magicalItemNumber, int price, int quantity, long msec) 
    {
        //TODO: Place bid on the market
        // RMI Call to server. Provide Magical Item , price and Qty
        // Validate Magical Item , price and Qty before making server call
        Boolean result;
        try {
            ExecutorRemote ex = (ExecutorRemote) Naming.lookup("rmi://"+hostName+":"+port+"/Executor");
            BidTradeArgs bid=new BidTradeArgs(price, quantity, msec);
            result=(Boolean)ex.invoke(userID, magicalItemNumber, 4, bid);
            return result.booleanValue();
        }catch (Exception e) {
            System.out.println("Error: Reading from cmd line - " + e.toString());
                                e.printStackTrace();
        }
        return false;
    }

        /**
    * WizardSellers place a trade on the market.
    */
    static boolean modifyTrade(int userID, int magicalItemNumber, int price, int quantity, long m) {
        //TODO: Sellers place a trade on the market
        // RMI Call to server. Provide Magical Item , price and Qty
        // Validate Magical Item , price and Qty before making call
        Boolean result;
        try {
            ExecutorRemote ex = (ExecutorRemote) Naming.lookup("rmi://"+hostName+":"+port+"/Executor");
            BidTradeArgs trade=new BidTradeArgs(price, quantity, m);
            result=(Boolean)ex.invoke(userID, magicalItemNumber, 1, trade);
            return result.booleanValue();
        }catch (Exception e) {
            System.out.println("Error: Reading from cmd line - " + e.toString());
                                e.printStackTrace();
        }
        return false;
    }
    
    /**
    * Obtain current score to be displayed
    */
    static int currentScore(EveryoneRef er) {
        //TODO: Obtain my score from the server
        int score = 0;
        UserStats result;
        try {
           ExecutorRemote ex = (ExecutorRemote) Naming.lookup("rmi://"+hostName+":"+port+"/Executor");
            result=(UserStats)ex.invoke(er.getID(), er.getItemNumber(), 2, er);
            return result.getScore();
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
    static MagicalItemInfo currentCommodity(EveryoneRef er) {
        //TODO: Obtain my current goal from server
        MagicalItemInfo miir;
       UserStats result;
        try {
           ExecutorRemote ex = (ExecutorRemote) Naming.lookup("rmi://"+hostName+":"+port+"/Executor");
            result=(UserStats)ex.invoke(er.getID(), er.getItemNumber(), 2, er);
            return result.getMagicalItemInfo();
        } catch (Exception e) {
            System.out.println("Error getting current commodity: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }
    
    static int getQuantity(EveryoneRef er, int itemNumber)
    {
       Integer result;
        try {
           ExecutorRemote ex = (ExecutorRemote) Naming.lookup("rmi://"+hostName+":"+port+"/Executor");
            result=(Integer)ex.invoke(er.getID(), itemNumber, 5, er);
            return result.intValue();
        } catch (Exception e) {
            System.out.println("Error getting quantity for item "+itemNumber +" "+ e.toString());
            e.printStackTrace();
        }
        return 0;        
    }
    
    static int getAverageSellingPrice(EveryoneRef er, int itemNumber)
    {
       Integer result;
        try {
            ExecutorRemote ex = (ExecutorRemote) Naming.lookup("rmi://"+hostName+":"+port+"/Executor");
            result=(Integer)ex.invoke(er.getID(), itemNumber, 6, er);
            return result.intValue();
        } catch (Exception e) {
            System.out.println("Error getting quantity for item "+itemNumber +" "+ e.toString());
            e.printStackTrace();
        }
        return 0;        
    }


    static int targetCost(EveryoneRef er) {
        //TODO: Obtain my current goal from server
        int cost;
       UserStats result;
        try {
           ExecutorRemote ex = (ExecutorRemote) Naming.lookup("rmi://"+hostName+":"+port+"/Executor");
            result=(UserStats)ex.invoke(er.getID(), er.getItemNumber(), 2, er);
            return result.getCost();
        } catch (Exception e) {
            System.out.println("Error getting target cost: " + e.toString());
            e.printStackTrace();
        }
        return -1;
    }

    static int targetQuantity(EveryoneRef er) {
       UserStats result;
        try {
           ExecutorRemote ex = (ExecutorRemote) Naming.lookup("rmi://"+hostName+":"+port+"/Executor");
            result=(UserStats)ex.invoke(er.getID(), er.getItemNumber(), 2, er);
            return result.getQuantity()+result.getQuantityLocked();
        } catch (Exception e) {
            System.out.println("Error getting target quantity: " + e.toString());
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
            MagicalItemInfo[] magicalItemInfo;
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
        EveryoneRef er= null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int price, quantity, magicalItemNumber = 0;
        long msec;
        String host = (args.length < 1) ? null : args[0];
        hostName=host;
        port=Integer.parseInt(args[1]);
        try {
            er = registerUser(host,port);
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
            System.out.println("6: View the quantity");
            System.out.println("7: Random test!");
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
                            if(placeBid(er.getID(), magicalItemNumber, price, quantity, msec)) {
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
                            if(placeTrade(er.getID(), magicalItemNumber, price, quantity, msec)) {
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
                case 5: MagicalItemInfo miir = currentCommodity(er);
                        int tcost=targetCost(er), tquantity=targetQuantity(er);
                        if(er.isWizard())
                            System.out.println("You have a goal of selling "+tquantity+" "+miir.getName()+"["+miir.getIndex()+"]"+" and your target selling price is "+tcost);
                        else
                            System.out.println("You have a goal of buying "+tquantity+" "+miir.getName()+"["+miir.getIndex()+"]"+" and your buying price target is "+tcost);
                        System.out.println(miir.getSymbol());
                        System.out.println(miir.getPicture());
                        break;
                case 6:
                         System.out.println("Enter the item number:");
                         int itemNumber = Integer.parseInt(br.readLine());
                         System.out.println("You have "+getQuantity(er, itemNumber)+" of item no. "+itemNumber);
                         break;
                case 7:
                        RandomUser ru[]=new RandomUser[100];
                        for(int k=0; k<100; k++)
                        {
                            ru[k]=new RandomUser(hostName, port);
                            ru[k].start();
                        }
                        while(true){}
                        /*for(int k=0; k<100; k++)
                        {
                            ru[k].interrupt();
                        }*/
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
