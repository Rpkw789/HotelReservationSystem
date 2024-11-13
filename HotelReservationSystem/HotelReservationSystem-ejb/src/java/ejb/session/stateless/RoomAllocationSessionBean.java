/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RoomAvailabilityStatusEnum;

/**
 *
 * @author ranen
 */
@Stateless
public class RoomAllocationSessionBean implements RoomAllocationSessionBeanRemote, RoomAllocationSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public void allocateWalkInRoom(RoomType roomType, Reservation reservation) {

    }

    public void allocateDailyReservation(LocalDate currentDate) {
        Query query = em.createQuery("SELECT r FROM Reservation r");
        List<Reservation> allReservations = query.getResultList();
        List<Reservation> currentDayReservations = new ArrayList<Reservation>();
        allReservations.stream().filter(r -> r.getCheckInDate().equals(currentDate)).forEach(r -> currentDayReservations.add(r));

        Set<RoomType> involvedRoomTypes = new HashSet<RoomType>();
        for (Reservation reservation : currentDayReservations) {
            involvedRoomTypes.add(reservation.getRoomType());
        }

        for (RoomType roomType : involvedRoomTypes) {
            List<Reservation> reservationsForThisRoomType = new ArrayList<Reservation>();
            currentDayReservations.stream().filter(r -> r.getRoomType().equals(roomType)).forEach(r -> reservationsForThisRoomType.add(r));

            List<Room> allRooms = roomType.getRooms();
            List<Room> availableRooms = new ArrayList<Room>();
            allRooms.stream().filter(r -> r.getAvailabilityStatus().equals(RoomAvailabilityStatusEnum.AVAILABLE) && r.getReservation() == null).forEach(r -> availableRooms.add(r));

            int roomCounter = 0;
            int reservationCounter = 0;
            for (; roomCounter < availableRooms.size() && reservationCounter < reservationsForThisRoomType.size();) {
                Reservation reservation = reservationsForThisRoomType.get(reservationCounter);
                int numberOfRooms = reservation.getNumberOfRooms();

                for (int i = 0; i < numberOfRooms; i++) {
                    Room room = availableRooms.get(roomCounter);

                    room.setReservation(reservation);
                    reservation.getGivenRooms().add(room);

                    roomCounter++;
                }
                reservationCounter++;
            }

            if (reservationCounter < reservationsForThisRoomType.size()) {
                for (int i = reservationCounter; i < reservationsForThisRoomType.size(); i++) {
                    Reservation reservation = reservationsForThisRoomType.get(i);
                    int numberOfRooms = reservation.getNumberOfRooms();
                    
                    
                }
            }
        }
    }

}
