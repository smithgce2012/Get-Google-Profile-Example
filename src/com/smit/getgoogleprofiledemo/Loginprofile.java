

package com.smit.getgoogleprofiledemo;


/**
 * This example shows how to fetch tokens if you are creating a foreground
 * task/activity and handle auth exceptions.
 */
public class Loginprofile extends AbstractGetLoginNameTask {

	public Loginprofile(MainActivity activity, String email,
			String scope) {
		super(activity, email, scope);
	}

	
}
