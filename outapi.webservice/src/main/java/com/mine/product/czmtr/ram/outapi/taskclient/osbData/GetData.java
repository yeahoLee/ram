
package com.mine.product.czmtr.ram.outapi.taskclient.osbData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>get_data complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="get_data">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="xmlmsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "get_data", propOrder = {
        "serviceid",
        "xmlmsg"
})
public class GetData {

    protected String serviceid;
    protected String xmlmsg;

    /**
     * ��ȡserviceid���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getServiceid() {
        return serviceid;
    }

    /**
     * ����serviceid���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setServiceid(String value) {
        this.serviceid = value;
    }

    /**
     * ��ȡxmlmsg���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getXmlmsg() {
        return xmlmsg;
    }

    /**
     * ����xmlmsg���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setXmlmsg(String value) {
        this.xmlmsg = value;
    }

}
