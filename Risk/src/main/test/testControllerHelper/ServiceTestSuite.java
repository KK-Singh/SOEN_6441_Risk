package testControllerHelper;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
/**
 * this suite run the test classes for Game Controller, Map controller, map verification
 * @author Kunal
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ GameControllerHelperTest.class, MapControllerHelperTest.class, MapVerificationTest.class })
public class ServiceTestSuite {

}
