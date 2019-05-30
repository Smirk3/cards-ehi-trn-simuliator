package ehi.environment;

import ehi.wiki.Login;

import java.util.List;

public interface EnvironmentProvider {

    Boolean isProviderAccessible();

    void authProvider(Login login) throws EnvProviderException;

    List<Environment> getEnvironments();

}
