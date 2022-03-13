package com.schaudha.mapper;

import com.schaudha.model.EmployeeContactXml;
import com.schaudha.model.EmployeeContractJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class EmployeeContractToJsonMapperDecorator implements EmployeeContractToJsonMapper {
}
