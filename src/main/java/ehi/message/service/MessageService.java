package ehi.message.service;

import ehi.message.model.Message;
import ehi.message.model.Response;

public interface MessageService {

    String createNewRequest(Message message);

    Response doRequest(String url, String request);

}
