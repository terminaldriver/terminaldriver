package com.terminaldriver.tn5250j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface Table {

	int row() default 0;

	int column() default 0;

	int height() default 0;

	int width() default 0;
	
	int rowsPerRecord() default 0;
	
	Class<?> type();
	
}