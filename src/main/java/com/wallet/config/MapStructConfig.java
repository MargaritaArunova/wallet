package com.wallet.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperConfig(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.WARN
)
public class MapStructConfig {

}
