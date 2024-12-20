/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 *
 * @author ranen
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Column(nullable = false)
    @NotNull
    @PositiveOrZero(message = "Price must be zero or positive")
    private double fee;
    @Column(nullable = false)
    @NotNull
    private boolean checkedIn;
    @Column(nullable = false)
    @NotNull
    private LocalDate checkInDate;
    @Column(nullable = false)
    @NotNull
    private LocalDate checkOutDate;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private int numberOfRooms;

    // Relationships
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Guest guest;

    @ManyToMany
    private List<Rate> rates = new ArrayList<Rate>();

    @OneToMany(mappedBy = "reservation")
    private List<Room> givenRooms = new ArrayList<Room>();

    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;

    public Reservation() {
    }

    public Reservation(int numberOfRooms, double fee, boolean checkedIn, LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {
        this.fee = fee;
        this.checkedIn = checkedIn;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfRooms = numberOfRooms;
        this.roomType = roomType;
        this.givenRooms = new ArrayList<Room>();
    }

    public boolean overlaps(LocalDate startDate, LocalDate endDate) {
        if (this.checkInDate.isAfter(endDate) || this.checkOutDate.isBefore(startDate)) {
            return false;
        }
        return true;
    }

    public boolean overlaps(LocalDate currentDate) {
        if ((this.checkInDate.isBefore(currentDate) || this.checkInDate.equals(currentDate)) && (this.checkOutDate.isAfter(currentDate) || this.checkOutDate.equals(currentDate))) {
            return true;
        }
        return false;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }

    /**
     * @return the fee
     */
    public double getFee() {
        return fee;
    }

    /**
     * @param fee the fee to set
     */
    public void setFee(double fee) {
        this.fee = fee;
    }

    /**
     * @return the checkedIn
     */
    public boolean isCheckedIn() {
        return checkedIn;
    }

    /**
     * @param checkedIn the checkedIn to set
     */
    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    /**
     * @return the checkInDate
     */
    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    /**
     * @param checkInDate the checkInDate to set
     */
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    /**
     * @return the checkOutDate
     */
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * @param checkOutDate the checkOutDate to set
     */
    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    /**
     * @return the guest
     */
    public Guest getGuest() {
        return guest;
    }

    /**
     * @param guest the guest to set
     */
    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    /**
     * @return the roomType
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    /**
     * @return the numberOfRooms
     */
    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    /**
     * @param numberOfRooms the numberOfRooms to set
     */
    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    /**
     * @return the givenRooms
     */
    public List<Room> getGivenRooms() {
        return givenRooms;
    }

    /**
     * @param givenRooms the givenRooms to set
     */
    public void setGivenRooms(List<Room> rooms) {
        this.givenRooms = rooms;
    }

    /**
     * @return the rates
     */
    public List<Rate> getRates() {
        return rates;
    }

    /**
     * @param rates the rates to set
     */
    public void setRates(List<Rate> rate) {
        this.rates = rate;
    }

}
