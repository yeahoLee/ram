
package com.mine.product.czmtr.ram.asset.taskclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>equipCode complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="equipCode"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IN_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="w_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "equipCode", propOrder = {
        "innum",
        "wid"
})
public class EquipCode {

    @XmlElement(name = "IN_NUM")
    protected String innum;
    @XmlElement(name = "w_ID")
    protected String wid;

    /**
     * ��ȡinnum���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getINNUM() {
        return innum;
    }

    /**
     * ����innum���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setINNUM(String value) {
        this.innum = value;
    }

    /**
     * ��ȡwid���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getWID() {
        return wid;
    }

    /**
     * ����wid���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setWID(String value) {
        this.wid = value;
    }

}
