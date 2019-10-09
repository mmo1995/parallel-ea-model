#!/bin/bash
python gleam-simulator-read-from-redis.py & python gleam-simulator-write-to-redis.py & ./testOpalSimu_2 127.0.0.1 $1 $2