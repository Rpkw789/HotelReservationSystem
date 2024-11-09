/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import util.enumeration.RoomAvailabilityStatusEnum;
import util.enumeration.RoomOperationalStatusEnum;

/**
 *
 * @author taniafoo
 */
@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    @NotNull
    @Column(unique = true, nullable = false)
    @Size(min=4, max=4)
    @Pattern(regexp = "\\d{4}")
    private String roomNumber;
    @NotNull
    @Column(nullable=false)
    private RoomAvailabilityStatusEnum availabilityStatus;
    @Column(nullable=false)
    private RoomOperationalStatusEnum operationalStatus;
    
    // Relationships
    @OneToOne(mappedBy = "givenRoom", optional = true)
    private Reservation reservation;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;

    public Room() {
    }

    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
        this.availabilityStatus = RoomAvailabilityStatusEnum.AVAILABLE;
        this.operationalStatus = RoomOperationalStatusEnum.ENABLED;
    }

    
    
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + roomId + " ]";
    }

    /**
     * @return the roomNumber
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * @param roomNumber the roomNumber to set
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * @return the reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
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
     * @return the availabilityStatus
     */
    public RoomAvailabilityStatusEnum getAvailabilityStatus() {
        return availabilityStatus;
    }

    /**
     * @param availabilityStatus the availabilityStatus to set
     */
    public void setAvailabilityStatus(RoomAvailabilityStatusEnum availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    /**
     * @return the operationalStatus
     */
    public RoomOperationalStatusEnum getOperationalStatus() {
        return operationalStatus;
    }

    /**
     * @param operationalStatus the operationalStatus to set
     */
    public void setOperationalStatus(RoomOperationalStatusEnum operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    
}
