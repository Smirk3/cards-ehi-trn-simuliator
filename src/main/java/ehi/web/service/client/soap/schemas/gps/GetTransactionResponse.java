//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.19 at 01:13:18 PM EEST 
//


package ehi.web.service.client.soap.schemas.gps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="GetTransactionResult" type="{http://tempuri.org/}ResponseMsg"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "GetTransactionResponse")
public class GetTransactionResponse {

    @XmlElement(name = "GetTransactionResult", required = true)
    protected ResponseMsg getTransactionResult;

    /**
     * Gets the value of the getTransactionResult property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseMsg }
     *     
     */
    public ResponseMsg getGetTransactionResult() {
        return getTransactionResult;
    }

    /**
     * Sets the value of the getTransactionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseMsg }
     *     
     */
    public void setGetTransactionResult(ResponseMsg value) {
        this.getTransactionResult = value;
    }

}
