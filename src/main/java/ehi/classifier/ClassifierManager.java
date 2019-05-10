package ehi.classifier;

import ehi.classifier.bean.Mcc;
import ehi.classifier.bean.ProcessingCode;
import ehi.classifier.bean.TransactionType;
import ehi.gps.classifier.AccountingEntryType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class ClassifierManager {

    private static final Logger logger = LogManager.getLogger(ClassifierManager.class);

    private List<Mcc> mccs;

    private List<ProcessingCode> processingCodes;

    private List<TransactionType> transactionTypes;


    public ClassifierManager() {
        try {
            this.mccs = parseMccs(new ClassPathResource("data/csv/mcc.csv"));
            this.processingCodes = parseProcessingCodes(new ClassPathResource("data/csv/processingCodes.csv"));
            this.transactionTypes = parseTransactionTypes(new ClassPathResource("data/csv/transactionTypes.csv"));

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Mcc> getMccs() {
        return mccs;
    }

    public List<ProcessingCode> getProcessingCodes() {
        return processingCodes;
    }

    public List<TransactionType> getTransactionTypes() {
        return transactionTypes;
    }

    private static List<Mcc> parseMccs(Resource resource) throws IOException {
        List<Mcc> list = new ArrayList<>();
        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.MYSQL)) {
            for (CSVRecord csvRecord : csvParser) {
                String description = csvRecord.get(1);
                String code = csvRecord.get(2);
                list.add(new Mcc(code, description));
            }
        }
        return list;
    }

    private static List<ProcessingCode> parseProcessingCodes(Resource resource) throws IOException {
        List<ProcessingCode> list = new ArrayList<>();
        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.MYSQL)) {
            for (CSVRecord csvRecord : csvParser) {
                String label = csvRecord.get(1);
                String value = csvRecord.get(2);
                AccountingEntryType accEntryType = AccountingEntryType.valueOf(csvRecord.get(3));
                list.add(new ProcessingCode(value, label, accEntryType));
            }
        }
        return list;
    }

    private static List<TransactionType> parseTransactionTypes(Resource resource) throws IOException {
        List<TransactionType> list = new ArrayList<>();
        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.MYSQL)) {
            for (CSVRecord csvRecord : csvParser) {
                String id = Integer.valueOf(csvRecord.get(0).hashCode()).toString();
                list.add(new TransactionType(id, csvRecord.get(2), csvRecord.get(1), csvRecord.get(0)));
            }
        }
        return list;
    }

}
