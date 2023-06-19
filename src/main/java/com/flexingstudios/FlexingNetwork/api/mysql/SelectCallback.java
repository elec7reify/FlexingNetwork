package com.flexingstudios.flexingnetwork.api.mysql;

import java.sql.ResultSet;

public interface SelectCallback extends Callback {
    void output(ResultSet rs) throws Exception;
}
