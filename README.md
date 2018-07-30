# calamity

Calamity Buffer - A buffer made of components

I decided actually to go with handrolling my own buffers and dealing with raw sockets in my JDB project to communicated between the debugger and the client application. As it turns out, this is a huge hassle. I thought that building Trident's network infrastructure back in 2016 was a hassle, but without Netty, I ended up with a huge amount of unmaintainable spaghetti code in the client application. Thank God it worked without having to make too many changes.

The point of calamity is to get rid of the calamity that is working with byte arrays and streams. Something that mixes ByteArrayOutputStream with the flexibility of ByteBuf and the compatibility with Java's native streams. This is still a WIP project as I spare my free time to work on my actual job and doing other gaming (read: procrastination) related activities.
