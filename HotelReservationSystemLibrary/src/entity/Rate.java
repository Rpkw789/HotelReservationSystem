/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import util.enumeration.OperationalStatusEnum;
import util.enumeration.RateTypeEnum;

/**
 *
 * @author taniafoo
 */
@Entity
public class Rate implements Serializable, Comparable<Rate> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;

    @Column(length = 50, nullable = false)
    @NotNull
    private String name;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private double ratePerNight;

    @Enumerated(EnumType.STRING)
    private RateTypeEnum rateType;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;

    @Column(nullable = true)
    private LocalDate validityStart;
    @Column(nullable = true)
    private LocalDate validityEnd;

    @Column(nullable = false)
    private OperationalStatusEnum operationalStatus = OperationalStatusEnum.ENABLED;

    @ManyToMany(mappedBy = "rates")
    private List<Reservation> reservations = new ArrayList<Reservation>();

    public Rate() {
    }

    public Rate(String name, double ratePerNight, RateTypeEnum rateType, RoomType roomType) {
        this.name = name;
        this.ratePerNight = ratePerNight;
        this.rateType = rateType;
        this.roomType = roomType;
    }

    public boolean overlaps(LocalDate startDate, LocalDate endDate) {
        if (this.validityStart.isAfter(endDate) || this.validityEnd.isBefore(startDate)) {
            return false;
        }
        return true;
    }
    
    public double calculateCost(int nights) {
        return nights * ratePerNight;
    }

    public boolean overlaps(LocalDate currentDate) {
        if ((this.validityStart.isBefore(currentDate) || this.validityStart.equals(currentDate)) && (this.validityEnd.isAfter(currentDate) || this.validityEnd.equals(currentDate))) {
            return true;
        }
        return false;
    }

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rateId != null ? rateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rateId fields are not set
        if (!(object instanceof Rate)) {
            return false;
        }
        Rate other = (Rate) object;
        if ((this.rateId == null && other.rateId != null) || (this.rateId != null && !this.rateId.equals(other.rateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Rate[ id=" + rateId + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the ratePerNight
     */
    public double getRatePerNight() {
        return ratePerNight;
    }

    /**
     * @param ratePerNight the ratePerNight to set
     */
    public void setRatePerNight(double ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    /**
     * @return the rateType
     */
    public RateTypeEnum getRateType() {
        return rateType;
    }

    /**
     * @param rateType the rateType to set
     */
    public void setRateType(RateTypeEnum rateType) {
        this.rateType = rateType;
    }

    /**
     * @return the validityStart
     */
    public LocalDate getValidityStart() {
        return validityStart;
    }

    /**
     * @param validityStart the validityStart to set
     */
    public void setValidityStart(LocalDate validityStart) {
        this.validityStart = validityStart;
    }

    /**
     * @return the validityEnd
     */
    public LocalDate getValidityEnd() {
        return validityEnd;
    }

    /**
     * @param validityEnd the validityEnd to set
     */
    public void setValidityEnd(LocalDate validityEnd) {
        this.validityEnd = validityEnd;
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
     * @return the operationalStatus
     */
    public OperationalStatusEnum getOperationalStatus() {
        return operationalStatus;
    }

    /**
     * @param operationalStatus the operationalStatus to set
     */
    public void setOperationalStatus(OperationalStatusEnum operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    /**
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    private int getPriorityValue() {
        if(rateType.equals(RateTypeEnum.PUBLISHED)) {
            return 4;
        } else if(rateType.equals(RateTypeEnum.NORMAL)) {
            return 3;
        } else if(rateType.equals(RateTypeEnum.PEAK)) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public int compareTo(Rate another) {
        return Integer.compare(this.getPriorityValue(), another.getPriorityValue());
    }

}
