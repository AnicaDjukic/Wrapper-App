package com.wrapper.app.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "OrganizacioneJedinice")
public class Departman extends OrganizacionaJedinica {
}
