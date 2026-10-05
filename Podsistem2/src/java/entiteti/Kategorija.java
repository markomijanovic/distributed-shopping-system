/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user
 */
@Entity
@Table(name = "kategorija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kategorija.findAll", query = "SELECT k FROM Kategorija k"),
    @NamedQuery(name = "Kategorija.findByIdKategorije", query = "SELECT k FROM Kategorija k WHERE k.idKategorije = :idKategorije"),
    @NamedQuery(name = "Kategorija.findByNaziv", query = "SELECT k FROM Kategorija k WHERE k.naziv = :naziv")})
public class Kategorija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_kategorije")
    private Integer idKategorije;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "naziv")
    private String naziv;
    @OneToMany(mappedBy = "idKategorije")
    private List<Artikal> artikalList;
    @OneToMany(mappedBy = "nadkategorijaId")
    private List<Kategorija> kategorijaList;
    @JoinColumn(name = "nadkategorija_id", referencedColumnName = "id_kategorije")
    @ManyToOne
    private Kategorija nadkategorijaId;

    public Kategorija() {
    }

    public Kategorija(Integer idKategorije) {
        this.idKategorije = idKategorije;
    }

    public Kategorija(Integer idKategorije, String naziv) {
        this.idKategorije = idKategorije;
        this.naziv = naziv;
    }

    public Integer getIdKategorije() {
        return idKategorije;
    }

    public void setIdKategorije(Integer idKategorije) {
        this.idKategorije = idKategorije;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @XmlTransient
    public List<Artikal> getArtikalList() {
        return artikalList;
    }

    public void setArtikalList(List<Artikal> artikalList) {
        this.artikalList = artikalList;
    }

    @XmlTransient
    public List<Kategorija> getKategorijaList() {
        return kategorijaList;
    }

    public void setKategorijaList(List<Kategorija> kategorijaList) {
        this.kategorijaList = kategorijaList;
    }

    public Kategorija getNadkategorijaId() {
        return nadkategorijaId;
    }

    public void setNadkategorijaId(Kategorija nadkategorijaId) {
        this.nadkategorijaId = nadkategorijaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKategorije != null ? idKategorije.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kategorija)) {
            return false;
        }
        Kategorija other = (Kategorija) object;
        if ((this.idKategorije == null && other.idKategorije != null) || (this.idKategorije != null && !this.idKategorije.equals(other.idKategorije))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "podsistem2.Kategorija[ idKategorije=" + idKategorije + " ]";
    }

    
    
}
