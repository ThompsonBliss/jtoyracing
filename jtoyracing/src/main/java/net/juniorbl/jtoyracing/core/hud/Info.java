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

package net.juniorbl.jtoyracing.core.hud;

import java.nio.FloatBuffer;

import net.juniorbl.jtoyracing.util.ResourcesPath;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;

/**
 * Responsible for put the information on the screen.
 *
 * Note: part of the creation of this bar was extracted from JMonkeyEngine documentation.
 *
 * @version 1.0 Oct 16, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public class Info extends Node {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -7326797374588827635L;

	/**
	 * Height of the health symbol.
	 */
	private static final float HEALTH_SYMBOL_HEIGHT = 40f;

	/**
	 * Width of the health symbol.
	 */
	private static final float HEALTH_SYMBOL_WIDTH = 40f;

	/**
	 * Location of the message information on the screen.
	 */
	private static final Vector3f TEXT_INFO_LOCATION = new Vector3f(0, 85, 0);

	/**
	 * Location of the health bar.
	 */
	private static final Vector3f HEALTH_BAR_LOCATION = new Vector3f(80, 65, 0);

	/**
	 * Width of the bar's border.
	 */
	private final static int BAR_BORDER_WIDTH = 96;

	/**
	 * Height of the bar's border.
	 */
	private final static int BAR_BORDER_HEIGHT = 13;

	/**
	 * Width of the health bar.
	 */
	private final static int HEALTH_BAR_WIDTH = 94;

	/**
	 * Height of the health bar.
	 */
	private final static int HEALTH_BAR_HEIGHT = 11;

	/**
	 * Location of the health symbol.
	 */
	private static final Vector3f HEALTH_SYMBOL_LOCATION = new Vector3f(25, 55, 0);

	/**
	 * First pixel, first from above, of the bar.
	 */
	private static final int BAR_FIRST_PIXEL = 53;

	/**
	 * Last pixel, first from above, of the bar.
	 */
	private static final int BAR_LAST_PIXEL = 63;

	/**
	 * Symbol of the health.
	 */
	private Quad healthSymbol;

	/**
	 * Borders of the bar.
	 */
	private Quad barBorders;

	/**
	 * A health bar.
	 */
	private Quad healthBar;

	/**
	 * Object which holds the text information.
	 */
	private Text textInfo;

	/**
	 * Node which represents the health bar.
	 */
	private Node healthBarNode;

	/**
	 * Max value of the health bar.
	 */
	private int maxHealthBarValue;

	/**
	 * Height of the health bar.
	 */
	private int healthBarTextureHeight;

	/**
	 * Info uses a renderer to create texture and alpha states. A vehicle's health is used in the health bar.
	 *
	 * @param renderer the renderer.
	 * @param vehicleHealth vehicle's health.
	 */
	public Info(Renderer renderer, Integer vehicleHealth) {
		this.maxHealthBarValue = vehicleHealth;
		TextureState healthSymbolTextureState = loadTextureState(renderer.createTextureState(),
				ResourcesPath.IMAGES_PATH + "healthSymbol.png");
		createHealthSymbol(healthSymbolTextureState);
		TextureState healthTextureState = loadTextureState(renderer.createTextureState(),
				ResourcesPath.IMAGES_PATH + "healthBar.png");
		createHealthBar(healthTextureState);
		createTransparency(renderer.createAlphaState());
		//FIXME the use of "10" below is because the bar doesn't fill entirely.
		setHealthBarValue(vehicleHealth + 10);
		startTextInformation();
	}

	/**
	 * Starts the text information.
	 */
	private void startTextInformation() {
		textInfo = new Text("textNode", "");
		textInfo.setTextColor(ColorRGBA.red);
		textInfo.setLocalTranslation(TEXT_INFO_LOCATION);
		this.attachChild(textInfo);
	}

	/**
	 * Prints a message on the screen.
	 *
	 * @param message the message to be printed.
	 */
	public final void printMessage(String message) {
		textInfo.print(message);
	}

	/**
	 * Creates the health bar. This bar shows the amount of health each car has available for the race
	 * at some moment.
	 *
	 * @param textureState the texture state.
	 */
	private void createHealthBar(TextureState textureState) {
		healthBarNode = new Node();
		healthBarNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		healthBarNode.setLocalTranslation(HEALTH_BAR_LOCATION);
		healthBarNode.setLightCombineMode(LightState.OFF);
		healthBarNode.updateRenderState();
		healthBarNode.setRenderState(textureState);

		int healthBarTextureWidth = textureState.getTexture().getImage().getWidth();
		healthBarTextureHeight = textureState.getTexture().getImage().getHeight();

		barBorders = new Quad("barBorder", new Float(BAR_BORDER_WIDTH), new Float(BAR_BORDER_HEIGHT));
		healthBar = new Quad("bar", new Float(HEALTH_BAR_WIDTH), new Float(HEALTH_BAR_HEIGHT));

		healthBarNode.attachChild(barBorders);
		healthBarNode.attachChild(healthBar);
		barBorders.setTextureBuffer(0, createTextureCoordinates(BAR_BORDER_WIDTH, BAR_BORDER_HEIGHT,
				healthBarTextureWidth, healthBarTextureHeight));
	}

	/**
	 * Loads a texture state according to a given image path.
	 *
	 * @param textureState a texture state.
	 * @param imagePath a image path.
	 * @return the texture state.
	 */
	private TextureState loadTextureState(TextureState textureState, String imagePath) {
		textureState.setTexture(TextureManager.loadTexture(getClass()
				.getClassLoader().getResource(imagePath),
				Texture.MM_LINEAR, Texture.FM_LINEAR, 1.0f, true));
		textureState.setEnabled(true);
		return textureState;
	}

	/**
	 * Creates coordinates for the texture according to a given width and height.
	 *
	 * @param imageWidth the width of an image.
	 * @param imageHeight the height of an image.
	 * @param textureWidth the width of a texture.
	 * @param textureHeight the height of a texture.
	 * @return the texture coordinates represented as a FloatBuffer.
	 */
	private FloatBuffer createTextureCoordinates(int imageWidth, int imageHeight,
			int textureWidth, int textureHeight) {
		FloatBuffer textureCoordinates = BufferUtils.createVector2Buffer(4);
		textureCoordinates.put((float) 0 / textureWidth).put(1f - (float) 0 / textureHeight);
		textureCoordinates.put((float) 0 / textureWidth).put(1f - (float) imageHeight / textureHeight);
		textureCoordinates.put((float) imageWidth / textureWidth).put(1f - (float) imageHeight / textureHeight);
		textureCoordinates.put((float) imageWidth / textureWidth).put(1f - (float) 0 / textureHeight);
		return textureCoordinates;
	}

	/**
	 * Creates an alpha state to not show transparency of images.
	 *
	 * @param alphaState the alpha state.
	 */
	private void createTransparency(AlphaState alphaState) {
		alphaState.setBlendEnabled(true);
		alphaState.setSrcFunction(AlphaState.SB_SRC_ALPHA);
		alphaState.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
		alphaState.setTestEnabled(false);
		alphaState.setEnabled(true);
		this.setRenderState(alphaState);

		healthBarNode.setRenderState(alphaState);
		healthBarNode.updateRenderState();
		this.attachChild(healthBarNode);
	}

	/**
	 * Creates the health symbol.
	 *
	 * @param textureState the texture state.
	 */
	private void createHealthSymbol(TextureState textureState) {
		healthSymbol = new Quad("hud", HEALTH_SYMBOL_WIDTH, HEALTH_SYMBOL_HEIGHT);
		healthSymbol.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		healthSymbol.setLocalTranslation(HEALTH_SYMBOL_LOCATION);
		healthSymbol.setLightCombineMode(LightState.OFF);
		healthSymbol.updateRenderState();
		healthSymbol.setRenderState(textureState);
		int healthSymbolTextureWidth = textureState.getTexture().getImage().getWidth();
		int healthSymbolTextureHeight = textureState.getTexture().getImage().getHeight();
		healthSymbol.setTextureBuffer(0,
				createTextureCoordinates((int) HEALTH_SYMBOL_WIDTH, (int) HEALTH_SYMBOL_HEIGHT,
						healthSymbolTextureWidth, healthSymbolTextureHeight));
		this.attachChild(healthSymbol);
	}

	/**
	 * Sets a value in the health bar. Calculates the position of the content according to a given value.
	 *
	 * @param healthValue the amount of health.
	 */
	public final void setHealthBarValue(int healthValue) {
		int desiredHealth = healthValue;
		desiredHealth %= (int) maxHealthBarValue;
		FloatBuffer coordinates = BufferUtils.createVector2Buffer(4);
		//Calculate the location
		float relCoord = 0.5f - ((float) desiredHealth / maxHealthBarValue) * 0.5f;
		coordinates.put(relCoord).put(1f - (float) BAR_FIRST_PIXEL / healthBarTextureHeight);
		coordinates.put(relCoord).put(1f - (float) BAR_LAST_PIXEL / healthBarTextureHeight);
		coordinates.put(relCoord + 0.5f).put(1f - (float) BAR_LAST_PIXEL / healthBarTextureHeight);
		coordinates.put(relCoord + 0.5f).put(1f - (float) BAR_FIRST_PIXEL / healthBarTextureHeight);
		healthBar.setTextureBuffer(0, coordinates);
	}
}
