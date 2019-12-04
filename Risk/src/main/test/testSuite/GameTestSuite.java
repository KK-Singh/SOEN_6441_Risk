package testSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import testControllerHelper.ServiceTestSuite;
import testModel.ModelTestSuit;
/**
 * this suite have suite ServiceTestSuite and ModelTestSuit
 * @author Kunal
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ ServiceTestSuite.class, ModelTestSuit.class })
public class GameTestSuite {

}
