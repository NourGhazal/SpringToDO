package de.sellwerk.backend.helper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UpdateStatus implements Serializable {
    @JsonProperty("status")
    private  String status;


    @JsonCreator
    public UpdateStatus(String status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
