package ru.antisessa.mvctest.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MySpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    //обозначение файла конфигурации
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }

    //Все HTTP запросы от пользователя посылаем на DispatcherServlet
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
}
