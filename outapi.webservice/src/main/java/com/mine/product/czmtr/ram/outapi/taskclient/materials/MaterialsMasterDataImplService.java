package com.mine.product.czmtr.ram.outapi.taskclient.materials;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class was generated by Apache CXF 3.2.7
 * 2019-05-27T16:14:07.914+08:00
 * Generated source version: 3.2.7
 */
@WebServiceClient(name = "MaterialsMasterDataImplService",
        wsdlLocation = "http://10.0.42.7:7001/czx-web/webservice/materials?wsdl",
        targetNamespace = "http://materials.api.adam.hitina.net/")
public class MaterialsMasterDataImplService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://materials.api.adam.hitina.net/", "MaterialsMasterDataImplService");
    public final static QName MaterialsMasterDataImplPort = new QName("http://materials.api.adam.hitina.net/", "MaterialsMasterDataImplPort");

    static {
        URL url = null;
        try {
            //动态获取方式
            ResourceBundle resource = ResourceBundle.getBundle("config.serviceIp");
            String iP = resource.getString("WZGL.IP").trim();
            url = new URL(iP + "/czx-web/webservice/materials?wsdl");

        	//生产环境地址http://10.0.52.206:7001/czx-web/webservice/materials?wsdl
        	//测试环境地址http://10.0.42.7:7001/czx-web/webservice/materials?wsdl
//            url = new URL("http://10.0.42.7:7001/czx-web/webservice/materials?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(MaterialsMasterDataImplService.class.getName())
                    .log(java.util.logging.Level.INFO,
                            "Can not initialize the default wsdl from {0}", "http://10.0.42.7:7001/czx-web/webservice/materials?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public MaterialsMasterDataImplService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public MaterialsMasterDataImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MaterialsMasterDataImplService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public MaterialsMasterDataImplService(WebServiceFeature... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public MaterialsMasterDataImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public MaterialsMasterDataImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }


    /**
     * @return returns MaterialsMasterData
     */
    @WebEndpoint(name = "MaterialsMasterDataImplPort")
    public MaterialsMasterData getMaterialsMasterDataImplPort() {
        return super.getPort(MaterialsMasterDataImplPort, MaterialsMasterData.class);
    }

    /**
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns MaterialsMasterData
     */
    @WebEndpoint(name = "MaterialsMasterDataImplPort")
    public MaterialsMasterData getMaterialsMasterDataImplPort(WebServiceFeature... features) {
        return super.getPort(MaterialsMasterDataImplPort, MaterialsMasterData.class, features);
    }

}