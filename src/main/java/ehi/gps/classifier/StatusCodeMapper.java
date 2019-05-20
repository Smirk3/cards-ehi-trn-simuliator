package ehi.gps.classifier;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class StatusCodeMapper {

    private static final Map<String, String> STATUS;

    public static final String STATUS_CODE_SUCCESS = "00";

    static {
        STATUS = new HashMap<>();
        STATUS.put(STATUS_CODE_SUCCESS, "All Good");
        STATUS.put("01", "Refer to card issuer");
        STATUS.put("03", "Invalid merchant");
        STATUS.put("04", "capture card");
        STATUS.put("05", "Do not honor");
        STATUS.put("06", "Unspecified Error");
        STATUS.put("08", "Honor with identification");
        STATUS.put("10", "Partial Approval");
        STATUS.put("12", "Invalid transaction");
        STATUS.put("13", "Invalid amount");
        STATUS.put("14", "Invalid card number (no such number)");
        STATUS.put("15", "Unable to route at IEM");
        STATUS.put("17", "Customer Cancellation");
        STATUS.put("30", "Format error");
        STATUS.put("31", "Issuer sign-off");
        STATUS.put("32", "Completed Partially");
        STATUS.put("33", "Expired card");
        STATUS.put("36", "Restricted card");
        STATUS.put("37", "Card acceptor call acquirer security");
        STATUS.put("38", "Allowable PIN tries exceeded");
        STATUS.put("41", "Lost card (Capture)");
        STATUS.put("43", "Stolen card (Capture)");
        STATUS.put("51", "Insufficient funds");
        STATUS.put("54", "Expired card");
        STATUS.put("55", "Incorrect PIN");
        STATUS.put("57", "Transaction not permitted to cardholder");
        STATUS.put("58", "Transaction not permitted to terminal");
        STATUS.put("61", "Exceeds withdrawal amount limit");
        STATUS.put("62", "Restricted card");
        STATUS.put("63", "Security Violation");
        STATUS.put("64", "Original amount incorrect");
        STATUS.put("65", "Exceeds withdrawal frequency limit");
        STATUS.put("66", "Card acceptor call acquirer’s security department");
        STATUS.put("67", "Card to be picked up at ATM");
        STATUS.put("68", "Response received too late");
        STATUS.put("70", "Cardholder to contact issuer");
        STATUS.put("71", "PIN not changed");
        STATUS.put("75", "Allowable number of PIN tries exceeded");
        STATUS.put("76", "Wrong PIN, allowable number of PIN tries exceeded");
        STATUS.put("77", "Issuer does not participate in the service");
        STATUS.put("78", "Account balance unavailable");
        STATUS.put("79", "Unacceptable PIN – Transaction declined Retry");
        STATUS.put("80", "Network error");
        STATUS.put("81", "Foreign network failure");
        STATUS.put("82", "Timeout at IEM");
        STATUS.put("83", "Card Destroyed");
        STATUS.put("85", "PIN Unblock request");
        STATUS.put("86", "PIN validation not possible");
        STATUS.put("87", "Purchase Amount Only, No Cash Back Allowed");
        STATUS.put("88", "Cryptographic failure");
        STATUS.put("89", "Authentication failure");
        STATUS.put("91", "Issuer or switch is inoperative");
        STATUS.put("92", "Unable to route at AEM");
        STATUS.put("94", "Duplicate transmission");
        STATUS.put("95", "Reconcile error");
        STATUS.put("96", "System malfunction");
        STATUS.put("98", "Refund given to Customer");
        STATUS.put("99", "Card Voided");
        STATUS.put("N7", "Decline for CVV2 failure");
        STATUS.put("P5", "PIN Change/Unblock request declined");
        STATUS.put("P6", "Unsafe PIN");
    }

    public static String getText(String code){
        if (!StringUtils.hasText(code)) {
            throw new IllegalArgumentException("Status code can not be empty.");
        } else if (!STATUS.containsKey(code)){
            throw new IllegalArgumentException(String.format("Unknown status code: %s", code));
        } else {
            return STATUS.get(code);
        }
    }
}
