/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.wiki;


import ehi.BaseController;
import ehi.environment.EnvProviderException;
import ehi.environment.EnvironmentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/ehi/wiki/bas/login")
public class WikiLoginController extends BaseController {

    @Autowired
    private EnvironmentProvider envProvider;

    @PostMapping("")
    public void wikiLogin(HttpServletRequest request, HttpServletResponse response,
                          @RequestBody Login login) {
        AjaxResponse ajaxResponse;
        try {
            envProvider.authProvider(login);
            new WikiAuthStatus(request).setAuthenticated(true);
            ajaxResponse = new AjaxResponse("OK");
        } catch (EnvProviderException e) {
            ajaxResponse = new AjaxResponse("ERROR", e.getMessage());
        }
        jsonResponse(ajaxResponse, AjaxResponse.class, response);
    }

    @GetMapping("/skip")
    public void wikiLoginSkip(HttpServletRequest request, HttpServletResponse response) {
        new WikiAuthStatus(request).setSkipped(true);
        jsonResponse(new AjaxResponse("OK"), AjaxResponse.class, response);
    }

    @GetMapping("/show")
    public void showWikiLogin(HttpServletRequest request, HttpServletResponse response) {
        WikiAuthStatus wikiAuthStatus = new WikiAuthStatus(request);
        Boolean show;
        if (!wikiAuthStatus.isSkipped() &&
            !wikiAuthStatus.isAuthenticated() &&
            envProvider.isProviderAccessible()){
            show = true;
        } else {
            show = false;
        }
        jsonResponse(new AjaxResponseShowWikiLogin("OK", show), AjaxResponseShowWikiLogin.class, response);
    }

}
