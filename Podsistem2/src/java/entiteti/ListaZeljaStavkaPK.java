/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Embeddable
public class ListaZeljaStavkaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_liste")
    private int idListe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_artikla")
    private int idArtikla;

    public ListaZeljaStavkaPK() {
    }

    public ListaZeljaStavkaPK(int idListe, int idArtikla) {
        this.idListe = idListe;
        this.idArtikla = idArtikla;
    }

    public int getIdListe() {
        return idListe;
    }

    public void setIdListe(int idListe) {
        this.idListe = idListe;
    }

    public int getIdArtikla() {
        return idArtikla;
    }

    public void setIdArtikla(int idArtikla) {
        this.idArtikla = idArtikla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idListe;
        hash += (int) idArtikla;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListaZeljaStavkaPK)) {
            return false;
        }
        ListaZeljaStavkaPK other = (ListaZeljaStavkaPK) object;
        if (this.idListe != other.idListe) {
            return false;
        }
        if (this.idArtikla != other.idArtikla) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "podsistem2.ListaZeljaStavkaPK[ idListe=" + idListe + ", idArtikla=" + idArtikla + " ]";
    }
    
}
