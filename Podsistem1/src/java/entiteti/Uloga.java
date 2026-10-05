/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import entiteti.Korisnik;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "uloga")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Uloga.findAll", query = "SELECT u FROM Uloga u"),
    @NamedQuery(name = "Uloga.findByIdUloge", query = "SELECT u FROM Uloga u WHERE u.idUloge = :idUloge"),
    @NamedQuery(name = "Uloga.findByNaziv", query = "SELECT u FROM Uloga u WHERE u.naziv = :naziv")})
public class Uloga implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_uloge")
    private Integer idUloge;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "naziv")
    private String naziv;
    @ManyToMany(mappedBy = "ulogaList")
    private List<Korisnik> korisnikList;

    public Uloga() {
    }

    public Uloga(Integer idUloge) {
        this.idUloge = idUloge;
    }

    public Uloga(Integer idUloge, String naziv) {
        this.idUloge = idUloge;
        this.naziv = naziv;
    }

    public Integer getIdUloge() {
        return idUloge;
    }

    public void setIdUloge(Integer idUloge) {
        this.idUloge = idUloge;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @XmlTransient
    public List<Korisnik> getKorisnikList() {
        return korisnikList;
    }

    public void setKorisnikList(List<Korisnik> korisnikList) {
        this.korisnikList = korisnikList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUloge != null ? idUloge.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Uloga)) {
            return false;
        }
        Uloga other = (Uloga) object;
        if ((this.idUloge == null && other.idUloge != null) || (this.idUloge != null && !this.idUloge.equals(other.idUloge))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Uloga[ idUloge=" + idUloge + " ]";
    }
    
}
