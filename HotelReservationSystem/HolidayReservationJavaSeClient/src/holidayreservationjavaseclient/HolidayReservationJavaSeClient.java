/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package holidayreservationjavaseclient;

import java.util.Scanner;
import ws.partner.Partner;
import ws.partner.PartnerNotFoundException_Exception;
import ws.partner.PartnerWebService_Service;

/**
 *
 * @author taniafoo
 */
public class HolidayReservationJavaSeClient {

    private static PartnerWebService_Service service = new PartnerWebService_Service();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(service);
        mainApp.run();

    }

        
    

}
