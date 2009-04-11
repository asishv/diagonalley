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
public interface ApprenticeBuyerRemote extends java.rmi.Remote {
    boolean bid(int price, int quantity, int magicalItemNumber,long msec) throws RemoteException;
    boolean modifyBid(int price, int quantity, int magicalItemNumber, long msec) throws RemoteException;
    int getScore() throws RemoteException;
    int getTargetQuantity() throws RemoteException;
    int getTargetCost() throws RemoteException;
    int getTargetQuantityLocked() throws RemoteException;
    MagicalItemInfoRemote getTargetCommodityInfo() throws RemoteException;
}
