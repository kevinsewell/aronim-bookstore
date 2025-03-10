package com.aronim.bookstore.catalog;

import com.aronim.bookstore.catalog.application.dto.BookDto;
import com.aronim.bookstore.catalog.domain.model.Book;
import com.aronim.bookstore.catalog.infrastructure.persistence.entity.BookEntity;
import com.aronim.bookstore.platform.CorePlatformConfiguration;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.modelmapper.config.Configuration.AccessLevel.*;

@Configuration
@ComponentScan(basePackageClasses = {
        CorePlatformConfiguration.class
})
public class CatalogModuleConfiguration {

    @Bean
    public PropertyMap<?, ?> bookDtoToBookPropertyMap() {
        return new PropertyMap<BookDto, Book>() {
            @Override
            protected void configure() {
                map(source("authorFirstName"), destination("author.firstName"));
                map(source("authorLastName"), destination("author.lastName"));
                map(source("id"), destination("id.value"));
                map(source("isbn"), destination("isbn.value"));
                map(source("publisherName"), destination("publisher.name"));
                map(source("title"), destination("title.value"));
            }
        };
    }

    @Bean
    public PropertyMap<?, ?> bookToBookDtoPropertyMap() {
        return new PropertyMap<Book, BookDto>() {
            @Override
            protected void configure() {
                map(source("author.firstName"), destination("authorFirstName"));
                map(source("author.lastName"), destination("authorLastName"));
                map(source("id.value"), destination("id"));
                map(source("isbn.value"), destination("isbn"));
                map(source("publisher.name"), destination("publisherName"));
                map(source("title.value"), destination("title"));
            }
        };
    }

    @Bean
    public PropertyMap<?, ?> bookEntityToBookPropertyMap() {
        return new PropertyMap<BookEntity, Book>() {
            @Override
            protected void configure() {
                map(source("authorFirstName"), destination("author.firstName"));
                map(source("authorLastName"), destination("author.lastName"));
                map(source("id"), destination("id.value"));
                map(source("isbn"), destination("isbn.value"));
                map(source("publisherName"), destination("publisher.name"));
                map(source("title"), destination("title.value"));
            }
        };
    }

    @Bean
    public PropertyMap<?, ?> bookToBookEntityPropertyMap() {
        return new PropertyMap<Book, BookEntity>() {
            @Override
            protected void configure() {
                map(source("author.firstName"), destination("authorFirstName"));
                map(source("author.lastName"), destination("authorLastName"));
                map(source("id.value"), destination("id"));
                map(source("isbn.value"), destination("isbn"));
                map(source("publisher.name"), destination("publisherName"));
                map(source("title.value"), destination("title"));
            }
        };
    }

    @Bean
    public ModelMapper modelMapper(List<? extends PropertyMap<?, ?>> propertyMaps) {

        final ModelMapper modelMapper = new ModelMapper();
        final org.modelmapper.config.Configuration configuration = modelMapper.getConfiguration();

        configuration.setFieldAccessLevel(PRIVATE);
        configuration.setFieldMatchingEnabled(true);

        propertyMaps.forEach(modelMapper::addMappings);

        return modelMapper;
    }
}
