from flask import request
from flask import Flask
import os
import redis


app = Flask(__name__)
r = redis.StrictRedis(host="localhost", port=6379)


# CORS headers
@app.after_request
def add_cors_headers(response):
    response.headers.add('Access-Control-Allow-Origin', '*')
    response.headers.add('Access-Control-Allow-Credentials', 'true')
    response.headers.add('Access-Control-Allow-Headers', 'Content-Type')
    response.headers.add('Access-Control-Expose-Headers', 'X-Dimensions')
    return response


@app.route('/opal-simu/tasks/output', methods=['POST'])
def sendDataToRedis():
    print(str(request.data)) 
    with open('job_id', 'r') as job_file:
        r.set('proof.gleam.jobs.' + str(job_file.readline()) + '.result', request.data)
        r.incr('proof.gleam.jobs.done.count')
    return "ok"

if __name__ == '__main__':
    app.run(host="127.0.0.1", port=8060, threaded=True, debug=True, use_reloader=False)
