package com.schaudha.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.schaudha.mapper.EmployeeContractToJsonMapper;
import com.schaudha.model.EmployeeContactXml;
import com.schaudha.model.EmployeeContractJson;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Optional;

@Configuration
public class WriteToJsonConfiguration {
    private static final String STEP_NAME = "writeDbToJson";

    @Bean
    public Step writeDbToJson(StepBuilderFactory stepBuilderFactory,
                                @Value("${output.xml.file}") String jsonFile,
                                EntityManagerFactory entityManagerFactory,
                                Optional<EmployeeContractToJsonMapper> employeeContractToJsonMapper
                                ) {
           return stepBuilderFactory.get(STEP_NAME)
                .<EmployeeContactXml , EmployeeContractJson>chunk(200)
                .reader(readFromDb(entityManagerFactory))
                .processor(processor(employeeContractToJsonMapper.orElse(null)))
                .writer(writer(jsonFile))
                .build();
    }

    private JsonFileItemWriter<EmployeeContractJson> writer(String jsonFile) {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        return new JsonFileItemWriterBuilder<EmployeeContractJson>()
                .name("jsonWriter")
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>(objectMapper))
                .resource(new FileSystemResource(jsonFile))
                .build();
    }

    private ItemProcessor<EmployeeContactXml , EmployeeContractJson> processor(EmployeeContractToJsonMapper employeeContractToJsonMapper) {
        return employeeContractToJsonMapper::toJson;
    }

    private JpaCursorItemReader<EmployeeContactXml> readFromDb(EntityManagerFactory entityManagerFactory) {
        return new JpaCursorItemReaderBuilder<EmployeeContactXml>()
                .name("dbReader")
                .queryProvider(new JpaQueryProvider() {
                    private EntityManager entityManager;
                    @Override
                    public Query createQuery() {
                        CriteriaQuery<EmployeeContactXml> query = entityManager.getCriteriaBuilder().createQuery(EmployeeContactXml.class);
                        return entityManager.createQuery(query.select(query.from(EmployeeContactXml.class)));
                    }

                    @Override
                    public void setEntityManager(EntityManager entityManager) {
                        this.entityManager = entityManager;
                    }
                })
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
