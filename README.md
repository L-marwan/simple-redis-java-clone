# simple-redis-java-clone

This is a simple clone of a Redis server, it started as part of the coding challenges:
https://codingchallenges.fyi/challenges/challenge-redis/

The main goal is to just try out new java features, it uses virtual threads, enhanced Strings, the new pattern matching syntax etc...


**todo :**
- [x] simple websocket server
- [x] handle each request in its thread (using virtual threads)
- [x] setup the redis-client and redis-benchmark
- [x] create a simple parser (just parse the simple string)
- [x] add more parsing (arrays and bulk strings)
- [x] implement a commands system

### Notes: 
* Setting up Redis cli on windows is not as smooth as it should be. There's no windows package for it, so the only option
is to either install inside WSL, or use a docker container, when I tried the WSL route, turns out it's very messy to access
the host from the guest WSL os when you also have docker installed. I ended up just using a VM to have redis installed and was 
able to use both redis-cli and redis-benchmark. 
* This implementation only supports either inline commands (responses should not end in "\r\n"), or normal commands, 
I still didn't figure out a way to know what kind of response is expected. 
* The performance is bad, and there isn't a way to know that the client is done, so the socket remains open. 
* I'm not sure if Virtual Threads were the right choice for this. 
* It was fun to implement this. 
* I tried to have a native build for this, but it was again painful to get it working on windows;
I just used a debian VM, the native application worked fine with 'build-native', but for some reason building the native image
with the maven plugin broke the Reflection part (the classes annotated with @Command were not picked up). 