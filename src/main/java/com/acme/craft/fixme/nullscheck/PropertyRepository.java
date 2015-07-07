package com.acme.craft.fixme.nullscheck;

import java.util.Optional;

public class PropertyRepository {

	public Optional<Property> get(String propertyId) {
		// sth goes wrong...
		return Optional.empty();
	}

	public void save(Property property) {
		// sth sophisticated happened...
	}
}
