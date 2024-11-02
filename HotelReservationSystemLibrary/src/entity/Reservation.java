/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
    private Date checkInDate;
    @Column(nullable = false)
    @NotNull
    private Date checkOutDate;
    
    // Relationships
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Guest guest;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private Rate rate;
    
    @OneToOne
    private Room givenRoom;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType chosenRoomType;

    public Reservation() {
    }

    public Reservation(double fee, boolean checkedIn, Date checkInDate, Date checkOutDate) {
        this.fee = fee;
        this.checkedIn = checkedIn;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
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
    public Date getCheckInDate() {
        return checkInDate;
    }

    /**
     * @param checkInDate the checkInDate to set
     */
    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    /**
     * @return the checkOutDate
     */
    public Date getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * @param checkOutDate the checkOutDate to set
     */
    public void setCheckOutDate(Date checkOutDate) {
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
     * @return the rate
     */
    public Rate getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(Rate rate) {
        this.rate = rate;
    }

    /**
     * @return the givenRoom
     */
    public Room getGivenRoom() {
        return givenRoom;
    }

    /**
     * @param givenRoom the givenRoom to set
     */
    public void setGivenRoom(Room givenRoom) {
        this.givenRoom = givenRoom;
    }

    /**
     * @return the chosenRoomType
     */
    public RoomType getChosenRoomType() {
        return chosenRoomType;
    }

    /**
     * @param chosenRoomType the chosenRoomType to set
     */
    public void setChosenRoomType(RoomType chosenRoomType) {
        this.chosenRoomType = chosenRoomType;
    }

}
