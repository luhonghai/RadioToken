package com.halosolutions.library.data;

/**
 * Created by luhonghai on 6/24/17.
 */

public class AuthToken {

    private String token;

    private String raw;

    private String areaId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
