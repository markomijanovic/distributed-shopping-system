/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
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
@Table(name = "korpa_stavka")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KorpaStavka.findAll", query = "SELECT k FROM KorpaStavka k"),
    @NamedQuery(name = "KorpaStavka.findByIdStavke", query = "SELECT k FROM KorpaStavka k WHERE k.idStavke = :idStavke"),
    @NamedQuery(name = "KorpaStavka.findByKolicina", query = "SELECT k FROM KorpaStavka k WHERE k.kolicina = :kolicina")})
public class KorpaStavka implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_stavke")
    private Integer idStavke;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kolicina")
    private int kolicina;
    @JoinColumn(name = "id_artikla", referencedColumnName = "id_artikla")
    @ManyToOne
    private Artikal idArtikla;
    @JoinColumn(name = "korisnicko_ime", referencedColumnName = "korisnicko_ime")
    @ManyToOne
    private Korpa korisnickoIme;

    public KorpaStavka() {
    }

    public KorpaStavka(Integer idStavke) {
        this.idStavke = idStavke;
    }

    public KorpaStavka(Integer idStavke, int kolicina) {
        this.idStavke = idStavke;
        this.kolicina = kolicina;
    }

    public Integer getIdStavke() {
        return idStavke;
    }

    public void setIdStavke(Integer idStavke) {
        this.idStavke = idStavke;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public Artikal getIdArtikla() {
        return idArtikla;
    }

    public void setIdArtikla(Artikal idArtikla) {
        this.idArtikla = idArtikla;
    }

    public Korpa getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(Korpa korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
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
        if (!(object instanceof KorpaStavka)) {
            return false;
        }
        KorpaStavka other = (KorpaStavka) object;
        if ((this.idStavke == null && other.idStavke != null) || (this.idStavke != null && !this.idStavke.equals(other.idStavke))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "podsistem2.KorpaStavka[ idStavke=" + idStavke + " ]";
    }
    
}
