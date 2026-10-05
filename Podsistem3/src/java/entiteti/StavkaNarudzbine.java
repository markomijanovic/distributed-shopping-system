/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "stavka_narudzbine")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StavkaNarudzbine.findAll", query = "SELECT s FROM StavkaNarudzbine s"),
    @NamedQuery(name = "StavkaNarudzbine.findByIdStavke", query = "SELECT s FROM StavkaNarudzbine s WHERE s.idStavke = :idStavke"),
    @NamedQuery(name = "StavkaNarudzbine.findByIdArtikla", query = "SELECT s FROM StavkaNarudzbine s WHERE s.idArtikla = :idArtikla"),
    @NamedQuery(name = "StavkaNarudzbine.findByKolicina", query = "SELECT s FROM StavkaNarudzbine s WHERE s.kolicina = :kolicina"),
    @NamedQuery(name = "StavkaNarudzbine.findByJedinicnaCena", query = "SELECT s FROM StavkaNarudzbine s WHERE s.jedinicnaCena = :jedinicnaCena")})
public class StavkaNarudzbine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_stavke")
    private Integer idStavke;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_artikla")
    private int idArtikla;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kolicina")
    private int kolicina;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "jedinicna_cena")
    private BigDecimal jedinicnaCena;
    @JoinColumn(name = "id_narudzbine", referencedColumnName = "id_narudzbine")
    @ManyToOne
    private Narudzbina idNarudzbine;

    public StavkaNarudzbine() {
    }

    public StavkaNarudzbine(Integer idStavke) {
        this.idStavke = idStavke;
    }

    public StavkaNarudzbine(Integer idStavke, int idArtikla, int kolicina, BigDecimal jedinicnaCena) {
        this.idStavke = idStavke;
        this.idArtikla = idArtikla;
        this.kolicina = kolicina;
        this.jedinicnaCena = jedinicnaCena;
    }

    public Integer getIdStavke() {
        return idStavke;
    }

    public void setIdStavke(Integer idStavke) {
        this.idStavke = idStavke;
    }

    public int getIdArtikla() {
        return idArtikla;
    }

    public void setIdArtikla(int idArtikla) {
        this.idArtikla = idArtikla;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public BigDecimal getJedinicnaCena() {
        return jedinicnaCena;
    }

    public void setJedinicnaCena(BigDecimal jedinicnaCena) {
        this.jedinicnaCena = jedinicnaCena;
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
        hash += (idStavke != null ? idStavke.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StavkaNarudzbine)) {
            return false;
        }
        StavkaNarudzbine other = (StavkaNarudzbine) object;
        if ((this.idStavke == null && other.idStavke != null) || (this.idStavke != null && !this.idStavke.equals(other.idStavke))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.StavkaNarudzbine[ idStavke=" + idStavke + " ]";
    }
    
}
