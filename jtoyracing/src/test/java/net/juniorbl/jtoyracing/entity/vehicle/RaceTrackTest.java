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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import junit.framework.Assert;
import mockit.Mockit;
import net.juniorbl.jtoyracing.entity.environment.RaceTrack;
import net.juniorbl.jtoyracing.util.ModelUtil;
import net.juniorbl.jtoyracing.util.ModelUtilForTest;
import net.juniorbl.jtoyracing.util.StaticPhysicsNodeImpl;

import org.junit.Before;
import org.junit.Test;

import com.jme.math.Vector3f;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.TextureState;
import com.jmex.physics.PhysicsSpace;

/**
 * Tests for RaceTrack.
 * 
 * TODO not finished yet.
 * 
 * @version 1.0 Jun 21, 2008
 * @author Fco. Carlos L. Barros Junior
 */
public class RaceTrackTest {
	
	/**
	 * The location for the vehicle and track.
	 */
	private static final Vector3f TESTED_LOCATION = new Vector3f(10, 10, 10);

	/**
	 * The RaceTrack.
	 */
	private RaceTrack raceTrack;
	
	/**
	 * PhysicsSpace necessary to construct a RaceTrack.
	 */
	private PhysicsSpace physicsSpace;
	
	/**
	 * AlphaState necessary to construct a RaceTrack.
	 */
	private AlphaState alphaState;
	
	/**
	 * TextureState necessary to construct a RaceTrack.
	 */
	private TextureState textureState;

	/**
	 * {@inheritDoc}
	 */
	@Before
	public final void setUp() throws Exception {
		Assert.fail("not finished yet");
		physicsSpace = createMock(PhysicsSpace.class);
		physicsSpace = createMock(PhysicsSpace.class);
		alphaState = createMock(AlphaState.class);
		textureState = createMock(TextureState.class);
		StaticPhysicsNodeImpl physicsNodeImpl = new StaticPhysicsNodeImpl();
		expect(physicsSpace.createStaticNode()).andReturn(physicsNodeImpl);
		expectLastCall().times(3);
		replay(physicsSpace);
		
		Mockit.redefineMethods(ModelUtil.class, ModelUtilForTest.class);
		
		raceTrack = new RaceTrack(physicsSpace, TESTED_LOCATION, alphaState, textureState);
	}

	/**
	 * Test the correct result when a vehicle reaches a checkpoint.
	 */
	@Test
	public final void testIsVehicleReachedCheckpoint() {
		Assert.fail("not finished yet");
		StaticPhysicsNodeImpl vehicle = new StaticPhysicsNodeImpl();
		vehicle.setLocalTranslation(TESTED_LOCATION);
		Assert.assertTrue(raceTrack.isVehicleReachedCheckpoint(vehicle.getWorldBound()));
	}
}
