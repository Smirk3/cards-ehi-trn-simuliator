package ehi.wiki;

import ehi.wiki.AjaxResponse;

public class AjaxResponseShowWikiLogin extends AjaxResponse {

    public Boolean show;

    public AjaxResponseShowWikiLogin(String status, Boolean show) {
        super(status);
        this.show = show;
    }
}
