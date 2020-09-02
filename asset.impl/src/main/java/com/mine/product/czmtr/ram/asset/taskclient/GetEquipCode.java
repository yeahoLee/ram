
package com.mine.product.czmtr.ram.asset.taskclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>getEquipCode complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="getEquipCode"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="arg0" type="{http://getequipcodes.api.adam.hitina.net/}codeBean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getEquipCode", propOrder = {
        "arg0"
})
public class GetEquipCode {

    protected CodeBean arg0;

    /**
     * ��ȡarg0���Ե�ֵ��
     *
     * @return possible object is
     * {@link CodeBean }
     */
    public CodeBean getArg0() {
        return arg0;
    }

    /**
     * ����arg0���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link CodeBean }
     */
    public void setArg0(CodeBean value) {
        this.arg0 = value;
    }

}
