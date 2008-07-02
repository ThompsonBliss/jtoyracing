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

package net.juniorbl.jtoyracing.entity.vehicle;

import java.util.ArrayList;
import java.util.List;

import net.juniorbl.jtoyracing.audio.AudioConfig;
import net.juniorbl.jtoyracing.core.Health;
import net.juniorbl.jtoyracing.core.HealthObserver;
import net.juniorbl.jtoyracing.util.ModelUtil;
import net.juniorbl.jtoyracing.util.ResourcesPath;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.audio.AudioTrack;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.material.Material;

/**
 * A vehicle of the game. May be a superclass of various kids of vehicles in the future.
 *
 * @version 1.0 Aug 11, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public class Vehicle extends Node implements Health {

	/**
	 * Maximum value of health.
	 */
	public static final int MAX_VALUE_HEALTH = 500;

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 2049305191562715341L;
	
	/**
	 * Amount of health that is decreased.
	 */
	private static final int DECREASE_HEALTH_VALUE = 25;

	/**
	 * Scale of the chassis.
	 */
	private static final float CHASSIS_SCALE = .5f;

	/**
	 * Mass of the chassis.
	 */
	private static final float CHASSIS_MASS = 40;

	/**
	 * Location of the front suspension.
	 */
	private static final Vector3f FRONT_SUSPENSION_LOCATION = new Vector3f(-.2f, -1.7f, 0);

	/**
	 * Location of the rear suspension.
	 */
	private static final Vector3f REAR_SUSPENSION_LOCATION = new Vector3f(3.2f, -1.6f, 0);

	/**
	 * Minimum value of health.
	 */
	private static final int MIN_VALUE_HEALTH = 0;

	/**
	 * Health observers.
	 */
	private List <HealthObserver> healthObservers = new ArrayList<HealthObserver>();

	/**
	 * Health of a vehicle. The health decreases with time and is recharged when a checkpoint is reached.
	 */
	private int health;

	/**
	 * Chassis of a vehicle.
	 */
	private DynamicPhysicsNode chassis;

	/**
	 * Rear suspension.
	 */
	private Suspension rearSuspension;

	/**
	 * Front suspension.
	 */
	private Suspension frontSuspension;

	/**
	 * Sound of the engine.
	 */
	private AudioTrack engineSound;

	/**
	 * Constructs a vehicle.
	 *
	 * @param physicsSpace the physicsSpace.
	 * @param chassisLocation the location of the chassis.
	 */
	public Vehicle(PhysicsSpace physicsSpace, Vector3f chassisLocation) {
		health = MAX_VALUE_HEALTH;
		createChassis(physicsSpace, chassisLocation);
		createSuspension(physicsSpace);
		loadEngineSound();
	}

	/**
	 * Creates the chassis.
	 *
	 * @param physicsSpace the physicsSpace.
	 * @param chassisLocation the location of the chassis.
	 */
	private void createChassis(PhysicsSpace physicsSpace, Vector3f chassisLocation) {
		chassis = physicsSpace.createDynamicNode();
		chassis.setLocalTranslation(chassisLocation);
		chassis.generatePhysicsGeometry();
		chassis.attachChild(
				ModelUtil.convertMultipleModelToJME(ResourcesPath.MODELS_PATH + "obj/vehicle.obj"));
		chassis.setMaterial(Material.GLASS);
		chassis.setLocalScale(CHASSIS_SCALE);
		chassis.setMass(CHASSIS_MASS);
		this.attachChild(chassis);
	}

	/**
	 * Creates the suspension.
	 *
	 * @param physicsSpace the physicsSpace.
	 */
	private void createSuspension(PhysicsSpace physicsSpace) {
		frontSuspension = new Suspension(physicsSpace, chassis, FRONT_SUSPENSION_LOCATION);
		this.attachChild(frontSuspension);
		rearSuspension = new Suspension(physicsSpace, chassis, REAR_SUSPENSION_LOCATION);
		this.attachChild(rearSuspension);
	}

	/**
	 * Accelerates the car.
	 *
	 * @param velocity value of the velocity.
	 */
	final public void accelerate(float velocity) {
		if (hasHealth()) {
			rearSuspension.accelerate(velocity);
			frontSuspension.accelerate(velocity);
			if (!engineSound.isPlaying()) {
				engineSound.play();
			}
		} else {
			engineSound.stop();
		}
	}

	/**
	 * Stops the car.
	 */
	final public void stop() {
		if (hasHealth()) {
			rearSuspension.stop();
			frontSuspension.stop();
			if (!engineSound.isPlaying()) {
				engineSound.play();
			}
		} else {
			engineSound.stop();
		}
	}

	/**
	 * Verifies whether there is health available or not.
	 *
	 * @return <code>true</code> if there is health available, <code>false</code> if there is not.
	 */
	private boolean hasHealth() {
		return health > MIN_VALUE_HEALTH;
	}

	/**
	 * Update the engine sound depending on the speed.
	 *
	 * FIXME turn up the volume is not correct
	 */
	final public void updateEngineSound() {
		engineSound.setVolume((getSpeed() / 40) + .5f);
	}

	/**
	 * Returns the current speed.
	 *
	 * @return current speed value.
	 */
	private float getSpeed() {
		return chassis.getLinearVelocity(Vector3f.ZERO).length();
	}

	/**
	 * Steers the vehicle.
	 *
	 * @param direction the desired direction.
	 */
	final public void steer(float direction) {
		frontSuspension.steer(direction);
	}

	/**
	 * Unsteer the vehicle.
	 */
	final public void unsteer() {
		frontSuspension.unsteer();
	}

	/**
	 * Returns the chassis.
	 *
	 * @return the chassis.
	 */
	final public DynamicPhysicsNode getChassis() {
		return this.chassis;
	}

//	/**
//	 * Changes the location of the vehicle.
//	 *
//	 * @param location the new location.
//	 */
//	public void changeLocation(Vector3f location) {
//		//TODO http://www.jmonkeyengine.com/jmeforum/index.php?topic=5837.msg47229#msg47229
//	}

	/**
	 * Returns the current health.
	 *
	 * @return current health.
	 */
	final public int getHealth() {
		return health;
	}

	/**
	 * Decreases the health by a value that depends on the vehicle.
	 *
	 * TODO each vehicle should decrease its own health.
	 *
	 * @return current health.
	 */
	final public int decreaseHealth() {
		health -= DECREASE_HEALTH_VALUE;
		if (!hasHealth()) {
			rearSuspension.stop();
			frontSuspension.stop();
			engineSound.stop();
			notifyObserversHealthEnded();
		}
		return health;
	}

	/**
	 * Recharge the health by the given value.
	 *
	 * @param healthAmount amount of health.
	 * @return current amount of health.
	 */
	final public int rechargeHealth(int healthAmount) {
		health = healthAmount;
		if (!engineSound.isPlaying()) {
			engineSound.play();
		}
		return health;
	}

	/**
	 * Loads the engine sound.
	 */
	private void loadEngineSound() {
		engineSound = AudioConfig.loadSoundEffect(ResourcesPath.AUDIO_PATH + "engine.ogg");
		engineSound.play();
	}

	/**
	 * {@inheritDoc}
	 */
	final public void addObserver(final HealthObserver observadorEnergia) {
		this.healthObservers.add(observadorEnergia);
	}

	/**
	 * {@inheritDoc}
	 */
	final public void removeObserver(HealthObserver observadorEnergia) {
		healthObservers.remove(observadorEnergia);
	}

	/**
	 * {@inheritDoc}
	 */
	final public void notifyObserversHealthEnded() {
		for (HealthObserver healthObserver : this.healthObservers) {
			healthObserver.healthEnded();
		}
	}
}
