package com.schaudha.config;

import com.schaudha.JobCompletionNotificationListener;
import com.schaudha.model.EmployeeContactXml;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties
public class ConverterBatchConfiguration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(EmployeeContactXml.class);
        return marshaller;
    }

    @Bean
    public Job batchJob(JobCompletionNotificationListener jobCompletionNotificationListener,
                        JobBuilderFactory jobBuilderFactory,
                        @Qualifier("readXmlFileToDb") Optional<Step> readXmlFileToDb,
                        @Qualifier("writeDbToJson") Optional<Step> writeDbToJson) {
        JobBuilder jobBuilder = jobBuilderFactory.get("Converter-Batch-job")
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener);

        List<Step> steps = new ArrayList<>();

        readXmlFileToDb.ifPresent(steps::add);
        writeDbToJson.ifPresent(steps::add);

        if(!steps.isEmpty()) {
            JobFlowBuilder jobFlowBuilder = jobBuilder.flow(steps.get(0));
            return jobFlowBuilder.next(steps.get(1)).end().build();
        }

        return null;
    }
}
