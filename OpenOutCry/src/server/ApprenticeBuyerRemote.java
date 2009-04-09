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
public interface ApprenticeBuyerRemote extends java.rmi.Remote {
    boolean bid(int price, int quantity, int magicalItemNumber,long msec) throws RemoteException;
    boolean modifyBid(int price, int quantity, int magicalItemNumber, long msec) throws RemoteException;
}
