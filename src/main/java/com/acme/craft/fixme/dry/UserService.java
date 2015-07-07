package com.acme.craft.fixme.dry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Collection;

public class UserService {

	public HashSet doSomethingDifferent(List<User> users) {
		HashSet<String> usrnms = new HashSet<String>();
		for (int i = users.size(); i >= 0; i--) {
			User user = users.get(i);
			
			if (user.isAdult()) {
				String temp = users.get(i).getFullName();
				usrnms.add(temp);
			}
		}
		return usrnms;
	}
}
