package com.schaudha.mapper;

import com.schaudha.model.EmployeeContactXml;
import com.schaudha.model.EmployeeContractJson;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
@DecoratedWith(EmployeeContractToJsonMapperDecorator.class)
public interface EmployeeContractToJsonMapper {

    @Mappings({
            @Mapping(target = "name" , expression = "java(input.getFirstName() + \" \" + input.getLastName())"),
            @Mapping(target = "email" , source = "emailAddress"),
            @Mapping(target = "phone" , source = "cellPhone"),
            @Mapping(target = "designation" , source = "role"),
            @Mapping(target = "team" , source = "team")})
    EmployeeContractJson toJson(EmployeeContactXml input);
}
