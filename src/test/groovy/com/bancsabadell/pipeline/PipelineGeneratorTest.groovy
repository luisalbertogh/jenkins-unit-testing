/**
 * Test pipeline in Groovy.
 */
package com.bancsabadell.pipeline

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import com.bancsabadell.jenkins.unit.PipelineTest
import com.bancsabadell.jenkins.unit.domain.File
import com.lesfurets.jenkins.unit.*

import groovy.mock.interceptor.*


/**
 * @author loga
 *
 */
class PipelineGeneratorTest extends PipelineTest {
    /**
     * Set up test.
     */
    @Override
    @Before
    public void setUp() {
        /* Read properties files to a map - envs = readProperties  file: "properties/generator.properties" */
        propfilesMap.put('generator.properties', 'src/test/resources/properties/generator.properties')
        
        /* Set default pom */
        poms.put('pom.xml','pom.xml')
        
        /* Read file (jenkins method) - output = readFile("output.txt") */
        files.put('tempProperties.properties','src/test/resources/properties/tempProperties.properties')
        files.put('output.txt', 'src/test/resources/properties/tempProperties.properties')
        
        /* Load groovy script */
        scripts.add('script/Utils/BitbucketScript.groovy')

        /* Set up test */
        super.setUp()

        /* Define variables */
        binding.setVariable('conf_branch', 'master')
        binding.setVariable('conf_credentials', 'user:password')
        binding.setVariable('conf_gitUrl', 'github.com')
        binding.setVariable('user', 'the_user')
        binding.setVariable('cred', 'the_credentials')
        binding.setVariable('projectName', 'the_project')
        binding.setVariable('repoName', 'the_repo')		
    }

    /**
     * Test execution.
     * @throws Exception
     */
    @Test
    public void testExecution() throws Exception {
        /* Load and run the script */
        runScript('pipe-generator.pipeline')
		
        /* Print call stack */
        printCallStack()

        /* Assert job status */
        assertJobStatusSuccess()
    }
}
