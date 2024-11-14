/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Rate;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.OperationalStatusEnum;
import util.enumeration.RateTypeEnum;

/**
 *
 * @author ranen
 */
@Stateless
public class RoomAvailabilitySessionBean implements RoomAvailabilitySessionBeanRemote, RoomAvailabilitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<Pair<RoomType, Integer>> getAvailableRoomTypeAndNumber(LocalDate checkInDate, LocalDate checkOutDate) {
        Query query = em.createQuery("SELECT rt FROM RoomType rt");
        List<RoomType> roomTypes = query.getResultList();

        List<Pair<RoomType, Integer>> result = new ArrayList<Pair<RoomType, Integer>>();

        Query query1 = em.createQuery("SELECT r FROM Reservation r");
        List<Reservation> allReservations = query1.getResultList();

        for (RoomType roomType : roomTypes) {
            List<Reservation> overlapReservations = new ArrayList<Reservation>();
            for (Reservation reservation : allReservations) {
                if (reservation.overlaps(checkInDate, checkOutDate)) {
                    overlapReservations.add(reservation);
                }
            }

            int[] roomAvailability = new int[checkOutDate.compareTo(checkInDate)];
            for (Reservation reservation : overlapReservations) {
                LocalDate overlapStart = reservation.getCheckInDate().isBefore(checkInDate) ? checkInDate : reservation.getCheckInDate();
                LocalDate overlapEnd = reservation.getCheckOutDate().isAfter(checkOutDate) ? checkOutDate.minusDays(1) : reservation.getCheckOutDate().minusDays(1);

                if (!reservation.getGivenRooms().isEmpty()) {
                    for (LocalDate date = overlapStart; !date.isAfter(overlapEnd); date = date.plusDays(1)) {
                        int index = date.compareTo(checkInDate);

                        for (Room room : reservation.getGivenRooms()) {
                            if (room.getRoomType().equals(roomType)) {
                                System.out.println("HERE");
                                roomAvailability[index] += 1;
                            }
                        }
                    }
                } else {
                    if (reservation.getRoomType().equals(roomType)) {
                        for (LocalDate date = overlapStart; !date.isAfter(overlapEnd); date = date.plusDays(1)) {
                            int index = date.compareTo(checkInDate);
                            System.out.println("THERE");
                            roomAvailability[index] += reservation.getNumberOfRooms();
                        }
                    }
                }

            }

            int totalRooms = (int) roomType.getRooms().stream().filter(r -> r.getOperationalStatus().equals(OperationalStatusEnum.ENABLED)).count();
            int minAvailableRooms = totalRooms;
            System.out.println(roomType + ": " + Arrays.toString(roomAvailability));
            for (int roomsUsed : roomAvailability) {
                minAvailableRooms = Math.min(minAvailableRooms, totalRooms - roomsUsed);
            }

            Pair<RoomType, Integer> pair = new Pair<RoomType, Integer>(roomType, minAvailableRooms);
            result.add(pair);
        }

        return result;
    }

    @Override
    public double getCostWalkIn(LocalDate checkInDate, LocalDate checkOutDate, Long roomTypeId) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        List<Rate> rates = roomType.getRates();
        Rate publishedRate = rates.stream().filter(r -> r.getRateType().equals(RateTypeEnum.PUBLISHED)).findFirst().get();

        double totalCost = publishedRate.calculateCost(checkOutDate.compareTo(checkInDate));
        return totalCost;
    }

    public List<Rate> getRateByRoomType(LocalDate checkInDate, LocalDate checkOutDate, Long roomTypeId) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        List<Rate> rates = roomType.getRates();

        List<Rate> usedRates = new ArrayList<Rate>();
        for (LocalDate date = checkInDate; !date.isAfter(checkOutDate.minusDays(1)); date = date.plusDays(1)) {

            List<Rate> currentRates = new ArrayList<Rate>();
            for (Rate rate : rates) {
                if (rate.overlaps(date)) {
                    currentRates.add(rate);
                }
            }

            currentRates.sort(null);
            usedRates.add(currentRates.get(0));
            
            System.out.println("DATE: " + date + " == " + currentRates);
        }

        System.out.println("USEDRATES:" + usedRates.toString());
        return usedRates;
    }

    @Override
    public List<Rate> getRateByRoomTypeWalkIn(Long roomTypeId) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        List<Rate> rates = roomType.getRates();

        Rate publishedRate = rates.stream().filter(r -> r.getRateType().equals(RateTypeEnum.PUBLISHED)).findFirst().get();
        List<Rate> usedRates = new ArrayList<Rate>();

        usedRates.add(publishedRate);

        return usedRates;
    }

}
