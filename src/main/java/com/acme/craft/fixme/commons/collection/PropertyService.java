package com.acme.craft.fixme.commons.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class PropertyService {

	
	//inicjalizacja inline
	public List<String> defaultProperties() {
//		List<String> properties = new ArrayList<>();
//		properties.add("property1");
//		properties.add("property2");
//		properties.add("property3");
//		properties.add("property4");
		
		List<String> properties = Arrays.asList("p1" ,"p2", "p3", "p4");

		return properties;
	}

	// fix nullcheck
	public boolean valid(List<String> properties) {
		if (properties != null && CollectionUtils.isEmpty(properties)) {
			boolean isValid = true;
			for (String property : properties) {
				isValid = isValid && valid(property);
			}
		}
		return false;
	}

	private boolean valid(String property) {
		return property != null && !property.isEmpty();
	}
}
