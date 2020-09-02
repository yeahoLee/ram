
package com.mine.product.czmtr.ram.outapi.taskclient.materials;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.mine.product.czmtr.ram.outapi.taskclient.materials package.
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

    private final static QName _GetMaterials_QNAME = new QName("http://materials.api.adam.hitina.net/", "getMaterials");
    private final static QName _GetMaterialsResponse_QNAME = new QName("http://materials.api.adam.hitina.net/", "getMaterialsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.mine.product.czmtr.ram.outapi.taskclient.materials
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetMaterials }
     */
    public GetMaterials createGetMaterials() {
        return new GetMaterials();
    }

    /**
     * Create an instance of {@link GetMaterialsResponse }
     */
    public GetMaterialsResponse createGetMaterialsResponse() {
        return new GetMaterialsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMaterials }{@code >}}
     */
    @XmlElementDecl(namespace = "http://materials.api.adam.hitina.net/", name = "getMaterials")
    public JAXBElement<GetMaterials> createGetMaterials(GetMaterials value) {
        return new JAXBElement<GetMaterials>(_GetMaterials_QNAME, GetMaterials.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMaterialsResponse }{@code >}}
     */
    @XmlElementDecl(namespace = "http://materials.api.adam.hitina.net/", name = "getMaterialsResponse")
    public JAXBElement<GetMaterialsResponse> createGetMaterialsResponse(GetMaterialsResponse value) {
        return new JAXBElement<GetMaterialsResponse>(_GetMaterialsResponse_QNAME, GetMaterialsResponse.class, null, value);
    }

}
