package com.flexingstudios.FlexingNetwork.api.mysql;

import java.sql.ResultSet;

public interface SelectCallback extends Callback {
    void done(ResultSet paramResulSet) throws Exception;
}
