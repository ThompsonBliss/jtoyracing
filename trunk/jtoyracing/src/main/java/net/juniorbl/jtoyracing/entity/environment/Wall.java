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

import net.juniorbl.jtoyracing.util.ModelUtil;
import net.juniorbl.jtoyracing.util.ResourcesPath;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

//FIXME don't load a 3D model, use 4 Quad

//FIXME erase this class, load the wall inside the room

/**
 * Wall of the room.
 *
 * @version 1.0 Sep 16, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public class Wall extends Node {
	
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 8276219154604080263L;

	/**
	 * Constructs a wall.
	 * 
	 * @param physicsSpace the physicsSpace;
	 */
	public Wall(PhysicsSpace physicsSpace) {
		super("wall");
		StaticPhysicsNode wall = physicsSpace.createStaticNode();
		wall.setLocalTranslation(new Vector3f(0, -30, 30));
		wall.attachChild(ModelUtil.convertMultipleModelToJME(ResourcesPath.MODELS_PATH + "obj/wall.obj"));
		wall.setMaterial(Material.GHOST);
		wall.setLocalScale(11f);
		wall.generatePhysicsGeometry();
		this.attachChild(wall);
	}
}
