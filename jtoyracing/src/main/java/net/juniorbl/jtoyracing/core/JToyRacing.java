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

package net.juniorbl.jtoyracing.core;

import static net.juniorbl.jtoyracing.entity.vehicle.Vehicle.MAX_VALUE_HEALTH;
import net.juniorbl.jtoyracing.action.camera.CameraPositionHandler;
import net.juniorbl.jtoyracing.action.vehicle.Steer;
import net.juniorbl.jtoyracing.action.vehicle.Traction;
import net.juniorbl.jtoyracing.audio.AudioConfig;
import net.juniorbl.jtoyracing.core.camera.VehicleChaseCamera;
import net.juniorbl.jtoyracing.core.hud.Info;
import net.juniorbl.jtoyracing.entity.environment.Room;
import net.juniorbl.jtoyracing.entity.vehicle.Vehicle;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.CullState;
import com.jmex.physics.util.SimplePhysicsGame;

/**
 * Start point of the game.
 *
 * @version 1.0 Aug 11, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public final class JToyRacing extends SimplePhysicsGame implements ChronometerObserver, HealthObserver {

	/**
	 * Direction of left steer.
	 */
	private static final int LEFT_STEER_DIRECTION = -100;

	/**
	 * Direction of right steer.
	 */
	private static final int RIGHT_STEER_DIRECTION = 100;

	/**
	 * Velocity of the forward traction.
	 */
	private static final int FORWARD_TRACTION_VELOCITY = 50;

	/**
	 * Velocity of the backward traction.
	 */
	private static final int BACKWARD_TRACTION_VELOCITY = -50;

	/**
	 * Amount of seconds that a vehicle has to stop when its health ends.
	 */
	private static final int WAIT_SECONDS = 3;

	/**
	 * Location of the light.
	 */
	private static final Vector3f LIGHT_LOCATION = new Vector3f(0, 40, 0);

	/**
	 * Location of the vehicle in the race track.
	 */
	private static final float VEHICLE_RACE_TRACK_LOCATION = 28.2f;

	/**
	 * Location of the camera.
	 */
	private static final Vector3f CAMERA_LOCATION = new Vector3f(-64, -7, 124);

	/**
	 * Normal gravity.
	 */
	private static final Vector3f NORMAL_GRAVITY = new Vector3f(0, -45, 0);

	/**
	 * Amount of health that will be recharged when a vehicle stops.
	 */
	private static final int RECHARGE_HEALTH = 250;

	/**
	 * A camera that chave a vehicle.
	 */
	private VehicleChaseCamera vehicleChaseCamera;

	/**
	 * Vehicle of the game.
	 *
	 * TODO today there is only one vehicle, the future versions are going to have more.
	 */
	private Vehicle vehicle;

	/**
	 * Environment of the game.
	 *
	 * TODO today there is only one environment, the future versions are going to have more,
	 * like kitchen, garden and so on.
	 */
	private Room room;

	/**
	 * Information displayed on the screen.
	 */
	private Info info;

	/**
	 * Thread which monitors the health of the vehicles.
	 */
	private HealthMonitor healthMonitor;

	/**
	 * Chronometer that runs every time a vehicle stops because lack of health.
	 */
	private HealthChronometer healthChronometer;

	/**
	 * Audio configuration.
	 */
	private AudioConfig audio;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void simpleInitGame() {
		loadOptimization();
		loadEnvironment();
		loadVehicles();
		loadGravitation();
		loadCamera();
		loadKeyboardControllers();
		loadInfo();
		loadAudio();
		loadHealthMonitor();
	}

	/**
	 * Thread that runs to monitors the health of the vehicles.
	 */
	private void loadHealthMonitor() {
		healthMonitor = new HealthMonitor(this);
		healthMonitor.start();
	}

	/**
	 * Loads the audio configuration.
	 */
	private void loadAudio() {
		this.audio = new AudioConfig(cam);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void simpleUpdate() {
		super.simpleUpdate();
		vehicleChaseCamera.update(tpf);
		audio.update();
		updateVehicleEngineSound();
	}

	/**
	 * Loads the sound of the engines of the vehicles.
	 *
	 * TODO in the future get all the vehicles.
	 */
	private void updateVehicleEngineSound() {
		vehicle.updateEngineSound();
	}

	/**
	 * Update the health of the vehicles.
	 *
	 * TODO in the future get all the vehicles.
	 */
	public void updateVehiclesHealth() {
		// When a vehicle reach a checkpoint, its health is recharged
		if (room.isVehicleReachedCheckpoint(vehicle.getWorldBound())) {
			info.setHealthBarValue(vehicle.rechargeHealth(MAX_VALUE_HEALTH));
		}
		info.setHealthBarValue(vehicle.decreaseHealth());
	}

	/**
	 * Loads the vehicles of the game.
	 */
	private void loadVehicles() {
		float florHeight = room.getFloorHeight() - VEHICLE_RACE_TRACK_LOCATION;
		Vector3f vehicleLocation = new Vector3f(-40, florHeight, 120);
		vehicle = new Vehicle(getPhysicsSpace(), vehicleLocation);
		vehicle.addObserver(this);

//		SpatialTransformer rotationController = new SpatialTransformer();
//		rotationController.setObject(vehicle, 0, -1);
//		Quaternion rotation = new Quaternion();
//		rotation.fromAngleAxis(FastMath.DEG_TO_RAD * 180, new Vector3f(0, 0, 1));
//		rotationController.setRotation(0, 2, rotation);
//		vehicle.addController(rotationController);

		rootNode.attachChild(vehicle);
	}

	/**
	 * Loads the gravitation.
	 */
	private void loadGravitation() {
		getPhysicsSpace().setDirectionalGravity(NORMAL_GRAVITY);
	}

	/**
	 * Loads the room where the race takes place.
	 *
	 * FIXME in the future erase this method, use only loadEnvironment.
	 */
	private void loadRoom() {
		room = new Room(getPhysicsSpace(), display.getRenderer());
		room.attachChild(vehicle);
		rootNode.attachChild(room);
	}

	/**
	 * Start point.
	 */
	public static void main(String[] args) {
		JToyRacing game = new JToyRacing();
		game.setDialogBehaviour(NEVER_SHOW_PROPS_DIALOG);
		game.start();
	}

	/**
	 * Load optimization.
	 */
	private void loadOptimization() {
		// TODO change the key to display debug mode, now is 'V'.
		//showPhysics = true;
		// Don't display the back of the triangles.
		CullState cullState = display.getRenderer().createCullState();
		cullState.setCullMode(CullState.CS_BACK);
		rootNode.setRenderState(cullState);
		// Disable the default light source.
		// lightState.setEnabled(false);
	}

	/**
	 * Loads the keyboard controllers in order to control the vehicles.
	 */
	private void loadKeyboardControllers() {
		//FIXME isn't a vehicle responsible for its own traction and steer?
		input.addAction(new Traction(vehicle, BACKWARD_TRACTION_VELOCITY), InputHandler.DEVICE_KEYBOARD,
				KeyInput.KEY_UP, InputHandler.AXIS_NONE, false);
		input.addAction(new Traction(vehicle, FORWARD_TRACTION_VELOCITY), InputHandler.DEVICE_KEYBOARD,
				KeyInput.KEY_DOWN, InputHandler.AXIS_NONE, false);
		input.addAction(new Steer(vehicle, LEFT_STEER_DIRECTION),
				InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_LEFT,
				InputHandler.AXIS_NONE, false);
		input.addAction(new Steer(vehicle, RIGHT_STEER_DIRECTION),
				InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_RIGHT,
				InputHandler.AXIS_NONE, false);

		CameraPositionHandler cameraPositionHandler = new CameraPositionHandler(vehicleChaseCamera);
		input.addAction(cameraPositionHandler, InputHandler.DEVICE_KEYBOARD,
				KeyInput.KEY_V, InputHandler.AXIS_NONE, false);
	}

	/**
	 * Loads screen information system.
	 */
	private void loadInfo() {
		info = new Info(display.getRenderer(), vehicle.getHealth());
		// Use fpsNone node to display the characters.
		fpsNode.attachChild(info);
	}

	/**
	 * Loads the camera which will follow the vehicle.
	 */
	private void loadCamera() {
		cam.setLocation(CAMERA_LOCATION);
		vehicleChaseCamera = (VehicleChaseCamera) VehicleChaseCamera.getInstance(cam, vehicle.getChassis());
	}

	/**
	 * Loads the environment where the race takes place.
	 *
	 * FIXME move this code to Room class
	 */
	private void loadEnvironment() {
		loadRoom();
		//Light
		PointLight light = new PointLight();
		light.setLocation(LIGHT_LOCATION);
		//Shadow
		light.setAmbient(ColorRGBA.gray);
		//Light color
		light.setDiffuse(ColorRGBA.white);
		//Reflex color
		light.setSpecular(ColorRGBA.lightGray);
		light.setEnabled(true);
		// lightState.detachAll();
		lightState.attach(light);
		rootNode.setRenderState(lightState);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateTime(int seconds) {
		// TODO use internalization
		info.printMessage("Time left: " + String.valueOf(seconds) + " seconds");
	}

	/**
	 * {@inheritDoc}
	 */
	public void timeUP() {
		info.printMessage(String.valueOf(""));
		info.setHealthBarValue(vehicle.rechargeHealth(RECHARGE_HEALTH));
		healthChronometer.stopChronometer();
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateHealth() {
		updateVehiclesHealth();
	}

	/**
	 * {@inheritDoc}
	 */
	public void healthEnded() {
		//TODO player that wins the challenge at the beginning may win one more second
		healthChronometer = new HealthChronometer(WAIT_SECONDS);
		healthChronometer.addObserver(this);
		healthChronometer.start();
	}
}
