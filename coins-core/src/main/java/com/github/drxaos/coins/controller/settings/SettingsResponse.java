package com.github.drxaos.coins.controller.settings;


import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true, chain = true)
public class SettingsResponse {
    @Expose
    String lang;
}
