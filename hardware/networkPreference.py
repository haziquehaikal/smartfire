import sys, time
import urllib3 #networking library
import json

class networkPreference():

    def __init__(self):
        self.object=""
        self.value=""
        self.http = urllib3.PoolManager()
        # self.url="http://192.168.43.33/smarthome/public/api/smartfire/mobile/"
        # self.url="https://staging.uni10smarthome.com/api/smartfire/mobile/" #staging
        self.url="http://192.168.43.124/smarthome/public/api/smartfire/mobile/"

    def postData(self,payload,endpoint):
        encoded_data = json.dumps(payload).encode('utf-8')#create JSON object
        postaction = self.http.request(
            'POST',
            self.url + endpoint,
            #self.url+self.api,#IP add server
            body=payload,
            headers={'Content-Type': 'application/json'} )
        print(postaction.data)
        return postaction 
        #json.loads(r.data.decode('utf-8'))['json']

    def getData(self,endpoint):        
        
        getaction = self.http.request(
            'GET',
            self.url + endpoint)
        return getaction.data