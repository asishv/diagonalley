/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import DailyProphet.EventLogger;
import library.MainRemote;
import DailyProphet.EventReader;
import java.util.Date;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import library.EventReaderRemote;


/**
 *
 * @author Asish
 */
public class Main extends Thread implements MainRemote{
     public static final String LOG_FILE="DiagonAlleyLog.txt";
     EventLogger out;
     
     DailyProphet.EventReader in;
     Date startTime;
     MagicalItem[] magicalItems;
     public static final int MAX_COMMODITY = 20;
     ArrayList<WizardSeller> wizards;
     ArrayList<ApprenticeBuyer> apprentices;
     ArrayList<MagicalItemInfo> magicalItemInfo;
     volatile int numberOfUsers=0;
     
     MagicalItem getMagicalItem(int magicalItemNumber)
     {
         return magicalItems[magicalItemNumber];
     }
     
     public MagicalItemInfo[] getAllMagicalItems()
     {
         MagicalItemInfo[] m=new MagicalItemInfo[magicalItemInfo.size()];
         for(int i=0; i<magicalItemInfo.size(); i++)
         {
            m[i]=magicalItemInfo.get(i);
         }
         return m;
     }

     /**
     * Creates the Diagon Alley Wizards Sellers.
     */    
    WizardSeller createWizards(String name)
    {
       WizardSeller ws=new WizardSeller(name, numberOfUsers-1);
       Random random = new Random();
       int commodity=random.nextInt(20), quantity, cost;
  
       int apprenticeNo=findApprentice(magicalItems[commodity].magicalItemInfo);
       if(apprenticeNo == -1)
       {
           quantity=random.nextInt(1000)+100;
           cost=random.nextInt(1000)+100;
       }
       else
       {
           ApprenticeBuyer ab=apprentices.get(apprenticeNo);
           cost=ab.getTargetCost()-random.nextInt(10);
           quantity=ab.getTargetQuantity();           
       }
       
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           DiagonAlleyBuyerAccount daba=new DiagonAlleyBuyerAccount(ws);
           magicalItems[i].buyerAccount.add(daba);
           DiagonAlleySellerAccount dasa=new DiagonAlleySellerAccount(ws);
           magicalItems[i].sellerAccount.add(dasa);
       }
       
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           CurrentInventoryList ci=new CurrentInventoryList(0, 0, ws.index, magicalItems[i]);
           ws.currentInventoryList.add(ci);
       }
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           FutureInventoryList fi=new FutureInventoryList(0, 0, ws.index, magicalItems[i]);
           ws.futureInventoryList.add(fi);
       }

       CurrentInventoryList ci=ws.currentInventoryList.get(commodity);
       ci.quantity=quantity;
       ci.sellingPriceTarget=cost;
       ws.currentInventoryList.add(ci);

       wizards.add(ws);
       DiagonAlleySellerAccount dasa=new DiagonAlleySellerAccount(ws);
       magicalItems[commodity].sellerAccount.add(dasa);
       ci.diagonAlleySellerAccount=dasa;
       out.debug("Wizard Seller: "+name+" created!");
       return ws;
    }
    
     /**
     * Finds the wizard selling a particular magical item.
     */    
    int findWizard(MagicalItemInfo m)
    {
        for(int i=0; i<wizards.size(); i++)
        {
            WizardSeller ws=wizards.get(i);
            if(ws.getTargetCommodityInfo() == m)
                return i;
        }
        out.debug("No previous wizard for the commodity "+m.name+" exists!");
        return -1;
    }

    /**
     * Finds the buyerAccount buying a particular magical item.
     */    
    int findApprentice(MagicalItemInfo m)
    {
        for(int i=0; i<apprentices.size(); i++)
        {
            ApprenticeBuyer ab=apprentices.get(i);
            if(ab.getTargetCommodityInfo() == m)
                return i;
        }
        out.debug("No previous apprentice for the commodity "+m.name+" exists!");
        return -1;
    }

     /**
     * Register a user in Diagon Alley.
     */    
    synchronized public Everyone register(String name)
    {
        numberOfUsers++; //Handle Concurrency
        if(numberOfUsers%2 == 0) {
            out.write(name+" joined the game!\r\n"+name+" is a Wizard (Seller)\r\n");
            return (Everyone)createWizards(name);
        }
        else {
            out.write(name+" joined the game!\r\n"+name+" is a Apprentice (Buyer)\r\n");
            return (Everyone)createApprentices(name);
        }
    }
    
     /**
     * Creates the Diagon Alley Apprentice Buyers.
     */    
    ApprenticeBuyer createApprentices(String name)
    {
       ApprenticeBuyer ab=new ApprenticeBuyer(name, numberOfUsers-1, out);
       Random random = new Random();
       int commodity=random.nextInt(20), quantity, cost;
       int wizardNo=findWizard(magicalItems[commodity].magicalItemInfo);
       if(wizardNo == -1)
       {
           quantity=random.nextInt(1000)+100;
           cost=random.nextInt(1000)+100;
       }
       else
       {
           WizardSeller ws=wizards.get(wizardNo);
           cost=ws.getTargetCost()+random.nextInt(10);
           quantity=ws.getTargetQuantity();
       }
       
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           DiagonAlleyBuyerAccount daba=new DiagonAlleyBuyerAccount(ab);
           magicalItems[i].buyerAccount.add(daba);
           DiagonAlleySellerAccount dasa=new DiagonAlleySellerAccount(ab);
           magicalItems[i].sellerAccount.add(dasa);
       }

       for(int i=0; i<MAX_COMMODITY; i++)
       {
           CurrentInventoryList ci=new CurrentInventoryList(0, ab.index, 0, magicalItems[i]);
           ab.currentInventoryList.add(ci);
       }
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           FutureInventoryList fi=new FutureInventoryList(0, 0, ab.index, magicalItems[i]);
           ab.futureInventoryList.add(fi);
       }

       FutureInventoryList fi=ab.futureInventoryList.get(commodity);
       fi.quantity=quantity;
       fi.buyingTargetPrice=cost;
       ab.futureInventoryList.add(fi);
       apprentices.add(ab);        
       DiagonAlleyBuyerAccount daba=new DiagonAlleyBuyerAccount(ab);
       magicalItems[commodity].buyerAccount.add(daba);
       fi.diagonAlleyBuyerAccount=daba;
       out.debug("Apprentice Buyer: "+name+" created!");
       return ab;
    }
   
    /**
     * Creates the Diagon Alley magical items.
     */    
    void createMagicalItems()
    {
        magicalItems=new MagicalItem[MAX_COMMODITY];
        magicalItemInfo = new ArrayList();
         try{
            InputStream instream = getClass().getResourceAsStream("DiagonAlleyMagicalItems.csv");
            InputStreamReader infile = new InputStreamReader(instream);
            BufferedReader br = new BufferedReader(infile);
            for(int i=0; i<MAX_COMMODITY; i++)
            {
                magicalItems[i]=new MagicalItem(i);
                if(br.ready())
                {
                    String line=br.readLine();
                    String elem[]=line.split(",");
                    magicalItems[i].magicalItemInfo= new MagicalItemInfo(elem[0], elem[1], elem[2], magicalItems[i].index);
                    magicalItemInfo.add(magicalItems[i].magicalItemInfo);
                }
            }
        }catch(IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }        
        out.debug("Magical items successfully created");
    }
    
     /**
     * Creates a virtual wizard.
     */    
    void createVirtualWizards()
    {
     //TODO: Complete this code.  
        out.debug("Created virtual wizards");
    }

     /**
     * Creates a virtual buyerAccount.
     */    
    void createVirtualApprentices()
    {
     //TODO: Complete this code.   
        out.debug("Created virtual apprentices");
    }

    /**
     * Creates the Diagon Alley magical world.
     */    
    void createMagicalWorld()
    {
        createMagicalItems();
        createVirtualWizards();
        createVirtualApprentices();
    }
    
    public void run()
    {
        //TODO:Check if all buyerAccount and sellerAccount have met their goals
        //if (true) stop the server
        try{
           while(true)
           {
                Thread.sleep(5000);
                
           }
        }
        catch(InterruptedException ie)
        {

        }
    }
    
    Main()
    {
        try{
            FileWriter fw=new FileWriter(LOG_FILE);
            out=new DailyProphet.EventLogger(fw, 1000);
            out.writeln("Welcome to Diagon Alley Open Outcry Auction!");
            in=new DailyProphet.EventReader();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
            System.exit(0);
        }
        createMagicalWorld();
        wizards=new ArrayList();
        apprentices=new ArrayList();
    }
    
    public static void main(String args[])
    {
        Registry registry=null;
       	try {
	    Main obj = new Main();
            obj.in = new EventReader();
            EventReaderRemote stub1 = (EventReaderRemote) UnicastRemoteObject.exportObject(obj.in, 0);
	    MainRemote stub2 = (MainRemote) UnicastRemoteObject.exportObject(obj, 0);
	    // Bind the remote object's stub2 in the registry
            switch(args.length)
            {
                case 0:
                    System.err.println("No arguments supplied");
              	    registry = LocateRegistry.getRegistry();
                    break;
                case 1:
                    System.err.println("Port: "+args[0]);
                    registry = LocateRegistry.getRegistry(Integer.parseInt(args[0]));
                    break;
                case 2:
                    System.err.println("Hostname:"+args[0]+" Port:"+args[1]);
                    registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[0]));
                    break;
            }
            registry.bind("Read", stub1);
	    registry.bind("Main", stub2);
	    System.err.println("Server ready");
            obj.out.debug("Server started successfully\r\n");
            System.err.println("<Press ENTER to quit>");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                br.readLine();
        } 
        catch (IOException ioe) {ioe.printStackTrace();}
	catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
        }
        finally
        {
            try{
                registry.unbind("Read");
                registry.unbind("Main");
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            System.exit(0);
        }
//        m.start();
    }
}
