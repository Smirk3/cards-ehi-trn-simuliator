/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.message.service;

import ehi.classifier.bean.TransactionType;
import ehi.message.model.Message;
import ehi.message.model.MessageMainData;
import ehi.message.model.Response;

import java.util.List;

public interface MessageService {

    String createRequestForNewTransaction(Message message);

    String createRequestForSameTransaction(Message message);

    Response doRequest(String url, String request);

    List<MessageMainData> getMessagesMainData(Message message);
}
