/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DailyProphet;
import java.io.*;
/**
 *
 * @author Asish
 */
public class EventReader implements library.EventReaderRemote{
    public static final String LOG_FILE="DiagonAlleyLog.txt";
    public String get(int nbOfChars)
    {
        String message=null;
        char cbuf[]=new char[1000];
        try{
            FileReader fr=new FileReader(LOG_FILE);
            fr.skip(nbOfChars);
            if(fr.ready())
            {
                fr.read(cbuf);
                message=cbuf.toString();
            }
            fr.close();
        }catch(IOException ie)
        {
            
        }
        return message;
    }
}
