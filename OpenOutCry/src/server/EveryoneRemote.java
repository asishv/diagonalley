/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.rmi.*;

/**
 *
 * @author Asish
 */
public interface EveryoneRemote  extends java.rmi.Remote{
    boolean isWizard() throws RemoteException;
}
