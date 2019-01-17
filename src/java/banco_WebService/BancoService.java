/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco_WebService;

import WebServices_DB.OperacionesDB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author xcojcama
 */
@WebService(serviceName = "BancoService")
public class BancoService {

    /**
     * Web service transacion de Consiganacion tipo Soap
     */
    
    
    @WebMethod(operationName = "Transacion_Soap")
    public String Transacion_Soap(@WebParam(name = "cuenta") String cuenta, @WebParam(name = "dinero") int dinero) {
        OperacionesDB odb = new OperacionesDB();
        
        //TODO write your implementation code here:
        return odb.Transacion(cuenta, dinero);
    }
}
