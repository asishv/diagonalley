/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

/**
 *
 * @author Asish
 */
public class DiagonAlleySellerAccount  extends DiagonAlleyAccount{
    WizardSeller wizard;
    DiagonAlleySellerAccount(WizardSeller ws)
    {
        wizard=ws;
    }
}