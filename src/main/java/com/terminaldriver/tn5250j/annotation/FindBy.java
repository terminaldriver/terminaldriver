package com.terminaldriver.tn5250j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
public @interface FindBy {

	How how() default How.UNSET;

	String using() default "";

	String labelText() default "";

	String text() default "";

	String name() default "";

	int row() default 0;

	int column() default 0;

	int length() default 0;

	ScreenAttribute attribute() default ScreenAttribute.UNSET;

}