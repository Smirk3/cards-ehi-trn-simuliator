package ehi.environment;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnvironmentProviderBas implements EnvironmentProvider {

    private static final Logger logger = LogManager.getLogger(EnvironmentProviderBas.class);

    private static final String URL = "https://wiki.bas.lt";

    public EnvironmentProviderBas() {
        Unirest.setTimeouts(10000, 10000);
    }

    @Override
    public Boolean isProviderAccessible() {
        try {
            HttpResponse<String> response = Unirest.get(URL).asString();
            return HttpStatus.SC_OK == response.getStatus();

        } catch (UnirestException e) {
            return false;
        }
    }

    @Override
    public void authProvider(String username, String password) throws EnvProviderException {
        try {
            HttpResponse<String> response = Unirest.get(URL).basicAuth(username, password).asString();
            validateResponse(response);

        } catch (UnirestException e) {
            logger.error(e, e);
            throw new EnvProviderException("System error");
        }
    }

    @Override
    public List<Environment> getEnvironments() {
        try {
            HttpResponse<String> response = Unirest.get(URL + "/display/BSC/Development+Nano+systems+-+BCLT").asString();
            validateResponse(response);
            return parseDevelopmentEnvironments(response.getBody());

        } catch (UnirestException | EnvProviderException e) {
            logger.error(e, e);
            return new ArrayList<>();
        }
    }

    private List<Environment> parseDevelopmentEnvironments(String html) {
        List<Environment> envs = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        String nanoServicesIp = resolveNanoServicesIpAddress(doc);

        Element tableRows = doc.getElementsByTag("tbody").get(0);
        List<Node> titleColumns = tableRows.childNode(0).childNodes();
        for (int i = 2; i < titleColumns.size(); i++) {
            Node column = titleColumns.get(i);
            Environment env = new Environment();
            env.name = resolveValue(column) + " (development)";
            envs.add(env);
        }

        boolean isServicesRowsStarted = false;
        for (int i = 1; i < tableRows.childNodeSize(); i++) {
            String app = resolveValue(tableRows.childNode(i).childNode(0));
            if ("Services".equals(app) || isServicesRowsStarted) {
                isServicesRowsStarted = true;
                String protocol = resolveValue(tableRows.childNode(i).childNode(0));
                if ("HTTP".equals(protocol)) {
                    for (int j = 1; j < tableRows.childNode(i).childNodeSize(); j++) {
                        Node c = tableRows.childNode(i).childNode(j);

                        String envUrl = resolveUrl(c);
                        if (StringUtils.hasText(envUrl)) {
                            envUrl = envUrl.substring(0, envUrl.indexOf("/rest/"));
                        } else {
                            String port = resolveValue(c);
                            envUrl = String.format("https://%s:%s", nanoServicesIp, port);
                        }
                        envs.get(j - 1).url = envUrl;
                    }
                    break;
                }
            }
        }

        return envs;
    }

    private String resolveNanoServicesIpAddress(Document doc) {
        Element tableRows = doc.getElementsByTag("tbody").get(1);
        for (Node row : tableRows.childNodes()) {
            if ("Nano Services".equalsIgnoreCase(resolveValue(row.childNode(3)))) {
                return resolveValue(row.childNode(2));
            }
        }
        return null;
    }

    private String resolveUrl(Node value) {
        while (value.childNodeSize() > 0 && !"a".equals(value.nodeName())) {
            value = value.childNode(0);
        }
        if ("a".equals(value.nodeName())) {
            return value.attr("href");
        } else {
            return null;
        }
    }

    private String resolveValue(Node value) {
        while (value.childNodeSize() > 0) {
            value = value.childNode(0);
        }
        if (value instanceof TextNode) {
            return ((TextNode) value).text();
        } else {
            return "undefined";
        }
    }

    private void validateResponse(HttpResponse<String> response) throws EnvProviderException {
        if (HttpStatus.SC_UNAUTHORIZED == response.getStatus()) {
            throw new EnvProviderException("Invalid credentials");
        } else if (HttpStatus.SC_OK != response.getStatus()) {
            throw new EnvProviderException("Could not connect to " + URL);
        }
    }

    public static void main(String[] args) {
        new EnvironmentProviderBas().getEnvironments();


    }
}
