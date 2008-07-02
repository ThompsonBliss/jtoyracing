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

package net.juniorbl.jtoyracing.action.vehicle;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;
import net.juniorbl.jtoyracing.entity.vehicle.Vehicle;

/**
 * Steering action of a vehicle.
 *
 * @version 1.0 Sep 7, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public class Steer implements InputActionInterface {

	/**
	 * A vehicle.
	 */
	private Vehicle vehicle;

	/**
	 * The direction of the steer, right or left.
	 */
	private float direction;

	/**
	 * Constructs a steer action.
	 *
	 * @param vehicle the vehicle.
	 * @param direction the desired direction.
	 */
	public Steer(Vehicle vehicle, float direction) {
		this.vehicle = vehicle;
		this.direction = direction;
	}

	/**
	 * Steers the vehicle right or left.
	 *
	 * @param evt the event.
	 */
	public final void performAction(InputActionEvent evt) {
		if (evt.getTriggerPressed()) {
			vehicle.steer(direction);
		} else {
			vehicle.unsteer();
		}
	}
}
