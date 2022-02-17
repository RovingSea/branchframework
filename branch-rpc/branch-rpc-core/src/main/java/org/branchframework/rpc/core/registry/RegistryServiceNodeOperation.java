package org.branchframework.rpc.core.registry;

import java.io.IOException;

/**
 * @author Haixin Wu
 * @since 1.0
 */
public interface RegistryServiceNodeOperation {

    void register(RegistryServiceNode node) throws Exception;

    void unRegister(RegistryServiceNode node) throws Exception;

    void update(RegistryServiceNode node) throws Exception;

    void destroy() throws IOException;
}

