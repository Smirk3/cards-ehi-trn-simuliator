package ehi.message.service;

import ehi.classifier.bean.TransactionType;
import ehi.message.model.Message;
import ehi.message.model.Response;

public interface MessageService {

    String createRequestForNewTransaction(Message message);

    String createRequestForSameTransaction(Message message, TransactionType transactionType);

    Response doRequest(String url, String request);

}
