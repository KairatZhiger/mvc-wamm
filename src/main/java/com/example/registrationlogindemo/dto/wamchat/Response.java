package com.example.registrationlogindemo.dto.wamchat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By Kairat Zhiger
 * at 15.06.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    @JsonProperty("err")
    private Integer err;
    @JsonProperty("result")
    private String result;
    @JsonProperty("msg_id")
    private Long msgId;
}
