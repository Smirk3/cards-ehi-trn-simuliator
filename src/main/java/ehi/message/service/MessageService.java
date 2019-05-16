package ehi.message.service;

import ehi.jaxb.generated.GetTransaction;
import ehi.message.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

@Component
public class MessageService {

    private static final Logger logger = LogManager.getLogger(MessageService.class);

    public void createMessageRequest(Message message) {
        GetTransaction getTrn = new GetTransaction();
        generateXml(getTrn);
    }

    private String generateXml(GetTransaction object) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(GetTransaction.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "");

            //jaxbMarshaller.marshal(object, file);
            jaxbMarshaller.marshal(object, System.out);
            return null;

        } catch (JAXBException e) {
            logger.error("Could not marshal object: " + e.getMessage(), e);
            throw new IllegalArgumentException();
        }
    }

}
