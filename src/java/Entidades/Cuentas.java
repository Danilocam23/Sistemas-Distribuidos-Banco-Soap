/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
 * @author xcojcama
 */
@Entity
@Table(name = "cuentas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuentas.findAll", query = "SELECT c FROM Cuentas c")
    , @NamedQuery(name = "Cuentas.findByIDCuentas", query = "SELECT c FROM Cuentas c WHERE c.iDCuentas = :iDCuentas")
    , @NamedQuery(name = "Cuentas.findByNumerocuenta", query = "SELECT c FROM Cuentas c WHERE c.numerocuenta = :numerocuenta")
    , @NamedQuery(name = "Cuentas.findByFechacreacion", query = "SELECT c FROM Cuentas c WHERE c.fechacreacion = :fechacreacion")})
public class Cuentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_Cuentas")
    private Integer iDCuentas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "Numero_cuenta")
    private String numerocuenta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "Fecha_creacion")
    private String fechacreacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDCuentas")
    private List<Dineros> dinerosList;
    @JoinColumn(name = "ID_Usuarios", referencedColumnName = "ID_Usuarios")
    @ManyToOne(optional = false)
    private Usuarios iDUsuarios;

    public Cuentas() {
    }

    public Cuentas(Integer iDCuentas) {
        this.iDCuentas = iDCuentas;
    }

    public Cuentas(Integer iDCuentas, String numerocuenta, String fechacreacion) {
        this.iDCuentas = iDCuentas;
        this.numerocuenta = numerocuenta;
        this.fechacreacion = fechacreacion;
    }

    public Integer getIDCuentas() {
        return iDCuentas;
    }

    public void setIDCuentas(Integer iDCuentas) {
        this.iDCuentas = iDCuentas;
    }

    public String getNumerocuenta() {
        return numerocuenta;
    }

    public void setNumerocuenta(String numerocuenta) {
        this.numerocuenta = numerocuenta;
    }

    public String getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(String fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    @XmlTransient
    public List<Dineros> getDinerosList() {
        return dinerosList;
    }

    public void setDinerosList(List<Dineros> dinerosList) {
        this.dinerosList = dinerosList;
    }

    public Usuarios getIDUsuarios() {
        return iDUsuarios;
    }

    public void setIDUsuarios(Usuarios iDUsuarios) {
        this.iDUsuarios = iDUsuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDCuentas != null ? iDCuentas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuentas)) {
            return false;
        }
        Cuentas other = (Cuentas) object;
        if ((this.iDCuentas == null && other.iDCuentas != null) || (this.iDCuentas != null && !this.iDCuentas.equals(other.iDCuentas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Cuentas[ iDCuentas=" + iDCuentas + " ]";
    }
    
}
