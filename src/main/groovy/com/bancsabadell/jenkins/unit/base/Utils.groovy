/**
 * Utilities and functions for pipeline tests.
 */
package com.bancsabadell.jenkins.unit.base

import com.bancsabadell.jenkins.unit.domain.Library
import com.lesfurets.jenkins.unit.MethodCall
import com.lesfurets.jenkins.unit.MethodSignature
import com.lesfurets.jenkins.unit.PipelineTestHelper
import com.lesfurets.jenkins.unit.global.lib.GitSource
import com.lesfurets.jenkins.unit.global.lib.LocalSource
import com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.util.slurpersupport.GPathResult
import org.yaml.snakeyaml.Yaml

/**
 * @author loga
 *
 */
class Utils {
	/** Helper */
    private PipelineTestHelper helper

    /**
     * Constructor.
     * @param helper
     */
    public Utils(PipelineTestHelper helper) {
        this.helper = helper
    }

    /**
     * Load library from git source
     * @param libArg - The library to register
     */
    public void loadGitLib(Library libArg) {
        def gitSource = GitSource.gitSource(libArg.getGitUrl())
        def lib = LibraryConfiguration.library(libArg.getLibName()).retriever(gitSource).targetPath(libArg.getDestination()).defaultVersion(libArg.getBranch()).allowOverride(true).implicit(false).build()
        helper.registerSharedLibrary(lib)
    }

	/**
	 * Load library from local source
	 * @param libArg - The library to register
	 */
	public void loadLocalLib(Library libArg) {
		def localSource = MyLocalSource.localSource(libArg.getLocalUrl())
		def lib = LibraryConfiguration.library(libArg.getLibName()).retriever(localSource).targetPath(libArg.getDestination()).defaultVersion(libArg.getBranch()).allowOverride(true).implicit(false).build()
		helper.registerSharedLibrary(lib)
	}
	
    /**
     * Register readProperties method. The method reads the properties file and return a map.
     * @param filepath - The file path
     * @param enc - The encoding
     */
    public void registerReadPropertiesRetMap(String filepath, String enc) {
        helper.registerAllowedMethod('readProperties', [Map.class], { file ->
            return new File(filepath).readLines(enc).inject([:]){ map, line ->
                def entry = line.split("=")
                if(!line.startsWith('#')){
                    map.putAt(entry[0].trim(), entry[1].trim())
                }
                return map
            }
        })		
    }
    
    /**
     * Register readProperties method. The method reads the properties file and return a map.
     * @param filepath - The file path
     * @param enc - The encoding
     */
    public def returnReadPropertiesRetMap(String filepath, String enc) {
        return new File(filepath).readLines(enc).inject([:]){ map, line ->
            def entry = line.split("=")
            if(!line.startsWith('#')){
                map.putAt(entry[0].trim(), entry[1].trim())
            }
            return map
        }
    }
	
	/**
	 * Read properties from file and bind them.
	 * @param binding - The test Binding instance
	 * @param filepath - The filepath with the properties
	 * @param enc - The file encoding
	 */
	public void bindPropertiesFromFile(Binding binding, String filepath, String enc) {
		File propfile = new File(filepath)
		propfile.readLines(enc).each { String line ->
	        def entry = line.split("=")
	        if(!line.startsWith('#')){
				binding.setProperty(entry[0].trim(), entry[1].trim())
	        }
        }
	}
	
	/**
	 * Read pom file and find property value.
	 * @param filepath - The filepath to the pom
	 * @param property - The property value
	 */
	public String getPropertyFromPom(String filepath, String property) {
		String pomText = new File(filepath).getText()
		def matcher = pomText =~ "<$property>(.+)</$property>"
		return matcher ? matcher[0][1] : null
	}

	/**
	 * Read a pom file and retrieve the mapped content.
	 * @param filepath - The filepath to the pom
	 */
	public GPathResult readPomFile(String filepath) {
		return new XmlSlurper().parse(filepath)
	}
	
    /**
     * Register readFile method to read a filepath and return the file content.
     * @param filepath - The file path
     * @param enc - The encoding
     */
    public String readFile(String filepath) {
        return new File(filepath).getText('UTF-8')
    }
    
    /**
     * Register readFile method to read a filepath and return the file content.
     * @param filepath - The file path
     * @param enc - The encoding
     */
    public Map readYaml(String filepath) {
        return new Yaml().load(filepath)
    }

    /**
     * Register writeFile method to write a file.
     * @param filepath - The file path
     * @param enc - The encoding
     */
    public void writeFile(String filepath, String content) {
        Writer w = new FileWriter(new File(filepath))
        w.write(content)
        w.close()
    }
    
    /**
     * Register readJSON method to read a JSON filepath and return the file content.
     * @param filepath - The file path
     */
    public def readJSON(String filepath) {
        return new JsonSlurper().parse(new File(filepath))
    }
    
    /**
     * Register writeJSON method to write to a JSON filepath.
     * @param filepath - The file path
     */
    public def writeJSON(def jsonArg, String filepath) {
        new File(filepath).write(new JsonBuilder(jsonArg).toPrettyString())
    }
    
    /**
     * Register readJSON method to read a JSON filepath and return the file content.
     * @param filepath - The file path
     */
    public def readJSONFromText(String text) {
        return new JsonSlurper().parseText(text)
    }
    
    /**
     * Register load method. This is intended for methods that load a groovy file where a class is defined. Then it returns the class.
     * @param script - The script path
     */
    public void registerLoadMethod(Object script) {
        helper.registerAllowedMethod(MethodSignature.method('load', String.class), {file -> return script})
    }
	
	/**
	 * Register call method into stack.
	 * @param obj - Target object
	 * @param methodName - Method name
	 * @param args - Method arguments
	 * @param stackDepth - Stack depth
	 */
	public void registerCall(Object obj, String methodName, List<String> args, int stackDepth) {
		helper.getCallStack().add(new MethodCall(target: obj, methodName: methodName, args: args, stackDepth: stackDepth))
	}
	
	/**
	 * Count the number of calls to the method with name
	 * @param name method name
	 * @return call number
	 */
	public long methodCallCount(String targetName, String methodName) {
		helper.getCallStack().stream().filter { call ->
			call.getProperties().get('target').getClass().getSimpleName() == targetName && call.getProperties().get('methodName') == methodName 
		}.count()
	}
}
