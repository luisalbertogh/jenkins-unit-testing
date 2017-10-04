/**
 * Pipeline test base class. Use it to configure all your test common features.
 */
package net.luisalbertogh.jenkins.unit

import net.luisalbertogh.jenkins.unit.base.AbstractPipelineTest

/**
 * @author loga
 *
 */
class PipelineTest extends AbstractPipelineTest {
    
	/**
	 * Default constructor.
	 */
	public PipelineTest() {
		/* Root path for pipeline files to test */
		scriptRoots = ['src/test/resources/pipelines']
	} 
	
	/**
     * Set up test.
     */
    @Override
    public void setUp() {
        /* Set up super class */
        super.setUp()
    }
}
