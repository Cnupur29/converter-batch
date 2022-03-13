package com.schaudha.model;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "firstName", "lastName", "emailAddress", "cellPhone" })
@XmlRootElement(name = "EmployeeContact")
@Entity
@Data
@Table(name = "employee_contract_xml")
@NamedQueries({
        @NamedQuery(name = "EmployeeContactXml.findAll", query = "select e from EmployeeContactXml e")
})
public class EmployeeContactXml {

    @Id
    @XmlAttribute(required = true)
    protected Long id;

    @XmlAttribute(required = true)
    protected String team;

    @XmlAttribute(required =true)
    protected String role;

    @XmlAttribute(required=true)
    protected String status;

    @XmlElement(name = "FirstName", required = true)
    protected String firstName;

    @XmlElement(name = "LastName", required = true)
    protected String lastName;

    @XmlElement(name = "EmailAddress", required = true)
    protected String emailAddress;

    @XmlElement(name = "CellPhone", required = true)
    protected String cellPhone;

}
