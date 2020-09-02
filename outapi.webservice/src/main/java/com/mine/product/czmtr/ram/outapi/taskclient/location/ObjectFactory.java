
package com.mine.product.czmtr.ram.outapi.taskclient.location;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.mine.product.czmtr.ram.outapi.taskclient.location package.
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

    private final static QName _GetLocations_QNAME = new QName("http://location.api.adam.hitina.net/", "getLocations");
    private final static QName _GetLocationsResponse_QNAME = new QName("http://location.api.adam.hitina.net/", "getLocationsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.mine.product.czmtr.ram.outapi.taskclient.location
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetLocations }
     */
    public GetLocations createGetLocations() {
        return new GetLocations();
    }

    /**
     * Create an instance of {@link GetLocationsResponse }
     */
    public GetLocationsResponse createGetLocationsResponse() {
        return new GetLocationsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLocations }{@code >}}
     */
    @XmlElementDecl(namespace = "http://location.api.adam.hitina.net/", name = "getLocations")
    public JAXBElement<GetLocations> createGetLocations(GetLocations value) {
        return new JAXBElement<GetLocations>(_GetLocations_QNAME, GetLocations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLocationsResponse }{@code >}}
     */
    @XmlElementDecl(namespace = "http://location.api.adam.hitina.net/", name = "getLocationsResponse")
    public JAXBElement<GetLocationsResponse> createGetLocationsResponse(GetLocationsResponse value) {
        return new JAXBElement<GetLocationsResponse>(_GetLocationsResponse_QNAME, GetLocationsResponse.class, null, value);
    }

}
