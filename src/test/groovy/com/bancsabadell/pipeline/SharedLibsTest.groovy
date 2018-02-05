/**
 * Test pipeline with shared libraries in Groovy.
 */
package com.bancsabadell.pipeline

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import com.bancsabadell.jenkins.unit.PipelineTest
import com.bancsabadell.jenkins.unit.domain.Library
import com.lesfurets.jenkins.unit.*
import com.lesfurets.jenkins.unit.global.lib.*

/**
 * @author loga
 *
 */
class SharedLibsTest extends PipelineTest {
    /**
     * Set up test.
     */
    @Override
    @Before
    public void setUp() {
        /* Register library */
        libs.add(new Library(gitUrl:'https://github.com/luisalbertogh/my-jenkins-lib', libName:'simple-lib', destination:'my-jenkins-lib', branch:'master'))

        /* Set default pom */
        poms.put('pom.xml','pom.xml')
        
        /* Set up test */
        super.setUp()
    }

    /**
     * Test execution.
     */
    //@Test
    public void testExecution() {
        /* Run the script */
        runScript('sh-libraries.pipeline')

        /* Print call stack */
        printCallStack()

        /* Assert step */
        assertTrue(helper.callStack.findAll({call -> call.methodName == "echo"}).any({call -> MethodCall.callArgsToString(call).contains("Hello Luis from simple lib")}))

        /* Assert job status */
        assertJobStatusSuccess()
    }
}
