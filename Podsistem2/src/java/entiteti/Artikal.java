/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "artikal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Artikal.findAll", query = "SELECT a FROM Artikal a"),
    @NamedQuery(name = "Artikal.findByIdArtikla", query = "SELECT a FROM Artikal a WHERE a.idArtikla = :idArtikla"),
    @NamedQuery(name = "Artikal.findByNaziv", query = "SELECT a FROM Artikal a WHERE a.naziv = :naziv"),
    @NamedQuery(name = "Artikal.findByCena", query = "SELECT a FROM Artikal a WHERE a.cena = :cena"),
    @NamedQuery(name = "Artikal.findByPopust", query = "SELECT a FROM Artikal a WHERE a.popust = :popust")})
public class Artikal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_artikla")
    private Integer idArtikla;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "naziv")
    private String naziv;
    @Lob
    @Size(max = 65535)
    @Column(name = "opis")
    private String opis;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cena")
    private BigDecimal cena;
    @Column(name = "popust")
    private BigDecimal popust;
    @OneToMany(mappedBy = "idArtikla")
    private List<KorpaStavka> korpaStavkaList;
    @JoinColumn(name = "id_kategorije", referencedColumnName = "id_kategorije")
    @ManyToOne
    private Kategorija idKategorije;
    @JoinColumn(name = "prodavac", referencedColumnName = "korisnicko_ime")
    @ManyToOne
    private Korisnik prodavac;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artikal")
    private List<ListaZeljaStavka> listaZeljaStavkaList;

    public Artikal() {
    }

    public Artikal(Integer idArtikla) {
        this.idArtikla = idArtikla;
    }

    public Artikal(Integer idArtikla, String naziv, BigDecimal cena) {
        this.idArtikla = idArtikla;
        this.naziv = naziv;
        this.cena = cena;
    }

    public Integer getIdArtikla() {
        return idArtikla;
    }

    public void setIdArtikla(Integer idArtikla) {
        this.idArtikla = idArtikla;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public BigDecimal getPopust() {
        return popust;
    }

    public void setPopust(BigDecimal popust) {
        this.popust = popust;
    }

    @XmlTransient
    public List<KorpaStavka> getKorpaStavkaList() {
        return korpaStavkaList;
    }

    public void setKorpaStavkaList(List<KorpaStavka> korpaStavkaList) {
        this.korpaStavkaList = korpaStavkaList;
    }

    public Kategorija getIdKategorije() {
        return idKategorije;
    }

    public void setIdKategorije(Kategorija idKategorije) {
        this.idKategorije = idKategorije;
    }

    public Korisnik getProdavac() {
        return prodavac;
    }

    public void setProdavac(Korisnik prodavac) {
        this.prodavac = prodavac;
    }

    @XmlTransient
    public List<ListaZeljaStavka> getListaZeljaStavkaList() {
        return listaZeljaStavkaList;
    }

    public void setListaZeljaStavkaList(List<ListaZeljaStavka> listaZeljaStavkaList) {
        this.listaZeljaStavkaList = listaZeljaStavkaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArtikla != null ? idArtikla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artikal)) {
            return false;
        }
        Artikal other = (Artikal) object;
        if ((this.idArtikla == null && other.idArtikla != null) || (this.idArtikla != null && !this.idArtikla.equals(other.idArtikla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "podsistem2.Artikal[ idArtikla=" + idArtikla + " ]";
    }
    
}
