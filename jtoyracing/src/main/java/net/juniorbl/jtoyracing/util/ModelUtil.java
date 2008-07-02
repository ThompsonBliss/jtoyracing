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

package net.juniorbl.jtoyracing.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import com.jme.bounding.BoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.XMLparser.Converters.ObjToJme;

/**
 * Utility class for models. Part of this code came from JMonkeyEngine documentation.
 *
 * @version 1.0 Sep 9, 2007
 * @author Fco. Carlos L. Barros Junior
 */
final public class ModelUtil {
	
	/**
	 * Prevents this class from being instantiated.
	 */
	private ModelUtil() { }

	/**
	 * Converts a OBJ model into JME model according to a given path.
	 * 
	 * @param path the path.
	 * @return the JME model converted.
	 */
	private static ByteArrayOutputStream convertModelObjToJME(String path) {
		ObjToJme converter = new ObjToJme();
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		try {
			URL objFile = ModelUtil.class.getClassLoader()
					.getResource(path);
			converter.setProperty("mtllib", objFile);
			converter.setProperty("texdir", objFile);
			converter.convert(objFile.openStream(), byteArrayOS);
		} catch (IOException e) {
			//TODO handle exception
			throw new Error(e);
		}
		return byteArrayOS;
	}
	
	/**
	 * Converts a simple OBJ model into JME model according to a given path.
	 * 
	 * @param path the path.
	 * @return the JME model converted.
	 */
	public static TriMesh convertModelSimpleObjToJME(String path) {
		TriMesh model = null;
		try {
			ByteArrayOutputStream byteArrayOS = convertModelObjToJME(path);
			model = (TriMesh) BinaryImporter.getInstance().load(
					new ByteArrayInputStream(byteArrayOS.toByteArray()));
			model.setModelBound(new BoundingBox());
			model.updateModelBound();
			//FIXME temporary
			model.setDefaultColor(ColorRGBA.black);
		} catch (IOException e) {
			//TODO handle exception
			throw new Error(e);
		}
		return model;
	}
	
	/**
	 * Converts a multiple OBJ model into JME model according to a given path.
	 * 
	 * @param path the path.
	 * @return the JME model converted.
	 */
	public static Node convertMultipleModelToJME(String path) {
		Node modelo = null;
		try {
			ByteArrayOutputStream byteArrayOS = convertModelObjToJME(path);
			modelo = (Node) BinaryImporter.getInstance().load(
					new ByteArrayInputStream(byteArrayOS.toByteArray()));
			modelo.setModelBound(new BoundingBox());
			modelo.updateModelBound();
		} catch (IOException e) {
			//TODO handle exception
			throw new Error(e);
		}
		return modelo;
	}
}
