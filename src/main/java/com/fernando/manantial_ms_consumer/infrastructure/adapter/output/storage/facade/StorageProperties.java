package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "storage")
@Getter
@Setter
@NoArgsConstructor
public class StorageProperties {
    private String type;
    private String path;
}
