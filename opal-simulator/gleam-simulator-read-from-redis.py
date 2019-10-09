import redis
import time

r = redis.StrictRedis(host='localhost', port=6379)
p = r.pubsub()
p.subscribe('proof.gleam')
while True:
    time.sleep(0.1)
    m = p.get_message()
    if m is not None and 'type' in m.keys() and 'channel' in m.keys():
        if m['type'] == 'message' and m['channel'] == b'proof.gleam':
            if m['data'] == b'data_available':
                job_id = r.incr('proof.gleam.container.job.id')
                with open('job_id', 'w') as job_file:
                    print(str(job_id))
                    job_file.write(str(job_id))
                data = r.get('proof.gleam.jobs.' + str(job_id))
                if data is not None:
                    with open('data2Simu.dat', 'wb') as f:
                        f.write(data)
                        print('-----***************----------***************-------------')
