/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import util.enumeration.RateTypeEnum;

/**
 *
 * @author taniafoo
 */
@Entity
public class Rate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;
    
    @Column(length=50, nullable=false)
    @NotNull
    private String name;
    
    @NotNull
    @PositiveOrZero
    @Column(nullable=false)
    private double ratePerNight;
    
    @Enumerated(EnumType.STRING)
    private RateTypeEnum rateType;
    
    @NotNull
    @Column(nullable=false)
    private Date validityStart;
    @NotNull
    @Column(nullable=false)
    private Date validityEnd;

    public Rate() {
    }

    public Rate(String name, double ratePerNight, RateTypeEnum rateType, Date validityStart, Date validityEnd) {
        this.name = name;
        this.ratePerNight = ratePerNight;
        this.rateType = rateType;
        this.validityStart = validityStart;
        this.validityEnd = validityEnd;
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
    public Date getValidityStart() {
        return validityStart;
    }

    /**
     * @param validityStart the validityStart to set
     */
    public void setValidityStart(Date validityStart) {
        this.validityStart = validityStart;
    }

    /**
     * @return the validityEnd
     */
    public Date getValidityEnd() {
        return validityEnd;
    }

    /**
     * @param validityEnd the validityEnd to set
     */
    public void setValidityEnd(Date validityEnd) {
        this.validityEnd = validityEnd;
    }
    
}
