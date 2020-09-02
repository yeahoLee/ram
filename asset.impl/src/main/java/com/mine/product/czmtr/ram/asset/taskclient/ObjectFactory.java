
package com.mine.product.czmtr.ram.asset.taskclient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.mine.product.czmtr.ram.asset.taskclient package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetEquipCode_QNAME = new QName("http://getequipcodes.api.adam.hitina.net/", "getEquipCode");
    private final static QName _GetEquipCodeResponse_QNAME = new QName("http://getequipcodes.api.adam.hitina.net/", "getEquipCodeResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.mine.product.czmtr.ram.asset.taskclient
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetEquipCode }
     */
    public GetEquipCode createGetEquipCode() {
        return new GetEquipCode();
    }

    /**
     * Create an instance of {@link GetEquipCodeResponse }
     */
    public GetEquipCodeResponse createGetEquipCodeResponse() {
        return new GetEquipCodeResponse();
    }

    /**
     * Create an instance of {@link CodeBean }
     */
    public CodeBean createCodeBean() {
        return new CodeBean();
    }

    /**
     * Create an instance of {@link EquipCode }
     */
    public EquipCode createEquipCode() {
        return new EquipCode();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEquipCode }{@code >}
     *
     * @param value Java instance representing xml element's value.
     * @return the new instance of {@link JAXBElement }{@code <}{@link GetEquipCode }{@code >}
     */
    @XmlElementDecl(namespace = "http://getequipcodes.api.adam.hitina.net/", name = "getEquipCode")
    public JAXBElement<GetEquipCode> createGetEquipCode(GetEquipCode value) {
        return new JAXBElement<GetEquipCode>(_GetEquipCode_QNAME, GetEquipCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEquipCodeResponse }{@code >}
     *
     * @param value Java instance representing xml element's value.
     * @return the new instance of {@link JAXBElement }{@code <}{@link GetEquipCodeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://getequipcodes.api.adam.hitina.net/", name = "getEquipCodeResponse")
    public JAXBElement<GetEquipCodeResponse> createGetEquipCodeResponse(GetEquipCodeResponse value) {
        return new JAXBElement<GetEquipCodeResponse>(_GetEquipCodeResponse_QNAME, GetEquipCodeResponse.class, null, value);
    }

}
