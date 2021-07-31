import json
from tanserver import *


# API: register
def register(json_obj):
    user = json_obj.get('username')
    pwd  = json_obj.get('password')

    if user == None or pwd == None:
        # Send {"status":-1,"message":"missing username or password","result":{}}
        return json_append_status('{}', -1, 'missing username or password')

    try:
        res = pg_query('127.0.0.1',
                       'select row_to_json(users) from users where username = $1',
                       user)

        data = {}

        if res != '': # This username already exists.
            data['status'] = -1
        else:
            pg_query('127.0.0.1',
                     'insert into users values($1, $2)',
                     user, pwd)

            # Registration success!
            data['status'] = 0

        return json.dumps(data)
    except:
        # Send {"status":-2,"message":"query failed","result":{}}
        return json_append_status('{}', -2, 'query failed')


# API: login
def login(json_obj):
    user = json_obj.get('username')
    pwd  = json_obj.get('password')

    if user == None or pwd == None:
        return json_append_status('{}', -1, 'missing username or password')

    try:
        res = pg_query("127.0.0.1",
                       "select row_to_json(users) from users where username = $1 and password = $2",
                       user, pwd)

        data = {}

        if res != '': # Login OK
            data["status"] = 0
        else:
            data["status"] = -1

        return json.dumps(data)
    except:
        return json_append_status('{}', -2, 'query failed')
