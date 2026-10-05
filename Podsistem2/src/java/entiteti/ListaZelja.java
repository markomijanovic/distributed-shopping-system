/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user
 */
@Entity
@Table(name = "lista_zelja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListaZelja.findAll", query = "SELECT l FROM ListaZelja l"),
    @NamedQuery(name = "ListaZelja.findByIdListe", query = "SELECT l FROM ListaZelja l WHERE l.idListe = :idListe"),
    @NamedQuery(name = "ListaZelja.findByDatumKreiranja", query = "SELECT l FROM ListaZelja l WHERE l.datumKreiranja = :datumKreiranja")})
public class ListaZelja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_liste")
    private Integer idListe;
    @Column(name = "datum_kreiranja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumKreiranja;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "listaZelja")
    private List<ListaZeljaStavka> listaZeljaStavkaList;
    @JoinColumn(name = "korisnicko_ime", referencedColumnName = "korisnicko_ime")
    @OneToOne
    private Korisnik korisnickoIme;

    public ListaZelja() {
    }

    public ListaZelja(Integer idListe) {
        this.idListe = idListe;
    }

    public Integer getIdListe() {
        return idListe;
    }

    public void setIdListe(Integer idListe) {
        this.idListe = idListe;
    }

    public Date getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(Date datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }

    @XmlTransient
    public List<ListaZeljaStavka> getListaZeljaStavkaList() {
        return listaZeljaStavkaList;
    }

    public void setListaZeljaStavkaList(List<ListaZeljaStavka> listaZeljaStavkaList) {
        this.listaZeljaStavkaList = listaZeljaStavkaList;
    }

    public Korisnik getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(Korisnik korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idListe != null ? idListe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListaZelja)) {
            return false;
        }
        ListaZelja other = (ListaZelja) object;
        if ((this.idListe == null && other.idListe != null) || (this.idListe != null && !this.idListe.equals(other.idListe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "podsistem2.ListaZelja[ idListe=" + idListe + " ]";
    }
    
}
