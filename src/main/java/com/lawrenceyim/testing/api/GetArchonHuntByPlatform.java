package com.lawrenceyim.testing.api;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.apitools.builder.NotStringValuesProcessor;
import com.zebrunner.carina.utils.config.Configuration;

public class GetArchonHuntByPlatform extends AbstractApiMethodV2 {
    public GetArchonHuntByPlatform(String platform) {
        super(null, "api/get_archon_hunt_by_platform_response.json");
        replaceUrlPlaceholder("base_url", Configuration.getRequired("base_url"));
        replaceUrlPlaceholder("platform", platform);
        ignorePropertiesProcessor(NotStringValuesProcessor.class);
    }
}
