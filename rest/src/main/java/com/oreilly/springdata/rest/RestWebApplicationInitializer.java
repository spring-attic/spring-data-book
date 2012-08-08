/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oreilly.springdata.rest;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.rest.webmvc.RepositoryRestExporterServlet;
import org.springframework.data.rest.webmvc.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.ResourceProcessorInvokingHandlerAdapter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.oreilly.springdata.rest.order.web.CustomerOrdersController;

/**
 * Servlet 3.0 {@link WebApplicationInitializer} to setup both a root {@link WebApplicationContext} using the
 * {@link ApplicationConfig} class for repository setup. Beyond that it will deploy a
 * {@link RepositoryRestExporterServlet} to export the repositories defined in {@link ApplicationConfig} via a REST
 * interface.
 * 
 * @author Oliver Gierke
 */
public class RestWebApplicationInitializer implements WebApplicationInitializer {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet.ServletContext)
	 */
	public void onStartup(ServletContext container) throws ServletException {

		Dynamic filter = container.addFilter("oemivf", OpenEntityManagerInViewFilter.class);
		filter.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), false, "dispatcher");

		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(ApplicationConfig.class);

		// Manage the lifecycle of the root application context
		container.addListener(new ContextLoaderListener(rootContext));

		// Register and map the dispatcher servlet
		AnnotationConfigWebApplicationContext servletContext = new AnnotationConfigWebApplicationContext();
		servletContext.register(RepositoryRestMvcConfiguration.class, WebConfiguration.class);

		DispatcherServlet servlet = new DispatcherServlet(servletContext);
		ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", servlet);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}

	@Configuration
	@EnableWebMvc
	@ComponentScan(basePackageClasses = CustomerOrdersController.class)
	public static class WebConfiguration extends WebMvcConfigurationSupport {

		@Bean
		@Override
		public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
			RequestMappingHandlerAdapter original = super.requestMappingHandlerAdapter();
			return new ResourceProcessorInvokingHandlerAdapter(original);
		}

		@Bean
		public DomainClassConverter<?> domainClassConverter() {
			return new DomainClassConverter<FormattingConversionService>(mvcConversionService());
		}
	}
}
