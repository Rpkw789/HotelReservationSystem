/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationseclient;

import ws.partner.PartnerWebService_Service;

/**
 *
 * @author taniafoo
 */
public class HotelReservationSeClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        PartnerWebService_Service service = new PartnerWebService_Service();
        MainApp mainApp = new MainApp(service);
        mainApp.runApp();
        //service.getPartnerWebServicePort().doLogin(username, password);
    }
    
}
