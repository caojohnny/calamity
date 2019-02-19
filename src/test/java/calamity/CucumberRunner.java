package calamity;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = { "pretty", "html:target/cucumber" },
        features = "src/test/resources/calamity",
        glue = "calamity"
)
public class CucumberRunner {
}

