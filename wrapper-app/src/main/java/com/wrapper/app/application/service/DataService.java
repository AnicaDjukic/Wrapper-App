package com.wrapper.app.application.service;

import java.util.List;

public interface DataService<T> {

    List<T> getData(Class<T> entityName, String from);
}
