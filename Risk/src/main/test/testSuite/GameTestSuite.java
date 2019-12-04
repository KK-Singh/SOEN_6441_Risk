package testSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import testControllerHelper.ServiceTestSuite;
import testModel.ModelTestSuit;

@RunWith(Suite.class)
@SuiteClasses({ ServiceTestSuite.class, ModelTestSuit.class })
public class GameTestSuite {

}
