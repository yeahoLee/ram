
package com.mine.product.czmtr.ram.outapi.taskclient.osbData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>get_dataResponse complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="get_dataResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="response" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "get_dataResponse", propOrder = {
        "response"
})
public class GetDataResponse {

    protected String response;

    /**
     * ��ȡresponse���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getResponse() {
        return response;
    }

    /**
     * ����response���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResponse(String value) {
        this.response = value;
    }

}
