package com.aronim.bookstore.catalog;

import com.aronim.bookstore.platform.CorePlatformConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        CorePlatformConfiguration.class
})
public class CatalogModuleConfiguration {
}
