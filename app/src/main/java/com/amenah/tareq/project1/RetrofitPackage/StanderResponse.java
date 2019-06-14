package com.amenah.tareq.project1.RetrofitPackage;

import com.google.gson.JsonElement;

public class StanderResponse {

    boolean status;
    JsonElement data;
    JsonElement errors;

    public StanderResponse(boolean status, JsonElement data, JsonElement errors) {
        this.status = status;
        this.data = data;
        this.errors = errors;
    }

    public boolean getStatus() {
        return status;
    }

    public JsonElement getData() {
        return data;
    }

    public JsonElement getErrors() {
        return errors;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public void setErrors(JsonElement errors) {
        this.errors = errors;
    }
}
