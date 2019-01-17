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
@Table(name = "usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u")
    , @NamedQuery(name = "Usuarios.findByIDUsuarios", query = "SELECT u FROM Usuarios u WHERE u.iDUsuarios = :iDUsuarios")
    , @NamedQuery(name = "Usuarios.findByNombres", query = "SELECT u FROM Usuarios u WHERE u.nombres = :nombres")
    , @NamedQuery(name = "Usuarios.findByApellidos", query = "SELECT u FROM Usuarios u WHERE u.apellidos = :apellidos")})
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_Usuarios")
    private Integer iDUsuarios;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "Nombres")
    private String nombres;
    @Size(max = 255)
    @Column(name = "Apellidos")
    private String apellidos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDUsuarios")
    private List<Cuentas> cuentasList;

    public Usuarios() {
    }

    public Usuarios(Integer iDUsuarios) {
        this.iDUsuarios = iDUsuarios;
    }

    public Usuarios(Integer iDUsuarios, String nombres) {
        this.iDUsuarios = iDUsuarios;
        this.nombres = nombres;
    }

    public Integer getIDUsuarios() {
        return iDUsuarios;
    }

    public void setIDUsuarios(Integer iDUsuarios) {
        this.iDUsuarios = iDUsuarios;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @XmlTransient
    public List<Cuentas> getCuentasList() {
        return cuentasList;
    }

    public void setCuentasList(List<Cuentas> cuentasList) {
        this.cuentasList = cuentasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDUsuarios != null ? iDUsuarios.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.iDUsuarios == null && other.iDUsuarios != null) || (this.iDUsuarios != null && !this.iDUsuarios.equals(other.iDUsuarios))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Usuarios[ iDUsuarios=" + iDUsuarios + " ]";
    }
    
}
