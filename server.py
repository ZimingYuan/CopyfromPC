port = 1234

import os
import socket
from http.server import HTTPServer, BaseHTTPRequestHandler

class Resquest(BaseHTTPRequestHandler):
    def do_GET(self):
        r = os.popen('powershell Get-Clipboard')
        s = r.read()  
        r.close()  
        self.send_response(200)
        self.end_headers()
        self.wfile.write(s.encode())

host = ('0.0.0.0', port)
server = HTTPServer(host, Resquest)
print(socket.gethostbyname(socket.gethostname()) + ':' + str(port))
server.serve_forever()
