package ehi;

import com.google.gson.Gson;
import ehi.environment.Environment;
import ehi.environment.EnvironmentProvider;
import ehi.wiki.WikiAuthStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igorz on 2017-03-24.
 */
public class BaseController {

    private static final Logger logger = LogManager.getLogger(BaseController.class);

    protected static final String TEMPLATE = "index";
    protected static final String VIEW = "view";

    public static final String MODEL_ATTR_FORM_MODE = "formMode";

    private Gson gson = new Gson();

    @Autowired
    private EnvironmentProvider envProvider;


    protected final String jsonResponse(Object object, Class klass, HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        String json = gson.toJson(object, klass);

        try {
            response.getWriter().write(json);
            response.getWriter().flush();
            response.getWriter().close();
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
        } catch (IOException e) {
            logger.error(e);
        }

        return null;
    }

    protected List<Environment> getEnvironments(WikiAuthStatus authStatus) {
        if (authStatus.isAuthenticated()) {
            return envProvider.getEnvironments();
        } else {
            return new ArrayList<>();
        }
    }
}
