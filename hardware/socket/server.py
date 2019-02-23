import websockets
import time
import asyncio
import time

try:

    async def server(websocket, path):

        while True:
            message = await websocket.recv()
            print(f"< {message}")


    print("TEMP DATA FROM PI")
    start_server = websockets.serve(server, '192.168.0.56', 8765)

    asyncio.get_event_loop().run_until_complete(start_server)
    asyncio.get_event_loop().run_forever()

except KeyboardInterrupt:
    print("sdsds")