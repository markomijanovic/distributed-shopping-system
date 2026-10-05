/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user
 */
@Entity
@Table(name = "narudzbina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Narudzbina.findAll", query = "SELECT n FROM Narudzbina n"),
    @NamedQuery(name = "Narudzbina.findByIdNarudzbine", query = "SELECT n FROM Narudzbina n WHERE n.idNarudzbine = :idNarudzbine"),
    @NamedQuery(name = "Narudzbina.findByUkupnaCena", query = "SELECT n FROM Narudzbina n WHERE n.ukupnaCena = :ukupnaCena"),
    @NamedQuery(name = "Narudzbina.findByVremeKreiranja", query = "SELECT n FROM Narudzbina n WHERE n.vremeKreiranja = :vremeKreiranja"),
    @NamedQuery(name = "Narudzbina.findByAdresaDostave", query = "SELECT n FROM Narudzbina n WHERE n.adresaDostave = :adresaDostave"),
    @NamedQuery(name = "Narudzbina.findByGradDostave", query = "SELECT n FROM Narudzbina n WHERE n.gradDostave = :gradDostave")})
public class Narudzbina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_narudzbine")
    private Integer idNarudzbine;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "ukupna_cena")
    private BigDecimal ukupnaCena;
    @Column(name = "vreme_kreiranja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vremeKreiranja;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "adresa_dostave")
    private String adresaDostave;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grad_dostave")
    private int gradDostave;
    @OneToMany(mappedBy = "idNarudzbine")
    private List<StavkaNarudzbine> stavkaNarudzbineList;
    @JoinColumn(name = "kupac", referencedColumnName = "korisnicko_ime")
    @ManyToOne
    private Korisnik kupac;
    @OneToOne(mappedBy = "idNarudzbine")
    private Transakcija transakcija;

    public Narudzbina() {
    }

    public Narudzbina(Integer idNarudzbine) {
        this.idNarudzbine = idNarudzbine;
    }

    public Narudzbina(Integer idNarudzbine, BigDecimal ukupnaCena, String adresaDostave, int gradDostave) {
        this.idNarudzbine = idNarudzbine;
        this.ukupnaCena = ukupnaCena;
        this.adresaDostave = adresaDostave;
        this.gradDostave = gradDostave;
    }

    public Integer getIdNarudzbine() {
        return idNarudzbine;
    }

    public void setIdNarudzbine(Integer idNarudzbine) {
        this.idNarudzbine = idNarudzbine;
    }

    public BigDecimal getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(BigDecimal ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    public Date getVremeKreiranja() {
        return vremeKreiranja;
    }

    public void setVremeKreiranja(Date vremeKreiranja) {
        this.vremeKreiranja = vremeKreiranja;
    }

    public String getAdresaDostave() {
        return adresaDostave;
    }

    public void setAdresaDostave(String adresaDostave) {
        this.adresaDostave = adresaDostave;
    }

    public int getGradDostave() {
        return gradDostave;
    }

    public void setGradDostave(int gradDostave) {
        this.gradDostave = gradDostave;
    }

    @XmlTransient
    public List<StavkaNarudzbine> getStavkaNarudzbineList() {
        return stavkaNarudzbineList;
    }

    public void setStavkaNarudzbineList(List<StavkaNarudzbine> stavkaNarudzbineList) {
        this.stavkaNarudzbineList = stavkaNarudzbineList;
    }

    public Korisnik getKupac() {
        return kupac;
    }

    public void setKupac(Korisnik kupac) {
        this.kupac = kupac;
    }

    public Transakcija getTransakcija() {
        return transakcija;
    }

    public void setTransakcija(Transakcija transakcija) {
        this.transakcija = transakcija;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNarudzbine != null ? idNarudzbine.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Narudzbina)) {
            return false;
        }
        Narudzbina other = (Narudzbina) object;
        if ((this.idNarudzbine == null && other.idNarudzbine != null) || (this.idNarudzbine != null && !this.idNarudzbine.equals(other.idNarudzbine))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Narudzbina[ idNarudzbine=" + idNarudzbine + " ]";
    }
    
}
