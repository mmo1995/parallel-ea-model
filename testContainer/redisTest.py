import redis
import os
r = redis.StrictRedis()
test = r.incr("test")
c = 1
if c != 0:
    print "LAJBS"