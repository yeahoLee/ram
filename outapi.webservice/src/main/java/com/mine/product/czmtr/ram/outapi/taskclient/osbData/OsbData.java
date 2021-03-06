
package com.mine.product.czmtr.ram.outapi.taskclient.osbData;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 */
@WebService(name = "osb_data", targetNamespace = "http://org.osb.service/")
@XmlSeeAlso({
        ObjectFactory.class
})
public interface OsbData {


    /**
     * @param xmlmsg
     * @param serviceid
     * @return returns java.lang.String
     */
    @WebMethod(operationName = "get_data")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "get_data", targetNamespace = "http://org.osb.service/", className = "com.mine.product.czmtr.ram.outapi.taskclient.osbData.GetData")
    @ResponseWrapper(localName = "get_dataResponse", targetNamespace = "http://org.osb.service/", className = "com.mine.product.czmtr.ram.outapi.taskclient.osbData.GetDataResponse")
    @Action(input = "http://org.osb.service/osb_data/get_dataRequest", output = "http://org.osb.service/osb_data/get_dataResponse")
    public String getData(
            @WebParam(name = "serviceid", targetNamespace = "")
                    String serviceid,
            @WebParam(name = "xmlmsg", targetNamespace = "")
                    String xmlmsg);

}
