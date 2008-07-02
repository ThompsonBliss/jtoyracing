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

package net.juniorbl.jtoyracing.audio;

import com.jme.renderer.Camera;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;

/**
 * Audio configuration of the game.
 *
 * @version 1.0 Jan 02, 2008
 * @author Fco. Carlos L. Barros Junior
 */
public final class AudioConfig {

	/**
	 * Volume of the sound effect.
	 */
	private final static float SOUND_EFFECT_VOLUME = 0.1f;

    /**
     * The audio system of the game.
     */
	private AudioSystem audioSystem;
	
	/**
	 * Constructs a audio configuration.
	 *
	 * @param camera a camera to simulate audio 3D.
	 */
	public AudioConfig(Camera camera) {
		loadAudioSystem(camera);
	}

	/**
	 * Loads a sound effect. Sound effects are around in the scene.
	 * 
	 * @param effectPath path of the sound effect.
	 * @return sound effect loaded.
	 */
	public static AudioTrack loadSoundEffect(String effectPath) {
		AudioTrack soundEffect = AudioSystem.getSystem().
			createAudioTrack(AudioConfig.class.getClassLoader().getResource(effectPath), false);
		soundEffect.setType(AudioTrack.TrackType.ENVIRONMENT);
		soundEffect.setRelative(true);
		soundEffect.setLooping(true);
		soundEffect.setVolume(SOUND_EFFECT_VOLUME);
		return soundEffect;
	}

	/**
	 * Loads the audio system of the game. A camera's position is used as a reference.
	 *
	 * @param camera the camera.
	 */
	private void loadAudioSystem(Camera camera) {
		audioSystem = AudioSystem.getSystem();
		audioSystem.getEar().trackOrientation(camera);
		audioSystem.getEar().trackPosition(camera);
	}

	/**
	 * Updates the audio system.
	 */
	public void update() {
		audioSystem.update();
	}
}
