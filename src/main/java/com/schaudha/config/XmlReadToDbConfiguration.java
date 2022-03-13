package com.schaudha.config;

import com.schaudha.model.EmployeeContactXml;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.persistence.EntityManagerFactory;

@Configuration
public class XmlReadToDbConfiguration {
    private static final String STEP_NAME = "readXmlFileToDb";



    @Bean
    public Step readXmlFileToDb(StepBuilderFactory stepBuilderFactory,
                                @Value("${input.xml.file}") String xmlFile,
                                EntityManagerFactory entityManagerFactory,
                                Jaxb2Marshaller marshaller
                                ) {

        return stepBuilderFactory.get(STEP_NAME)
                .<EmployeeContactXml , EmployeeContactXml>chunk(200)
                .reader(xmlFileReader(xmlFile, marshaller))
                .writer(writeToDb(entityManagerFactory))
                .build();
    }

    private JpaItemWriter<EmployeeContactXml> writeToDb(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<EmployeeContactXml>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    private StaxEventItemReader<EmployeeContactXml> xmlFileReader(String xmlFile, Jaxb2Marshaller xmlMarshaller) {
        return new StaxEventItemReaderBuilder<EmployeeContactXml>()
                .name("xmlFileReader")
                .resource(new FileSystemResource(xmlFile))
                .encoding("UTF-8")
                .addFragmentRootElements("EmployeeContact")
                .unmarshaller(xmlMarshaller)
                .build();
    }
}
