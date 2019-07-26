/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.message.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

public class XmlObjectUtil {

    private static final Logger logger = LogManager.getLogger(XmlObjectUtil.class);

    public static Object toObject(String value, Class klass) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(klass);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return unmarshaller.unmarshal(new ByteArrayInputStream(value.getBytes("UTF-8")));

        } catch (JAXBException | UnsupportedEncodingException e) {
            logger.error("Could not marshal object: " + e.getMessage(), e);
            throw new IllegalArgumentException();
        }
    }

    public static String toXml(Object object, Class klass) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(klass);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "");

            StringOutputStream output = new StringOutputStream();
            jaxbMarshaller.marshal(object, output);
            String xml = output.toString();

            xml = xml.replaceAll(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
            xml = xml.replaceAll(" xsi:schemaLocation=\"\"", "");

            return xml;

        } catch (JAXBException e) {
            logger.error("Could not marshal object: " + e.getMessage(), e);
            throw new IllegalArgumentException();
        }
    }


}
