import asyncio
import websockets
import random
import time



async def client():
    async with websockets.connect(
            'ws://192.168.0.6:8765') as websocket:
        # state = input("Motor state ")
       
        counter = 0
        while counter < 5:
            state = str(random.random() * 100)
            print(f"> {state}")
            time.sleep(2)
            await websocket.send(state)
            counter + 1

asyncio.get_event_loop().run_until_complete(client())
