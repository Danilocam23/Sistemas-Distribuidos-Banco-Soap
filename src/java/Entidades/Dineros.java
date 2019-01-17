/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

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
 * @author xcojcama
 */
@Entity
@Table(name = "dineros")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dineros.findAll", query = "SELECT d FROM Dineros d")
    , @NamedQuery(name = "Dineros.findByIDDinero", query = "SELECT d FROM Dineros d WHERE d.iDDinero = :iDDinero")
    , @NamedQuery(name = "Dineros.findByValorinicial", query = "SELECT d FROM Dineros d WHERE d.valorinicial = :valorinicial")
    , @NamedQuery(name = "Dineros.findByValoractual", query = "SELECT d FROM Dineros d WHERE d.valoractual = :valoractual")})
public class Dineros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_Dinero")
    private Integer iDDinero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Valor_inicial")
    private int valorinicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Valor_actual")
    private int valoractual;
    @JoinColumn(name = "ID_Cuentas", referencedColumnName = "ID_Cuentas")
    @ManyToOne(optional = false)
    private Cuentas iDCuentas;

    public Dineros() {
    }

    public Dineros(Integer iDDinero) {
        this.iDDinero = iDDinero;
    }

    public Dineros(Integer iDDinero, int valorinicial, int valoractual) {
        this.iDDinero = iDDinero;
        this.valorinicial = valorinicial;
        this.valoractual = valoractual;
    }

    public Integer getIDDinero() {
        return iDDinero;
    }

    public void setIDDinero(Integer iDDinero) {
        this.iDDinero = iDDinero;
    }

    public int getValorinicial() {
        return valorinicial;
    }

    public void setValorinicial(int valorinicial) {
        this.valorinicial = valorinicial;
    }

    public int getValoractual() {
        return valoractual;
    }

    public void setValoractual(int valoractual) {
        this.valoractual = valoractual;
    }

    public Cuentas getIDCuentas() {
        return iDCuentas;
    }

    public void setIDCuentas(Cuentas iDCuentas) {
        this.iDCuentas = iDCuentas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDDinero != null ? iDDinero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dineros)) {
            return false;
        }
        Dineros other = (Dineros) object;
        if ((this.iDDinero == null && other.iDDinero != null) || (this.iDDinero != null && !this.iDDinero.equals(other.iDDinero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Dineros[ iDDinero=" + iDDinero + " ]";
    }
    
}
