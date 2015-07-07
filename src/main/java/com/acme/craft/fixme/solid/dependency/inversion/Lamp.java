package com.acme.craft.fixme.solid.dependency.inversion;

import lombok.Data;

@Data
public class Lamp implements SwitchOn {

	private boolean on = false;
	private boolean pressed;
	private Lamp lamp;

	@Override
	public void Switch() {
		pressed = !pressed;
		if (pressed) {
			lamp.setOn(true);
		} else {
			lamp.setOn(false);
		}
		
	}
}
