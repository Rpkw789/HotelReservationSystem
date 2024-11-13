/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ranen
 */
@Entity
public class RoomAllocationExceptionRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomAllocationExceptionId;
    @Column(nullable = false)
    @NotNull
    private LocalDate date;
    @Column(nullable = false)
    @NotBlank
    private String description;
    
    // Relationships
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private Reservation affectedReservation;

    public RoomAllocationExceptionRecord(LocalDate date, String description) {
        this.date = date;
        this.description = description;
    }

    public RoomAllocationExceptionRecord() {
    }
    

    public Long getRoomAllocationExceptionId() {
        return roomAllocationExceptionId;
    }

    public void setRoomAllocationExceptionId(Long roomAllocationExceptionId) {
        this.roomAllocationExceptionId = roomAllocationExceptionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomAllocationExceptionId != null ? roomAllocationExceptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomAllocationExceptionId fields are not set
        if (!(object instanceof RoomAllocationExceptionRecord)) {
            return false;
        }
        RoomAllocationExceptionRecord other = (RoomAllocationExceptionRecord) object;
        if ((this.roomAllocationExceptionId == null && other.roomAllocationExceptionId != null) || (this.roomAllocationExceptionId != null && !this.roomAllocationExceptionId.equals(other.roomAllocationExceptionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomAllocationExceptionRecord[ id=" + roomAllocationExceptionId + " ]";
    }

    /**
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return the affectedReservation
     */
    public Reservation getAffectedReservation() {
        return affectedReservation;
    }

    /**
     * @param affectedReservation the affectedReservation to set
     */
    public void setAffectedReservation(Reservation affectedReservation) {
        this.affectedReservation = affectedReservation;
    }


    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
