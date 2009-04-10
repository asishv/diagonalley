/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;
import java.rmi.*;

/**
 *
 * @author Asish
 */
public interface EventReaderRemote extends Remote{
    String get(int nbOfChars) throws RemoteException;
}
