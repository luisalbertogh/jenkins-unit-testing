/**
 * Mock environment variables map.
 */
package com.bancsabadell.jenkins.unit.domain

/**
 * @author loga
 *
 */
class Env extends TreeMap {
	Map getEnvironment() {
		this
	}
	
	def getProperty(p) {
		get(p)
	}
	
	void setProperty(p, v) {
		put(p, v)
	}
}
