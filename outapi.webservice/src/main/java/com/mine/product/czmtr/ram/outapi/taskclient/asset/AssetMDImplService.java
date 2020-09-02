package com.mine.product.czmtr.ram.outapi.taskclient.asset;

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
 * 2019-05-27T16:12:46.215+08:00
 * Generated source version: 3.2.7
 */
@WebServiceClient(name = "AssetMDImplService",
        wsdlLocation = "http://10.0.42.7:7001/czx-web/webservice/asset?wsdl",
        targetNamespace = "http://assetclass.api.adam.hitina.net/")
public class AssetMDImplService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://assetclass.api.adam.hitina.net/", "AssetMDImplService");
    public final static QName AssetMDImplPort = new QName("http://assetclass.api.adam.hitina.net/", "AssetMDImplPort");

    static {
        URL url = null;
        try {
            //动态获取方式
            ResourceBundle resource = ResourceBundle.getBundle("config.serviceIp");
            String iP = resource.getString("WZGL.IP").trim();
            url = new URL(iP + "/czx-web/webservice/asset?wsdl");


        	//生产环境地址http://10.0.52.206:7001/czx-web/webservice/asset?wsdl
        	//测试环境地址http://10.0.42.7:7001/czx-web/webservice/asset?wsdl
//            url = new URL("http://10.0.42.7:7001/czx-web/webservice/asset?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(AssetMDImplService.class.getName())
                    .log(java.util.logging.Level.INFO,
                            "Can not initialize the default wsdl from {0}", "http://10.0.42.7:7001/czx-web/webservice/asset?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public AssetMDImplService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public AssetMDImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AssetMDImplService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public AssetMDImplService(WebServiceFeature... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public AssetMDImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public AssetMDImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }


    /**
     * @return returns AssetMD
     */
    @WebEndpoint(name = "AssetMDImplPort")
    public AssetMD getAssetMDImplPort() {
        return super.getPort(AssetMDImplPort, AssetMD.class);
    }

    /**
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns AssetMD
     */
    @WebEndpoint(name = "AssetMDImplPort")
    public AssetMD getAssetMDImplPort(WebServiceFeature... features) {
        return super.getPort(AssetMDImplPort, AssetMD.class, features);
    }

}