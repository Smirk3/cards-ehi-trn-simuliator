package ehi.data.bean;

public class TransactionType {

    public String id;

    public String mtId;

    public String txnType;

    public String description;

    public TransactionType() {
    }

    public TransactionType(String id, String mtId, String txnType, String description) {
        this.id = id;
        this.mtId = mtId;
        this.txnType = txnType;
        this.description = description;
    }
}
