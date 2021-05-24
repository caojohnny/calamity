# `calamity`

Calamity Buffer - A buffer made of components

I decided actually to go with handrolling my own buffers and dealing with raw sockets in my JDB project to communicated between the debugger and the client application. As it turns out, this is a huge hassle. I thought that building Trident's network infrastructure back in 2016 was a hassle, but without Netty, I ended up with a huge amount of unmaintainable spaghetti code in the client application. Thank God it worked without having to make too many changes.

The point of calamity is to get rid of the calamity that is working with byte arrays and streams. Something that mixes ByteArrayOutputStream with the flexibility of ByteBuf and the compatibility with Java's native streams. This is still a WIP project as I spare my free time to work on my actual job and doing other gaming (read: procrastination) related activities.

# Building

``` shell
git clone https://github.com/caojohnny/calamity.git
cd calamity
mvn clean install
```

The output is located in the `target` directory.

# Usage

Download or build yourself using the instructions above.
Add the jar as a dependency.

Instantiate an instance of `CalamityBuf` using 
`CalamityOptions.newBuilder()` to customize certain
components as needed:

``` java
CalamityBuf buf = CalamityOptions.newBuilder().newBuf();
```

You can also use `getDefault()` rather than `newBuilder()`
if you wish to use all default options anyways.

You may then use the buffer to write and read bytes from
a source into the buffer. Operations are done from the
perspective of the buffer; i.e. writing will mean writing
into the buffer and reading will mean reading from the
buffer. 

Out-of-the-box support is given for indexing, which
is a customizable component giving you access to a reader
and writer index, incremented as you read and write. Rather
than using a single index, `calamity` follows the model set
by the [Netty Project](https://netty.io/)'s buffers, using
a double reader and writer index. You can emulate marking
by creating your very own indexes through subclassing one
of the `IndexKey` or `IdentityIndexKey` classes. This is
done in order to give names and create many possible
different marks and clarify different indexes and their
meaning.

Other notable components include the storage underlying the
`CalamityBuf`, which can be modified to create a linked
array of byte arrays rather than a single contiguous array, 
for example. It is also possible to change the resizer to
say, a no-op, or to add extra bounds checks. Finally, one
of the most powerful components is the marshalling ability,
which allows different types of data to be serialized into
the buffer as a provided feature. By default, you may only
add byte arrays, however, support can be added for a number
of different objects. Typecasting is done for all of the
component getter methods so that they may be used in their
raw form should extra functionality be needed.

# Notes

- This is not production-ready

# Credits

Built with [IntelliJ IDEA](https://www.jetbrains.com/idea/)

Uses [Cucumber](https://cucumber.io/) and [JUnit](https://junit.org/junit4/) for some behavioral testing
