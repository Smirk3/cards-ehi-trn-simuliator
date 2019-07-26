/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.environment;

import ehi.wiki.Login;

import java.util.List;

public interface EnvironmentProvider {

    Boolean isProviderAccessible();

    void authProvider(Login login) throws EnvProviderException;

    List<Environment> getEnvironments();

}
