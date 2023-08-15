package com.wrapper.app.infrastructure.external;

import java.util.List;

public interface DataService<T> {

    List<T> getData(Class<T> entityName, String from);
}
