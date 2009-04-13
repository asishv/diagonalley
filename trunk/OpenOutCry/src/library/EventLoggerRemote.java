/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;
import java.io.Serializable;

/**
 *
 * @author Asish
 */
public interface EventLoggerRemote extends Serializable{
        public void write (String msg);
        
        public void writeln (String msg);
        
        public void debug (String msg);

}
