/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import library.MainRemote;
import DailyProphet.EventReader;
import java.util.Date;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import library.*;

/**
 *
 * @author Asish
 */
public class Main extends Thread implements MainRemote{
     public static final String LOG_FILE="DiagonAlleyLog.txt";
     public DailyProphet.EventLogger out;
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
         MagicalItemInfo[] m=(MagicalItemInfo[])magicalItemInfo.toArray();
         System.out.println("Number of magical Items="+m.length);
         return m;
     }

     /**
     * Creates the Diagon Alley Wizards Sellers.
     */    
    WizardSeller createWizards(String name)
    {
       WizardSeller ws=new WizardSeller(name);
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
           CurrentInventoryList ci=new CurrentInventoryList(0, 0, magicalItems[i]);
           ws.currentInventoryList.add(ci);
       }
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           FutureInventoryList fi=new FutureInventoryList(0, 0, magicalItems[i]);
           ws.futureInventoryList.add(fi);
       }

       CurrentInventoryList ci=ws.currentInventoryList.get(commodity);
       ci.quantity=quantity;
       ci.sellingPriceTarget=cost;
       ws.currentInventoryList.add(ci);

       wizards.add(ws);
       DiagonAlleySellerAccount dasa=new DiagonAlleySellerAccount(ws);
       magicalItems[commodity].wizards.add(dasa);
       ci.diagonAlleySellerAccount=dasa;
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
        return -1;
    }

    /**
     * Finds the apprentice buying a particular magical item.
     */    
    int findApprentice(MagicalItemInfo m)
    {
        for(int i=0; i<apprentices.size(); i++)
        {
            ApprenticeBuyer ab=apprentices.get(i);
            if(ab.getTargetCommodityInfo() == m)
                return i;
        }
        return -1;
    }

     /**
     * Register a user in Diagon Alley.
     */    
    synchronized public Everyone register(String name)
    {
        numberOfUsers++;
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
       ApprenticeBuyer ab=new ApprenticeBuyer(name);
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
           CurrentInventoryList ci=new CurrentInventoryList(0, 0, magicalItems[i]);
           ab.currentInventoryList.add(ci);
       }
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           FutureInventoryList fi=new FutureInventoryList(0, 0, magicalItems[i]);
           ab.futureInventoryList.add(fi);
       }

       FutureInventoryList fi=ab.futureInventoryList.get(commodity);
       fi.quantity=quantity;
       fi.buyingTargetPrice=cost;
       ab.futureInventoryList.add(fi);
       apprentices.add(ab);        
       DiagonAlleyBuyerAccount daba=new DiagonAlleyBuyerAccount(ab);
       magicalItems[commodity].apprentice.add(daba);
       fi.diagonAlleyBuyerAccount=daba;
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
                magicalItems[i]=new MagicalItem();
                if(br.ready())
                {
                    String line=br.readLine();
                    String elem[]=line.split(",");
                    magicalItems[i].magicalItemInfo= new MagicalItemInfo(elem[0], elem[1], elem[2]);
                    magicalItemInfo.add(magicalItems[i].magicalItemInfo);
                }

            }
        }catch(IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }        
    }
    
     /**
     * Creates a virtual wizard.
     */    
    void createVirtualWizards()
    {
     //TODO: Complete this code.   
    }

     /**
     * Creates a virtual apprentice.
     */    
    void createVirtualApprentices()
    {
     //TODO: Complete this code.   
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
        //TODO:Check if all apprentice and wizards have met their goals
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
        createMagicalWorld();
        wizards=new ArrayList();
        apprentices=new ArrayList();
        try{
            FileWriter fw=new FileWriter(LOG_FILE);
            out=new DailyProphet.EventLogger(fw, 1000);
            in=new DailyProphet.EventReader();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
            System.exit(0);
        }
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
            obj.out.write("Auction Game Begins!");
	    System.err.println("Server ready");
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
