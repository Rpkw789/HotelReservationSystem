/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import util.enumeration.PartnerRoleEnum;

/**
 *
 * @author ranen
 */

@Entity
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    @Column(unique = true, nullable = false)
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    @Size(min = 5, max = 20)
    private String username;
    @NotBlank
    @Size(min = 5, max = 20)
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(max = 50)
    private String organisationName;
    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;
    @Column(unique = true)
    @NotBlank
    @Pattern(regexp = "\\d+")
    private String mobileNumber;
    
    // Relationships
    @OneToMany
    @JoinColumn(name = "partner")
    private List<Guest> guests;
    
    private PartnerRoleEnum role;
    

    public Partner(String username, String password, String organisationName, String email, String mobileNumber, PartnerRoleEnum role) {
        this.username = username;
        this.password = password;
        this.organisationName = organisationName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.role =role;
        this.guests = new ArrayList<Guest> ();
    }

    public Partner() {
    }
    
    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Partner[ id=" + partnerId + " ]";
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the organisationName
     */
    public String getOrganisationName() {
        return organisationName;
    }

    /**
     * @param organisationName the organisationName to set
     */
    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * @param mobileNumber the mobileNumber to set
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @return the guests
     */
    public List<Guest> getGuests() {
        return guests;
    }

    /**
     * @param guests the guests to set
     */
    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }

    /**
     * @return the role
     */
    public PartnerRoleEnum getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(PartnerRoleEnum role) {
        this.role = role;
    }

}
