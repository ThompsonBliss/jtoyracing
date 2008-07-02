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

package net.juniorbl.jtoyracing.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Chronometer used when a vehicle stops because of lack of health.
 *
 * TODO use chronometer at the beginning of the race.
 *
 * @version 1.0 Dec 22, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public final class HealthChronometer extends Thread implements Chronometer {

	/**
     * One second in milliseconds.
     */
	private static final int ONE_SECOND = 1000;

	/**
     * Indication when the chronometer should stop.
     */
	private boolean stop;

	/**
	 * Amount of time in seconds which a car should wait when his health ends.
	 */
	private int waitSeconds;

	/**
	 * List of observers.
	 */
	private List<ChronometerObserver> chronometerObservers = new ArrayList<ChronometerObserver>();

	/**
	 * Constructor used when the chronometer is monitoring the lack of health.
	 *
	 * @param seconds seconds a car should wait.
	 */
	public HealthChronometer(int seconds) {
		this.waitSeconds = seconds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while (!stop) {
			try {
				if (waitSeconds >= 0) {
					notifyObserversUpdateTime(waitSeconds);
					sleep(ONE_SECOND);
					--waitSeconds;
				} else {
					stopChronometer();
					notifyObserversTimeUP();
				}
			} catch (InterruptedException exception) {
				stopChronometer();
			}
		}
	}

    /**
     * Stops the chronometer.
     */
	public void stopChronometer() {
		stop = true;
	}

    /**
	 * {@inheritDoc}
	 */
	public void addObserver(ChronometerObserver observadorCronometro) {
		chronometerObservers.add(observadorCronometro);
	}

    /**
     * {@inheritDoc}
     */
	public void removeObserver(ChronometerObserver observadorCronometro) {
		chronometerObservers.remove(observadorCronometro);
	}

    /**
     * {@inheritDoc}
     */
	public void notifyObserversUpdateTime(int tempo) {
		for (ChronometerObserver observadorCronometro : chronometerObservers) {
			observadorCronometro.updateTime(tempo);
		}
	}

    /**
     * {@inheritDoc}
     */
	public void notifyObserversTimeUP() {
		for (ChronometerObserver observadorCronometro : chronometerObservers) {
			observadorCronometro.timeUP();
		}
	}
}
