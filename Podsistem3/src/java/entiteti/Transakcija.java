/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "transakcija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t"),
    @NamedQuery(name = "Transakcija.findByIdTransakcije", query = "SELECT t FROM Transakcija t WHERE t.idTransakcije = :idTransakcije"),
    @NamedQuery(name = "Transakcija.findBySuma", query = "SELECT t FROM Transakcija t WHERE t.suma = :suma"),
    @NamedQuery(name = "Transakcija.findByVremePlacanja", query = "SELECT t FROM Transakcija t WHERE t.vremePlacanja = :vremePlacanja")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_transakcije")
    private Integer idTransakcije;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "suma")
    private BigDecimal suma;
    @Column(name = "vreme_placanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vremePlacanja;
    @JoinColumn(name = "id_narudzbine", referencedColumnName = "id_narudzbine")
    @OneToOne
    private Narudzbina idNarudzbine;

    public Transakcija() {
    }

    public Transakcija(Integer idTransakcije) {
        this.idTransakcije = idTransakcije;
    }

    public Transakcija(Integer idTransakcije, BigDecimal suma) {
        this.idTransakcije = idTransakcije;
        this.suma = suma;
    }

    public Integer getIdTransakcije() {
        return idTransakcije;
    }

    public void setIdTransakcije(Integer idTransakcije) {
        this.idTransakcije = idTransakcije;
    }

    public BigDecimal getSuma() {
        return suma;
    }

    public void setSuma(BigDecimal suma) {
        this.suma = suma;
    }

    public Date getVremePlacanja() {
        return vremePlacanja;
    }

    public void setVremePlacanja(Date vremePlacanja) {
        this.vremePlacanja = vremePlacanja;
    }

    public Narudzbina getIdNarudzbine() {
        return idNarudzbine;
    }

    public void setIdNarudzbine(Narudzbina idNarudzbine) {
        this.idNarudzbine = idNarudzbine;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransakcije != null ? idTransakcije.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.idTransakcije == null && other.idTransakcije != null) || (this.idTransakcije != null && !this.idTransakcije.equals(other.idTransakcije))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Transakcija[ idTransakcije=" + idTransakcije + " ]";
    }
    
}
