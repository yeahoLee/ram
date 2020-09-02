
package com.mine.product.czmtr.ram.outapi.taskclient.asset;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.mine.product.czmtr.ram.outapi.taskclient.asset package.
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

    private final static QName _GetAssets_QNAME = new QName("http://assetclass.api.adam.hitina.net/", "getAssets");
    private final static QName _GetAssetsResponse_QNAME = new QName("http://assetclass.api.adam.hitina.net/", "getAssetsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.mine.product.czmtr.ram.outapi.taskclient.asset
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetAssets }
     */
    public GetAssets createGetAssets() {
        return new GetAssets();
    }

    /**
     * Create an instance of {@link GetAssetsResponse }
     */
    public GetAssetsResponse createGetAssetsResponse() {
        return new GetAssetsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssets }{@code >}}
     */
    @XmlElementDecl(namespace = "http://assetclass.api.adam.hitina.net/", name = "getAssets")
    public JAXBElement<GetAssets> createGetAssets(GetAssets value) {
        return new JAXBElement<GetAssets>(_GetAssets_QNAME, GetAssets.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssetsResponse }{@code >}}
     */
    @XmlElementDecl(namespace = "http://assetclass.api.adam.hitina.net/", name = "getAssetsResponse")
    public JAXBElement<GetAssetsResponse> createGetAssetsResponse(GetAssetsResponse value) {
        return new JAXBElement<GetAssetsResponse>(_GetAssetsResponse_QNAME, GetAssetsResponse.class, null, value);
    }

}
