
package com.mine.product.czmtr.ram.asset.taskclient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>codeBean complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="codeBean"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CODEBEAN" type="{http://getequipcodes.api.adam.hitina.net/}equipCode" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="NOTICE_NO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NOTICE_OS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ORG_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="USER_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "codeBean", propOrder = {
        "codebean",
        "noticeno",
        "noticeos",
        "orgid",
        "userid"
})
public class CodeBean {

    @XmlElement(name = "CODEBEAN", nillable = true)
    protected List<EquipCode> codebean;
    @XmlElement(name = "NOTICE_NO")
    protected String noticeno;
    @XmlElement(name = "NOTICE_OS")
    protected String noticeos;
    @XmlElement(name = "ORG_ID")
    protected String orgid;
    @XmlElement(name = "USER_ID")
    protected String userid;

    /**
     * Gets the value of the codebean property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codebean property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCODEBEAN().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EquipCode }
     */
    public List<EquipCode> getCODEBEAN() {
        if (codebean == null) {
            codebean = new ArrayList<EquipCode>();
        }
        return this.codebean;
    }

    /**
     * ��ȡnoticeno���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getNOTICENO() {
        return noticeno;
    }

    /**
     * ����noticeno���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNOTICENO(String value) {
        this.noticeno = value;
    }

    /**
     * ��ȡnoticeos���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getNOTICEOS() {
        return noticeos;
    }

    /**
     * ����noticeos���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNOTICEOS(String value) {
        this.noticeos = value;
    }

    /**
     * ��ȡorgid���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getORGID() {
        return orgid;
    }

    /**
     * ����orgid���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setORGID(String value) {
        this.orgid = value;
    }

    /**
     * ��ȡuserid���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getUSERID() {
        return userid;
    }

    /**
     * ����userid���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUSERID(String value) {
        this.userid = value;
    }

}
