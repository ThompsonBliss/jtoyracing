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

import javax.swing.ImageIcon;

import net.juniorbl.jtoyracing.util.ModelUtil;
import net.juniorbl.jtoyracing.util.ResourcesPath;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingVolume;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;

/**
 * A kid's room. First level of the game.
 *
 * @version 1.0 Aug 11, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public class Room extends Node {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -4018480325870153949L;

	/**
	 * Scale of the room's texture.
	 */
	private static final Vector3f ROOM_TEXTURE_SCALE = new Vector3f(2f, 2f, 2f);

	/**
	 * Size of the floor.
	 */
	private static final Vector3f FLOOR_SIZE = new Vector3f(9, 0, 5.2f);

	/**
	 * Location of the room.
	 */
	private static final Vector3f LOCATION = new Vector3f(-66, -30, 28);

	/**
	 * Block of terrain where the room is.
	 */
	private TerrainBlock floorBlock;

	/**
	 * Race track of the room.
	 */
	private RaceTrack raceTrack;

	/**
	 * Wall of the room.
	 */
	private Wall wall;

	/**
	 * Constructs a Room. It uses physicsSpace states and renderer to build its components.
	 * 
	 * @param physicsSpace the physicsSpace.
	 * @param renderer the renderer.
	 */
	public Room(PhysicsSpace physicsSpace, Renderer renderer) {
		createFloor(physicsSpace, renderer.createTextureState());
		loadRaceTrack(physicsSpace, renderer.createAlphaState(), renderer.createTextureState());
		loadWall(physicsSpace);
		createLegoDoll(physicsSpace);
		createRoomObjects(physicsSpace);
		this.setLocalTranslation(LOCATION);
	}

	/**
	 * Creates the floor of the room.
	 * 
	 * @param physicsSpace the physicsSpace.
	 * @param textureState the textureState.
	 */
	private void createFloor(PhysicsSpace physicsSpace, TextureState textureState) {
		MidPointHeightMap mapHeight = new MidPointHeightMap(32, 5f);
		floorBlock = new TerrainBlock("terrain", mapHeight.getSize(),
				FLOOR_SIZE, mapHeight.getHeightMap(),
				new Vector3f(0, 0, 0), false);
		floorBlock.setModelBound(new BoundingBox());
		floorBlock.updateModelBound();

		StaticPhysicsNode floorNode = physicsSpace.createStaticNode();
		floorNode.attachChild(floorBlock);
		floorNode.generatePhysicsGeometry(true);
		floorNode.setMaterial(Material.WOOD);
		this.attachChild(floorNode);
		loadFloorTexture(textureState);
	}

	/**
	 * Loads a texture of the floor.
	 * 
	 * @param textureState the textureState.
	 */
	private void loadFloorTexture(TextureState textureState) {
		ImageIcon imagemTextura = new ImageIcon(
				Room.class.getClassLoader().getResource(ResourcesPath.TEXTURE_PATH + "roomFloor.jpg"));
		Texture texture = TextureManager.loadTexture(
				imagemTextura.getImage(), Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR, true);
		texture.setWrap(Texture.WM_WRAP_S_WRAP_T);
		texture.setScale(ROOM_TEXTURE_SCALE);
		textureState.setTexture(texture);
		floorBlock.setRenderState(textureState);
	}

	/**
	 * Loads the race track.
	 * 
	 * @param physicsSpace the physicsSpace.
	 * @param alphaState the alphaState.
	 * @param textureState the textureState.
	 */
	private void loadRaceTrack(PhysicsSpace physicsSpace, AlphaState alphaState, TextureState textureState) {
		raceTrack = new RaceTrack(physicsSpace,
				new Vector3f(20, getFloorHeight(), 55), alphaState, textureState);
		this.attachChild(raceTrack);
	}
	
	/**
	 * Loads the wall.
	 * 
	 * @param physicsSpace the physicsSpace.
	 */
	private void loadWall(PhysicsSpace physicsSpace) {
		wall = new Wall(physicsSpace);
		wall.setLocalTranslation(new Vector3f(67, getFloorHeight() + 30, -28));
		this.attachChild(wall);
	}
	
	/**
	 * Checks whether a vehicle reached a checkpoint of the race track.
	 *
	 * @param vehicleBoundingVolume the bounding volume of a vehicles.
	 * @return <code>true</code> if the checkpoint was reached, <code>false</code> if not.
	 */
	public final boolean isVehicleReachedCheckpoint(BoundingVolume vehicleBoundingVolume) {
		return raceTrack.isVehicleReachedCheckpoint(vehicleBoundingVolume);
	}

	/**
	 * Gets the floor height.
	 * 
	 * @return floorHeight the floor height.
	 */
	public final float getFloorHeight() {
		return floorBlock.getHeight(new Vector3f());
	}

	/**
	 * Creates the lego doll in bend three.
	 * 
	 * @param physicsSpace the physicsSpace.
	 */
	private void createLegoDoll(PhysicsSpace physicsSpace) {
		StaticPhysicsNode legoDoll = physicsSpace.createStaticNode();
		legoDoll.setModelBound(new BoundingBox());
		legoDoll.setLocalTranslation(new Vector3f(200, getFloorHeight() + 3.9f, 30));
		legoDoll.attachChild(ModelUtil.convertMultipleModelToJME(
				ResourcesPath.MODELS_PATH + "obj/legoDoll.obj"));
		legoDoll.setMaterial(Material.IRON);
		legoDoll.setLocalScale(2.5f);
		legoDoll.generatePhysicsGeometry();
		this.attachChild(legoDoll);
	}

	/**
	 * Creates the objects that completes the room.
	 * 
	 * FIXME don't load the objects as one big model.
	 * 
	 * @param physicsSpace the physicsSpace.
	 */
	private void createRoomObjects(PhysicsSpace physicsSpace) {
		StaticPhysicsNode roomObjects = physicsSpace.createStaticNode();
		roomObjects.setModelBound(new BoundingBox());
		roomObjects.setLocalTranslation(new Vector3f(20, getFloorHeight(), 55));
		roomObjects.attachChild(
				ModelUtil.convertMultipleModelToJME(ResourcesPath.MODELS_PATH + "obj/roomObjects.obj"));
		roomObjects.setMaterial(Material.GHOST);
		roomObjects.setLocalScale(1f);
		roomObjects.generatePhysicsGeometry();
		this.attachChild(roomObjects);
	}
}
