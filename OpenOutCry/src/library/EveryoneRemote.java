/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;
import java.io.Serializable;
import java.rmi.*;

/**
 *
 * @author Asish
 */
public interface EveryoneRemote  extends java.rmi.Remote,Serializable{
    boolean isWizard() throws RemoteException;
}
