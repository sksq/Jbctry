import json,httplib
connection = httplib.HTTPSConnection('10.8.0.1',8080)
connection.set_tunnel('api.parse.com', 443)
connection.connect()
connection.request('POST', '/1/classes/DataContent', json.dumps({
       "tags": ["Movie","Science"],
       "file": {
         "name": "tfss-9af61142-f6dd-45a1-8fd9-1c5160f59dfb-pic.jpg",
         "__type": "File"
       },
       "category" : 0
     }), {
       "X-Parse-Application-Id": "iNUUU9wcOd9ICQbsLuIbxEuymmB8YltZERMjjAQS",
       "X-Parse-REST-API-Key": "HxFMo2YMGo9MV5Pj6ExR4U4BFJ3ZbHQ53bAAhTnk",
       "Content-Type": "application/json"
     })
result = json.loads(connection.getresponse().read())
print result



# { u'url': u'http://files.parsetfss.com/60cd5da0-b80f-4509-81c1-33505a8981b6/tfss-9af61142-f6dd-45a1-8fd9-1c5160f59dfb-pic.jpg', u'name': u'tfss-9af61142-f6dd-45a1-8fd9-1c5160f59dfb-pic.jpg'}