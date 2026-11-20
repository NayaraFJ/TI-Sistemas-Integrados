const http = require('http');
const fs = require('fs');
const path = require('path');
const root = __dirname;
const types = { '.html':'text/html; charset=utf-8', '.css':'text/css; charset=utf-8', '.js':'text/javascript; charset=utf-8', '.json':'application/json; charset=utf-8' };
http.createServer((request, response) => {
  const pathname = decodeURIComponent(new URL(request.url, 'http://localhost').pathname);
  const relative = pathname === '/' ? 'index.html' : pathname.replace(/^\/+/, '');
  const target = path.resolve(root, relative);
  if (!target.startsWith(root) || !fs.existsSync(target) || fs.statSync(target).isDirectory()) {
    response.writeHead(404, { 'content-type': 'text/plain; charset=utf-8' }); response.end('Não encontrado'); return;
  }
  response.writeHead(200, { 'content-type': types[path.extname(target)] || 'application/octet-stream', 'cache-control':'no-store' });
  fs.createReadStream(target).pipe(response);
}).listen(4173, () => console.log('SIGE Desk disponível em http://localhost:4173'));
