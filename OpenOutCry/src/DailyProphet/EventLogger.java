/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DailyProphet;

/**
 *
 * @author Asish
 */
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;


public class EventLogger {	
	@SuppressWarnings("serial")
	class ClosedLogException extends Exception{}
        private final int maxBuffSize; //The maximum size of the buffer, declared as final to prevent extending(child) classes from modifying the Buffer Size 
        private final Writer writer; //Writer object, declared as final to prevent extending(child) classes from modifying the Writer 
        private boolean closeFlag; //If log is closed then closeFlag=true else closeFlag=false
        private String buffer[]; //buffer that contains messages which needs to be written into Writer 
	private int size; //size of the buffer
        private int in; //index where new messages are placed 
        private int out; //index where messages are read
        LazyWriter lw; //Object of LazyWriter

        //Constructor
	public EventLogger(Writer writer, int maxBuffSize) {
		this.writer = writer;
		this.maxBuffSize = maxBuffSize;
                closeFlag=false;
                buffer = new String[maxBuffSize];
                size=0; //Initially buffer is empty
                in=0;
                out=0;
                lw=null; //Lazy Writer thread not started here to prevent data races.
	}
	
        /*
        * Only called by lazy writer thread.  Blocks if the log has not been closed and there is nothing to write.
        * If the log has been closed and the log is empty, return null, otherwise, remove and return the first item 
        * in the log buffer.
        */
	 synchronized private String get()
	 {
            String msg=null;
            //await((closeFlag = false) v (size > 0))
            while(!(closeFlag == true || size > 0)) 
                try{ wait(); } catch(InterruptedException ie){ie.printStackTrace();}
            if(size>0)
            {
               msg=buffer[out]; //Take mesage out of buffer
               out=(out+1)%maxBuffSize;
               size--;
               notifyAll(); //Wake up all waiting threads

            }
            return msg;
         }
	

        /*  Method called by application program to add a message to the log.  Blocks if necessary to maintain 
        *    the buffer size invariant.  If the log is closed before the method is called, or while it is blocked, a 
        *    LogClosedException is thrown.  This method is also responsible for lazily instantiating a writer thread *    to, at low priority, write the contents of the Log buffer to the writer passed into the constructor.
        */

        synchronized private void add(String msg) throws ClosedLogException
        {
            if(lw==null) //Start the lazy writer, if no lazy writer thread exists. It is started first for better performance.
            {
                 lw=new LazyWriter(); //Create a new instance of lazy writer
                 lw.setPriority(Thread.MIN_PRIORITY); //Set the lowest priority for the lazy writer thread
                 lw.start();          //Start the lazy writer thread          
            }
            //await ((closeFlag=false) v (size<maxBuffSize))
            while(!(closeFlag==true || size<maxBuffSize)) 
                try{ wait(); }catch(InterruptedException ie) {ie.printStackTrace();}
            if(closeFlag) //assert(!closeFlag)
                throw new ClosedLogException();
            buffer[in]=msg; //Put message in buffer
            in=(in+1)%maxBuffSize;
            size++;
            notifyAll(); //Wake up all waiting threads
        }
	
        public void write (String msg)
        {
            try{
                add(msg);
            }catch (ClosedLogException cle)
            {
                cle.printStackTrace();
            }
        }

        /*  close the Log.  After the log has been closed, no additional message may be added to the log. */
         public synchronized void close()
         {
             closeFlag=true;
             notifyAll(); //Wake up all waiting threads
         }



        /*  Thread that copies data written to Log buffer to the writer passed into the constructor.  
         *  After the Log has been closed and the buffer is empty, the thread should flush
         *  and close the writer and then 
         *  terminate.  
        */
	class LazyWriter extends Thread {                
            
		public void run() 
                {
                    String msg;
                    try{
                        do{
                            msg=get(); //Get the next message from the buffer
                            if(msg != null)                         
                                writer.write(msg);
                        }while(msg != null); //Get messages till msg == null
                        writer.flush(); //Flush the writer
                        writer.close(); //Close the writer
                    }
                    catch(IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                }
        }		
}

