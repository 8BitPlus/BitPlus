package me.themallard.bitmmo.api.analysis;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedHooks {

	public String[] fields();

	public String[] methods();
}