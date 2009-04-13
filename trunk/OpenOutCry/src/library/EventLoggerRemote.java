/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;

/**
 *
 * @author Asish
 */
public interface EventLoggerRemote {
        public void write (String msg);
        
        public void writeln (String msg);
        
        public void debug (String msg);

}
