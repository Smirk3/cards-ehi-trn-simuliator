package ehi.classifier.bean;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMtId() {
        return mtId;
    }

    public void setMtId(String mtId) {
        this.mtId = mtId;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
