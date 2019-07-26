/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.environment;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import ehi.wiki.Login;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnvironmentProviderBas implements EnvironmentProvider {

    private static final Logger logger = LogManager.getLogger(EnvironmentProviderBas.class);

    private static final String URL = "https://wiki.bas.lt";
    private static final String WSDL_URI = "/soap/GetTransaction?wsdl";

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
    public void authProvider(Login login) throws EnvProviderException {
        try {
            HttpResponse<String> response = Unirest.get(URL).basicAuth(login.user, login.password).asString();
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
                List<Environment> envs = parseDevEnvironments(response.getBody());

                response = Unirest.get(URL + "/display/BSC/Test+Nano+sistemos+MTST+Telia+DC").asString();
                validateResponse(response);
                envs.addAll(parseTestEnvironments(response.getBody()));
                envs.add(localhostUrl());

                if (!CollectionUtils.isEmpty(envs)){
                    Environment addCustom = new Environment();
                    addCustom.url = "ADD";
                    addCustom.name = "Add custom url";
                    envs.add(addCustom);
                }
                return envs;

        } catch (UnirestException | EnvProviderException e) {
            logger.error(e, e);
            return new ArrayList<>();
        }
    }

    private List<Environment> parseTestEnvironments(String html) {
        List<Environment> envs = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        String nanoServicesIp = resolveTestNanoServicesIpAddress(doc);

        Element tableRows = doc.getElementsByTag("tbody").get(0);
        List<Node> titleColumns = tableRows.childNode(0).childNodes();
        for (int i = 2; i < titleColumns.size(); i++) {
            Node column = titleColumns.get(i);
            Environment env = new Environment();
            env.name = resolveValue(column) + " (test)";
            envs.add(env);
        }

        boolean isServicesRowsStarted = false;
        for (int i = 1; i < tableRows.childNodeSize(); i++) {
            String app = resolveValue(tableRows.childNode(i).childNode(0));
            if ("Services".equals(app) || isServicesRowsStarted) {
                isServicesRowsStarted = true;
                String protocol = resolveValue(tableRows.childNode(i).childNode(1));
                if ("HTTP".equals(protocol)) {
                    for (int j = 2; j < tableRows.childNode(i).childNodeSize(); j++) {
                        Node c = tableRows.childNode(i).childNode(j);

                        String envUrl = resolveUrl(c);
                        if (StringUtils.hasText(envUrl) && envUrl.indexOf("/rest/") != -1) {
                            envUrl = envUrl.substring(0, envUrl.indexOf("/rest/"));
                        } else if (StringUtils.hasText(envUrl) && envUrl.startsWith("http")) {
                            envUrl = envUrl.endsWith("/") ? envUrl.substring(0, envUrl.length() - 1) : envUrl;
                        } else {
                            String port = resolveValue(c);
                            envUrl = String.format("https://%s:%s", nanoServicesIp, port);
                        }
                        envs.get(j - 2).name = envUrl + "  - " + envs.get(j - 2).name;
                        envs.get(j - 2).url = envUrl + WSDL_URI;
                    }
                    break;
                }
            }
        }

        return envs;
    }

    private List<Environment> parseDevEnvironments(String html) {
        List<Environment> envs = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        String nanoServicesIp = resolveDevNanoServicesIpAddress(doc);

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
                        envs.get(j - 1).name = envUrl + "  - " + envs.get(j - 1).name;
                        envs.get(j - 1).url = envUrl + WSDL_URI;
                    }
                    break;
                }
            }
        }

        return envs;
    }

    private String resolveTestNanoServicesIpAddress(Document doc) {
        Element tableRows = doc.getElementsByTag("tbody").get(1);
        for (Node row : tableRows.childNodes()) {
            if ("Nano Services".equalsIgnoreCase(resolveValue(row.childNode(5)))) {
                return resolveValue(row.childNode(2));
            }
        }
        return null;
    }

    private String resolveDevNanoServicesIpAddress(Document doc) {
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

    private Environment localhostUrl() {
        Environment env = new Environment();
        String url = "http://localhost:6060";
        env.url = url + WSDL_URI;
        env.name = url + " (local)";
        return env;
    }
}
