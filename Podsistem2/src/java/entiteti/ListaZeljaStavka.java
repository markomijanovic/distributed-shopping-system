/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "lista_zelja_stavka")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListaZeljaStavka.findAll", query = "SELECT l FROM ListaZeljaStavka l"),
    @NamedQuery(name = "ListaZeljaStavka.findByIdListe", query = "SELECT l FROM ListaZeljaStavka l WHERE l.listaZeljaStavkaPK.idListe = :idListe"),
    @NamedQuery(name = "ListaZeljaStavka.findByIdArtikla", query = "SELECT l FROM ListaZeljaStavka l WHERE l.listaZeljaStavkaPK.idArtikla = :idArtikla"),
    @NamedQuery(name = "ListaZeljaStavka.findByVremeDodavanja", query = "SELECT l FROM ListaZeljaStavka l WHERE l.vremeDodavanja = :vremeDodavanja")})
public class ListaZeljaStavka implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ListaZeljaStavkaPK listaZeljaStavkaPK;
    @Column(name = "vreme_dodavanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vremeDodavanja;
    @JoinColumn(name = "id_artikla", referencedColumnName = "id_artikla", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Artikal artikal;
    @JoinColumn(name = "id_liste", referencedColumnName = "id_liste", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ListaZelja listaZelja;

    public ListaZeljaStavka() {
    }

    public ListaZeljaStavka(ListaZeljaStavkaPK listaZeljaStavkaPK) {
        this.listaZeljaStavkaPK = listaZeljaStavkaPK;
    }

    public ListaZeljaStavka(int idListe, int idArtikla) {
        this.listaZeljaStavkaPK = new ListaZeljaStavkaPK(idListe, idArtikla);
    }

    public ListaZeljaStavkaPK getListaZeljaStavkaPK() {
        return listaZeljaStavkaPK;
    }

    public void setListaZeljaStavkaPK(ListaZeljaStavkaPK listaZeljaStavkaPK) {
        this.listaZeljaStavkaPK = listaZeljaStavkaPK;
    }

    public Date getVremeDodavanja() {
        return vremeDodavanja;
    }

    public void setVremeDodavanja(Date vremeDodavanja) {
        this.vremeDodavanja = vremeDodavanja;
    }

    public Artikal getArtikal() {
        return artikal;
    }

    public void setArtikal(Artikal artikal) {
        this.artikal = artikal;
    }

    public ListaZelja getListaZelja() {
        return listaZelja;
    }

    public void setListaZelja(ListaZelja listaZelja) {
        this.listaZelja = listaZelja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (listaZeljaStavkaPK != null ? listaZeljaStavkaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListaZeljaStavka)) {
            return false;
        }
        ListaZeljaStavka other = (ListaZeljaStavka) object;
        if ((this.listaZeljaStavkaPK == null && other.listaZeljaStavkaPK != null) || (this.listaZeljaStavkaPK != null && !this.listaZeljaStavkaPK.equals(other.listaZeljaStavkaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "podsistem2.ListaZeljaStavka[ listaZeljaStavkaPK=" + listaZeljaStavkaPK + " ]";
    }
    
}
