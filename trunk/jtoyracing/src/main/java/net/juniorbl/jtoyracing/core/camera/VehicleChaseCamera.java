/*
 * Copyright (c) 2008, JToyRacing
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <copyright holder> ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <copyright holder> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.juniorbl.jtoyracing.core.camera;

import com.jme.input.ChaseCamera;
import com.jme.input.thirdperson.ThirdPersonMouseLook;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Spatial;

import java.util.HashMap;

/**
 * Camera that follows a vehicle.
 *
 * @version 1.0 Jun 22, 2008
 * @author Fco. Carlos L. Barros Junior
 */
public final class VehicleChaseCamera extends ChaseCamera {

	/**
	 * Far distance from the car.
	 */
	private static final int FAR_DISTANCE = 50;

	/**
	 * Close distance from the car.
	 */
	private static final int CLOSE_DISTANCE = 30;

	/**
	 * Vehicle chase camera.
	 */
	private static ChaseCamera vehicleChaseCamera;

	/**
	 * Constructs a vehicle camera.
	 *
	 * @param camera the original camera.
	 * @param vehicleToFollow a vehicle to follow.
	 * @param properties the camera properties.
	 */
	private VehicleChaseCamera(Camera camera, Spatial vehicleToFollow, HashMap properties) {
		super(camera, vehicleToFollow, properties);
	}

	/**
	 * Return a instance of vehicle camera.
	 *
	 * @param camera an original camera.
	 * @param vehicleToFollow a vehicle to follow.
	 * @return instance of vehicle camera.
	 */
	public static ChaseCamera getInstance(Camera camera, Spatial vehicleToFollow) {
		HashMap properties = loadProperties();
		vehicleChaseCamera = new VehicleChaseCamera(camera, vehicleToFollow, properties);
		vehicleChaseCamera.setMaxDistance(FAR_DISTANCE);
		vehicleChaseCamera.setMinDistance(FAR_DISTANCE);
		vehicleChaseCamera.setStayBehindTarget(true);
		return vehicleChaseCamera;
	}

	/**
	 * Loads the initial properties for the camera.
	 *
	 * @return the properties loaded.
	 */
	private static HashMap<String, Object> loadProperties() {
		Vector3f targetOffset = new Vector3f();
		targetOffset.y = 4;
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put(ThirdPersonMouseLook.PROP_MAXROLLOUT, "70");
		properties.put(ThirdPersonMouseLook.PROP_MINROLLOUT, "10");
		properties.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
		properties.put(ThirdPersonMouseLook.PROP_MAXASCENT, "" + 25 * FastMath.DEG_TO_RAD);
		properties.put(ThirdPersonMouseLook.PROP_MINASCENT, "" + 10	* FastMath.DEG_TO_RAD);
		properties.put(ChaseCamera.PROP_INITIALSPHERECOORDS, new Vector3f(15, 0, 30 * FastMath.DEG_TO_RAD));
		properties.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
		return properties;
	}

	/**
	 * Modify the player's vision by changing the distance of the camera from the car.
	 */
	public void changeVision() {
		if (vehicleChaseCamera != null) {
			if (vehicleChaseCamera.getMaxDistance() == FAR_DISTANCE) {
				vehicleChaseCamera.setMaxDistance(CLOSE_DISTANCE);
				vehicleChaseCamera.setMinDistance(CLOSE_DISTANCE);
			} else {
				vehicleChaseCamera.setMaxDistance(FAR_DISTANCE);
				vehicleChaseCamera.setMinDistance(FAR_DISTANCE);
			}
		}
	}
}
