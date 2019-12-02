package testService;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GameServiceTest.class, MapServiceTest.class, MapVerificationTest.class })
public class ServiceTestSuite {

}
