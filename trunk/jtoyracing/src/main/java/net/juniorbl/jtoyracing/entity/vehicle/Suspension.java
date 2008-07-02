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

import com.jme.math.Vector3f;

import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.TranslationalJointAxis;
import com.jmex.physics.material.Material;

import static net.juniorbl.jtoyracing.entity.vehicle.Wheel.LEFT_WHEEL_SIDE;
import static net.juniorbl.jtoyracing.entity.vehicle.Wheel.RIGHT_WHEEL_SIDE;

/**
 * Suspension of a car.
 *
 * @version 1.0 Sep 3, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public class Suspension extends Node {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 160713063828231761L;

	/**
	 * Scale of the wheel base.
	 */
	private static final float WHEEL_BASE_SCALE = .1f;

	/**
	 * Location of the right wheel.
	 */
	private static final Vector3f RIGHT_WHEEL_LOCATION = new Vector3f(0, 0, -2);

	/**
	 * Location of the left wheel.
	 */
	private static final Vector3f LEFT_WHEEL_LOCATION = new Vector3f(0, 0, 2);

	/**
	 * Mass of the suspension.
	 */
	private static final float MASS = 7.5f;

	/**
	 * Max dislocation of springs.
	 */
	private static final float MAX_SPRING_DISLOCATION = 0.01f;

	/**
	 * Acceleration of the springs.
	 */
	private static final float SPRING_ACCELERATION = 1;

	/**
	 * Velocity of the springs.
	 */
	private static final float SPRING_VELOCITY = -15;

	/**
	 * Location of the left wheel base.
	 */
	private static final Vector3f LEFT_WHEEL_BASE_LOCATION = new Vector3f(0, .8f, 0f);

	/**
	 * Location of the right wheel base.
	 */
	private static final Vector3f RIGHT_WHEEL_BASE_LOCATION = new Vector3f(0, -.7f, -1f);

	/**
	 * Axis of the springs (Y axis).
	 */
	private static final Vector3f SPRING_AXIS = new Vector3f(0, 1, 0);

	/**
	 * Left wheel of the suspension.
	 */
	private Wheel leftWheel;

	/**
	 * Right wheel of the suspension.
	 */
	private Wheel rightWheel;

	/**
	 * Sustentation base for left wheel.
	 */
	private DynamicPhysicsNode leftWheelBase;

	/**
	 * Sustentation base for right wheel.
	 */
	private DynamicPhysicsNode rightWheelBase;

	/**
	 * Constructs a suspension. In order to create a suspension, it is necessary a chassis and the location of the
	 * suspension.
	 *
	 * @param physicsSpace the physicsSpace.
	 * @param chassis the chassis of the vehicle.
	 * @param location the location of the suspension.
	 */
	public Suspension(PhysicsSpace physicsSpace, DynamicPhysicsNode chassis, Vector3f location) {
		leftWheelBase = createWheelBase(physicsSpace, chassis, location.add(LEFT_WHEEL_BASE_LOCATION));
		rightWheelBase = createWheelBase(physicsSpace, chassis, location.subtract(RIGHT_WHEEL_BASE_LOCATION));
		leftWheel = new Wheel(leftWheelBase, LEFT_WHEEL_LOCATION, LEFT_WHEEL_SIDE);
		this.attachChild(leftWheel);
		rightWheel = new Wheel(rightWheelBase, RIGHT_WHEEL_LOCATION, RIGHT_WHEEL_SIDE);
		this.attachChild(rightWheel);
	}

	/**
	 * Creates a base for a wheel. A base is connected to the chassis by a joint.
	 *
	 * @param physicsSpace the physicsSpace.
	 * @param chassis the chassis of the vehicle.
	 * @param location the location of the base.
	 * @return the created base.
	 */
	private DynamicPhysicsNode createWheelBase(PhysicsSpace physicsSpace, DynamicPhysicsNode chassis,
			Vector3f location) {
		DynamicPhysicsNode wheelBase = physicsSpace.createDynamicNode();
		wheelBase.setLocalTranslation(chassis.getLocalTranslation().add(location));
		wheelBase.setLocalScale(WHEEL_BASE_SCALE);
		wheelBase.setMaterial(Material.GHOST);
		wheelBase.setMass(MASS);
		this.attachChild(wheelBase);
		Joint chassisSuspensionJoint = createChassisWheelJoint(physicsSpace, chassis, wheelBase);
		createSpring(chassisSuspensionJoint);
		return wheelBase;
	}

	/**
	 * Creates a joint which connects chassis and wheel base.
	 *
	 * @param physicsSpace the physicsSpace.
	 * @param chassis the chassis.
	 * @param wheelBase the wheel base.
	 * @return the created joint.
	 */
	private Joint createChassisWheelJoint(PhysicsSpace physicsSpace, DynamicPhysicsNode chassis,
			DynamicPhysicsNode wheelBase) {
		Joint chassisSuspensionJoint = physicsSpace.createJoint();
		chassisSuspensionJoint.attach(wheelBase, chassis);
		return chassisSuspensionJoint;
	}

	/**
	 * Creates a spring. A spring allows the suspension to move in the Y axis.
	 *
	 * @param chassisSuspensionJoint joint used to create Y axis.
	 */
	private void createSpring(Joint chassisSuspensionJoint) {
		TranslationalJointAxis spring = chassisSuspensionJoint.createTranslationalAxis();
		spring.setPositionMaximum(MAX_SPRING_DISLOCATION);
		spring.setPositionMinimum(-MAX_SPRING_DISLOCATION);
		spring.setAvailableAcceleration(SPRING_ACCELERATION);
		spring.setDesiredVelocity(SPRING_VELOCITY);
		spring.setDirection(SPRING_AXIS);
	}

	/**
	 * Accelerates the wheels.
	 *
	 * @param velocity the desired velocity.
	 */
	public final void accelerate(float velocity) {
		leftWheel.accelerate(velocity);
		rightWheel.accelerate(velocity);
	}

	/**
	 * Stops the wheels.
	 */
	public final void stop() {
		leftWheel.stop();
		rightWheel.stop();
	}

	/**
	 * Steers the wheels.
	 *
	 * @param direction the desired direction.
	 */
	public final void steer(float direction) {
		leftWheel.steer(direction);
		rightWheel.steer(direction);
	}

	/**
	 * Unsteer the wheels.
	 */
	public final void unsteer() {
		leftWheel.unsteer();
		rightWheel.unsteer();
	}
}
