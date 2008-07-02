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

package net.juniorbl.jtoyracing.entity.environment;

import java.util.ArrayList;
import java.util.List;

import net.juniorbl.jtoyracing.util.ModelUtil;
import net.juniorbl.jtoyracing.util.ResourcesPath;

import com.jme.bounding.BoundingVolume;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

/**
 * Place where the race happens.
 *
 * @version 1.0 Oct 2, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public class RaceTrack extends Node {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 8801407867521059306L;

	/**
	 * The actual Track.
	 */
	private StaticPhysicsNode track;

	/**
	 * Checkpoints of the race track.
	 */
	private List<StaticPhysicsNode> checkPoints;

	/**
	 * Constructs a race track. It uses various states to build its components.
	 * 
	 * @param physicsSpace the physicsSpace.
	 * @param trackLocation the trackLocation.
	 * @param alphaState the alphaState.
	 * @param textureState the textureState.
	 */
	public RaceTrack(PhysicsSpace physicsSpace, Vector3f trackLocation,
			AlphaState alphaState, TextureState textureState) {
		//FIXME trackLocation should be in this class
		createTrack(physicsSpace, trackLocation);
		createCheckPoints(physicsSpace, trackLocation, alphaState, textureState);
	}

	/**
	 * Create the track.
	 * 
	 * @param physicsSpace the physicsSpace.
	 * @param trackLocation the trackLocation.
	 */
	private void createTrack(PhysicsSpace physicsSpace, Vector3f trackLocation) {
		track = physicsSpace.createStaticNode();
		track.setLocalTranslation(trackLocation);
//		track.createBox("track");
		track.setLocalScale(1f);
		track.attachChild(ModelUtil.convertMultipleModelToJME(ResourcesPath.MODELS_PATH + "obj/raceTrack.obj"));
		track.generatePhysicsGeometry(true);
		track.setMaterial(Material.IRON);
		this.attachChild(track);
	}

	/**
	 * Create the checkpoints of the track. Checkpoints are some locations around a race track
	 * that recharge the health of the cars. It's the same location as the track because
	 * they are in the same 3D model.
	 *
	 * @param physicsSpace the physicsSpace.
	 * @param checkPointsLocation the location of the checkpoints, same location as the track.
	 * @param alphaState the alphaState.
	 * @param textureState the textureState.
	 */
	private void createCheckPoints(PhysicsSpace physicsSpace, Vector3f checkPointsLocation,
			AlphaState alphaState, TextureState textureState) {
		checkPoints = new ArrayList<StaticPhysicsNode>();
		//bend #3 checkpoint
		StaticPhysicsNode bendThreeCheckPoint = physicsSpace.createStaticNode();
		bendThreeCheckPoint.setLocalTranslation(checkPointsLocation);
		bendThreeCheckPoint.attachChild(
				ModelUtil.convertModelSimpleObjToJME(
						ResourcesPath.MODELS_PATH + "obj/bendThreeCheckpoint.obj"));
		configureCheckPoint(bendThreeCheckPoint);
		checkPoints.add(bendThreeCheckPoint);
		//start checkpoint
		StaticPhysicsNode startCheckPoint = physicsSpace.createStaticNode();
		startCheckPoint.setLocalTranslation(checkPointsLocation);
		startCheckPoint.attachChild(
				ModelUtil.convertModelSimpleObjToJME(
						ResourcesPath.MODELS_PATH + "obj/startCheckPoint.obj"));
		configureCheckPoint(startCheckPoint);
		checkPoints.add(startCheckPoint);

		configureCheckPointsStates(alphaState, textureState);
		this.attachChild(bendThreeCheckPoint);
		this.attachChild(startCheckPoint);
	}

	/**
	 * Configure the checkpoint so that there's no collision with it.
	 * 
	 * @param checkPoint the checkpoint.
	 */
	private void configureCheckPoint(StaticPhysicsNode checkPoint) {
		checkPoint.generatePhysicsGeometry();
		checkPoint.setLocalScale(1f);
		checkPoint.setMaterial(Material.GHOST);
	}

	/**
	 * Configure the states of the checkpoints. TextureState is used along with alphastate to set them transparent.
	 * 
	 * @param alphaState the alphaState.
	 * @param textureState the textureState.
	 */
	private void configureCheckPointsStates(AlphaState alphaState, TextureState textureState) {
		for (StaticPhysicsNode checkPoint : checkPoints) {
			Texture texture = TextureManager.loadTexture(getClass().getClassLoader()
					.getResource(ResourcesPath.TEXTURE_PATH + "transparency.png"),
					Texture.MM_LINEAR, Texture.FM_LINEAR, 1.0f, true);
			texture.setWrap(Texture.WM_WRAP_S_WRAP_T);
			texture.setScale(new Vector3f(1f, 1f, 1f));
			textureState.setTexture(texture);
			checkPoint.setRenderState(textureState);

			alphaState.setBlendEnabled(true);
			alphaState.setSrcFunction(AlphaState.SB_SRC_ALPHA);
			alphaState.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
			alphaState.setTestEnabled(false);
			alphaState.setEnabled(true);
			checkPoint.setRenderState(alphaState);
		}
	}
	
	/**
	 * Checks whether a vehicle reached a checkpoint.
	 *
	 * @param vehicleBoundingVolume the bounding volume of a vehicles.
	 * @return <code>true</code> if the checkpoint was reached, <code>false</code> if not.
	 */
	public final boolean isVehicleReachedCheckpoint(BoundingVolume vehicleBoundingVolume) {
		for (StaticPhysicsNode checkPoint : checkPoints) {
			if (vehicleBoundingVolume.intersects(checkPoint.getWorldBound())) {
				return true;
			}
		}
		return false;
	}
}
