/**
 * Abstract base pipeline test with regression test features. Extend this class to implement your own base pipeline test class.
 */
package com.bancsabadell.jenkins.unit.base

import com.bancsabadell.jenkins.unit.domain.Env
import com.bancsabadell.jenkins.unit.domain.File
import com.bancsabadell.jenkins.unit.domain.Library
import com.bancsabadell.pipeline.*
import com.lesfurets.jenkins.unit.BaseRegressionTest
import com.lesfurets.jenkins.unit.MethodSignature

import groovy.lang.Binding

import java.time.*

/**
 * @author loga
 *
 */
abstract class AbstractPipelineTest extends BaseRegressionTest {
    /** Properties files to load as maps */
    protected List<File> propfiles = []

	/** Properties files to bind */
	protected List<File> bindfiles = []
	
    /** Files to read */
    protected Map<String, String> files = [:]

    /** JSONs to read */
    protected Map<String, String> jsons = [:]
    
    /** Libraries to load */
    protected List<Library> libs = []

    /** Scripts to load */
    protected List<String> scripts = []

    /** Utils for registration and others */
    protected Utils utils

    /**
     * Set up test.
     */
    @Override
    public void setUp() {
        /* Set up super class */
        super.setUp()

        /* Default script location */
        helper.setScriptRoots(scriptRoots)

        /* Init helper with previous settings */
        helper.init()

        /* Create Utils instance */
        utils = new Utils(helper)

        /* Register methods Groovy and Jenkins methods */
        registerJenkinsMethods()
		registerMyMethods()
		
		/* Register read properties actions */
		registerReadProperties()
		
		/* Bind properties files */
		bindProperties()
        
		/* Load libraries and scripts */
		registerLoadLibraries()
        registerLoadScripts()
    }

	/**
	 * Register my Groovy methods.
	 */
	protected void registerMyMethods() {
		//helper.registerAllowedMethod('server', [String.class], null)
	}
	
    /**
     * Register jenkins scripted pipeline methods.
     */
    protected void registerJenkinsMethods() {
		helper.registerAllowedMethod('findFiles', [LinkedHashMap.class], {return [new java.io.File('mockFile')]})
        helper.registerAllowedMethod('jobDsl', [Map.class], null)
        helper.registerAllowedMethod('bat', [String.class], null)
        helper.registerAllowedMethod('timeout', [Integer.class, Closure.class], null)
        helper.registerAllowedMethod('waitUntil', [Closure.class], null)
        helper.registerAllowedMethod('writeFile', [Map.class], null)
        helper.registerAllowedMethod('build', [Map.class], null)
        helper.registerAllowedMethod('tool', [Map.class], { Map m -> return m.get("name") })
        helper.registerAllowedMethod('tool', [String.class], { t -> "${t}_HOME" })
        helper.registerAllowedMethod('withCredentials', [Map.class, Closure.class], null)
        helper.registerAllowedMethod('withCredentials', [List.class, Closure.class], null)
        helper.registerAllowedMethod('usernamePassword', [Map.class], { creds -> return creds })
        helper.registerAllowedMethod('deleteDir', [], null)
        helper.registerAllowedMethod('pwd', [], { 'workspaceDirMocked' })
        helper.registerAllowedMethod('stash', [Map.class], null)
        helper.registerAllowedMethod('unstash', [Map.class], null)
        helper.registerAllowedMethod('checkout', [Closure.class], null)
        helper.registerAllowedMethod('checkout', [String.class], null)
        helper.registerAllowedMethod('withEnv', [List.class, Closure.class], { List list, Closure c ->
            list.each {
                def item = it.split('=')
                assert item.size() == 2, "withEnv list does not look right: ${list.toString()}"
                addEnvVar(item[0], item[1])
                c.delegate = binding
                c.call()
            }
        })
		helper.registerAllowedMethod('timestamps', [Closure.class], null)
		helper.registerAllowedMethod('readMavenPom', [Map.class], { Map pomfile ->
			return utils.readPomFile(pomfile.get('file'))
		})
        helper.registerAllowedMethod('readJSON', [Map.class], { Map jsonfile ->
            if(jsonfile.get('file') != null) {
                return utils.readJSON(jsons.get(jsonfile.get('file')))
            }
            return utils.readJSONFromText(jsonfile.get('text'))
        })
        helper.registerAllowedMethod('readFile', [Map.class], { Map file ->
            return utils.readFile(files.get(file.get('file')))
        })
        helper.registerAllowedMethod('readFile', [String.class], { String file ->
            return utils.readFile(file)
        })
		helper.registerAllowedMethod('withSonarQubeEnv', [Closure.class], null)
        helper.registerAllowedMethod('withSonarQubeEnv', [String.class, Closure.class], null)
		helper.registerAllowedMethod('node', [String.class, Closure.class], null)
		helper.registerAllowedMethod('isUnix', [], {return false})
		helper.registerAllowedMethod('libraryResource', [String.class], {String filename -> 
			return new java.io.File(binding.getVariable('confDir') + filename).getText('UTF-8')
		})
    }

    /**
     * Register read properties methods.
     */
    protected void registerReadProperties() {
        for(props in propfiles) {
            utils.registerReadPropertiesRetMap(props.filepath, props.encoding)
        }
    }

	/**
	 * Bind properties from files.
	 */
	protected void bindProperties() {
		for(props in bindfiles) {
			utils.bindPropertiesFromFile(binding, props.filepath, props.encoding)
		}
	}

    /**
     * Register load library methods.
     */
    protected void registerLoadLibraries() {
        for(lib in libs) {
			/* Load Git library */
			if(lib.getGitUrl()){
				utils.loadGitLib(lib)
			}
			/* Load local library */
			else if(lib.getLocalUrl()){
				utils.loadLocalLib(lib)
			}
        }
    }

    /**
     * Register load script methods.
     */
    protected void registerLoadScripts() {
        for(script in scripts) {
            /* Run the script to return the object */
            Object obj = runScript(script)
            utils.registerLoadMethod(obj)
        }
    }

    /**
     * Add environment variables to test.
     * @param name - Variable name
     * @param val - Variable value
     */
    protected void addEnvVar(String name, String val) {
        if (!binding.hasVariable('env')) {
            binding.setVariable('env', new Env())
        }
        Env env = binding.getVariable('env')
        env[name] = val
    }
}
