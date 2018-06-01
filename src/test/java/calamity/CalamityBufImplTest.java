package calamity;

import com.gmail.woodyc40.calamity.CalamityBufImpl;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.WRITER;
import static org.junit.Assert.assertEquals;

public class CalamityBufImplTest {
    private CalamityBufImpl buf;

    @Given("^a regular buffer$")
    public void aRegularBuffer() {
        this.buf = CalamityBufImpl.alloc();
    }

    @When("^byte value (\\d+) is added to the end$")
    public void byteValueIsAddedToTheEnd(byte value) {
        this.buf.write(value);
    }

    @Then("^the buffer size should be (\\d+)$")
    public void theBufferSizeShouldBe(int size) {
        assertEquals(size, this.buf.idx(WRITER));
    }

    @And("^byte at the end should be (\\d+)$")
    public void byteAtTheEndShouldBe(int value) {
        assertEquals(value, this.buf.read(this.buf.idx(WRITER)));
    }
}
