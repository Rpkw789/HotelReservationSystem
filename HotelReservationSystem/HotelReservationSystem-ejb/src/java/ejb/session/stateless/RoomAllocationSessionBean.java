/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomAllocationExceptionRecord;
import entity.RoomType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
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

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public void allocateWalkInRoom(RoomType roomType, Reservation reservation) {

    }

    @Override
    public void allocateDailyReservation(LocalDate currentDate) {
        Query query = em.createQuery("SELECT r FROM Reservation r");
        List<Reservation> allReservations = query.getResultList();
        List<Reservation> currentDayReservations = new ArrayList<Reservation>();
        allReservations.stream().filter(r -> r.getCheckInDate().equals(currentDate) && r.getGivenRooms().size() != r.getNumberOfRooms()).forEach(r -> currentDayReservations.add(r));
        // r.getGivenRooms().size() != r.getNumberOfRooms()

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

            outer:
            for (; roomCounter < availableRooms.size() && reservationCounter < reservationsForThisRoomType.size();) {
                Reservation reservation = reservationsForThisRoomType.get(reservationCounter);
                int numberOfRooms = reservation.getNumberOfRooms();

                for (int i = reservation.getGivenRooms().size(); i < numberOfRooms; i++) { //int i = 0;
                    Room room = availableRooms.get(roomCounter);

                    room.setReservation(reservation);
                    reservation.getGivenRooms().add(room);

                    roomCounter++;
                    if (roomCounter >= availableRooms.size()) {
                        break outer;
                    }
                }
                reservationCounter++;
            }
            

            // EXCEPTIONS
            List<RoomType> filteredRoomType = new ArrayList<RoomType>();
            List<RoomType> roomTypes = roomTypeSessionBean.getAllRoomType();
            roomTypes.stream().filter(rt -> rt.isEnabled()).forEach(rt -> filteredRoomType.add(rt));

            if (reservationCounter < reservationsForThisRoomType.size()) {
                outerity:
                for (int i = reservationCounter; i < reservationsForThisRoomType.size(); i++) {
                    Reservation reservation = reservationsForThisRoomType.get(i);
                    int numberOfRooms = reservation.getNumberOfRooms();
                    int numberOfRoomsLeft = numberOfRooms - reservation.getGivenRooms().size();

                    RoomType unavailableRoomType = reservation.getRoomType();
                    
                    outery:
                    for (int j = numberOfRoomsLeft; j > 0; j--) {
                        RoomType currentRoomType = unavailableRoomType;
                        while (currentRoomType.getNextHigherRoomType() != null) {
                            currentRoomType = currentRoomType.getNextHigherRoomType();

                            List<Room> roomsies = currentRoomType.getRooms();
                            List<Room> updatedAvailableRooms = new ArrayList<Room>();
                            roomsies.stream().filter(r -> r.getAvailabilityStatus().equals(RoomAvailabilityStatusEnum.AVAILABLE) && r.getReservation() == null).forEach(r -> updatedAvailableRooms.add(r));

                            if (!updatedAvailableRooms.isEmpty()) {
                                Room room = updatedAvailableRooms.get(0);
                                room.setReservation(reservation);
                                reservation.getGivenRooms().add(room);
                                RoomAllocationExceptionRecord record = new RoomAllocationExceptionRecord(currentDate, "1 Room Upgraded From " + unavailableRoomType.getName() + " To " + currentRoomType.getName());
                                record.setAffectedReservation(reservation);
                                em.persist(record);
                                numberOfRoomsLeft--;
                                continue outery;
                            }
                        }
                        RoomAllocationExceptionRecord record = new RoomAllocationExceptionRecord(currentDate, "No Available Rooms for " + numberOfRoomsLeft + " Rooms");
                        record.setAffectedReservation(reservation);
                        em.persist(record);
                        continue outerity;
                    }
                }
            }
        }
    }
    
    @Override
    public List<RoomAllocationExceptionRecord> getRoomAllocationExceptionRecord(LocalDate date) {
        Query query = em.createQuery("SELECT r FROM RoomAllocationExceptionRecord r WHERE r.date = :date");
        query.setParameter("date", date);
        List<RoomAllocationExceptionRecord> list = query.getResultList();
        for(RoomAllocationExceptionRecord r : list) {
            r.getAffectedReservation().getGivenRooms().size();
            r.getAffectedReservation().getRoomType();
        }
        return list;
    }

}
