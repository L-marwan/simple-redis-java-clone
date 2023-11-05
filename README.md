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