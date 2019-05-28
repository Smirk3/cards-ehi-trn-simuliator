package ehi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by igorz on 2017-03-24.
 */
public class BaseController {

    private static final Logger logger = LogManager.getLogger(BaseController.class);

    protected static final String TEMPLATE = "index";
    protected static final String VIEW = "view";

    public static final String MODEL_ATTR_FORM_MODE = "formMode";


    protected final String jsonResponse(Object json, HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        try {
            response.getWriter().write(json.toString());
            response.getWriter().flush();
            response.getWriter().close();
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
        } catch (IOException e) {
            logger.error(e);
        }

        return null;
    }

}
