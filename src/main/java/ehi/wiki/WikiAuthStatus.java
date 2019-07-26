/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.wiki;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WikiAuthStatus {

    private HttpSession session;

    private static final String SESSION_ATTR_WIKI_AUTH = "wiki-auth-status";

    public WikiAuthStatus(HttpServletRequest request) {
        this.session = request.getSession();
    }

    public void setAuthenticated(Boolean value){
        AuthStatus authStatus = new AuthStatus();
        authStatus.authenticated = value;
        session.setAttribute(SESSION_ATTR_WIKI_AUTH, authStatus);
    }

    public void setSkipped(Boolean value){
        AuthStatus authStatus = new AuthStatus();
        authStatus.skipped = value;
        session.setAttribute(SESSION_ATTR_WIKI_AUTH, authStatus);
    }

    public Boolean isAuthenticated(){
        AuthStatus authStatus = (AuthStatus) session.getAttribute(SESSION_ATTR_WIKI_AUTH);
        return authStatus != null && authStatus.authenticated != null && authStatus.authenticated;
    }

    public Boolean isSkipped(){
        AuthStatus authStatus = (AuthStatus) session.getAttribute(SESSION_ATTR_WIKI_AUTH);
        return authStatus != null && authStatus.skipped != null && authStatus.skipped;
    }

}
