package ehi.environment;

import java.util.List;

public interface EnvironmentProvider {

    Boolean isProviderAccessible();

    void authProvider(String username, String password) throws EnvProviderException;

    List<Environment> getEnvironments();

}
