package com.wrapper.app.infrastructure.external;

import java.util.List;

public interface DataService {

    List<?> getData(Class<?> entityName, String from);
}
