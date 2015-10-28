package org.vmtest;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by victor on 28.10.15.
 */
@Configuration
@Retention(RetentionPolicy.RUNTIME)
public @interface TestConfiguration {
    String value() default "";
}