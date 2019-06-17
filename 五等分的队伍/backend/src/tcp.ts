import { createServer, Server, Socket } from 'net';
import { Buffer } from 'buffer';

class TcpServer {
  private server: Server
  private clients: Map<string, Socket>
  private targets: Map<string, string>;
  constructor() {
    this.server = createServer();
    this.clients = new Map<string, Socket>();
    this.targets = new Map<string, string>();
  }

  send(client: Socket, msg: string) {
    const buf = Buffer.alloc(200, "\0");
    const strbuf = Buffer.from(msg);
    strbuf.copy(buf, 0, 0, strbuf.length);
    client.write(buf);
    console.log('send to client:', buf.toString());
  }

  listen() {
    this.server.on('connection', c => {
      let self_username = '';
      c.on('data', data => {
        let msg = data.toString();
        try {
          const splits = msg.split("\0");
          if (splits.length > 0) msg = splits[0];
          const data = JSON.parse(msg);
          const cmd = data.cmd;
          const body = data.body || {};
          console.log('receive data:', data);
          switch (cmd) {
            case 0:
              if (body.username !== undefined) {
                this.clients.set(body.username, c);
                self_username = body.username;
                this.send(c, JSON.stringify({
                  cmd: 0,
                  body: { status: 1 }
                }));
              }
              break;

            case 1:
              console.log("cmd=1", self_username)
              if (body.target !== undefined && this.clients.has(self_username) && this.clients.has(body.target)) {
                this.targets.set(self_username, body.target);
                this.targets.set(body.target, self_username);
                this.send(this.clients.get(body.target),JSON.stringify({
                  cmd: 2,
                  body: { from: self_username }
                }));
                this.send(c, JSON.stringify({
                  cmd: 3,
                  body: { status: 1 }
                }));
              } else {
                this.send(c, JSON.stringify({
                  cmd: 3,
                  body: { status: 0 }
                }));
              }
              break;

            case 2:
                const target = this.targets.get(self_username);
                this.targets.delete(target);
                this.targets.delete(self_username);
                break;

            case 3:
              console.log("your target:", self_username, this.targets.get(self_username))
              if (this.targets.has(self_username) && this.clients.has(this.targets.get(self_username))) {
                this.send(this.clients.get(this.targets.get(self_username)),JSON.stringify({
                  cmd: 1,
                  body: body
                }));
              }
              break;

            default:
          }
        } catch (error) {
          console.log(error);
        }
      });

      c.on('close', err => {
        this.targets.delete(self_username);
        this.clients.delete(self_username);
        self_username = '';
      });
    });

    this.server.listen(3001, () => {
      console.log('tcp server is running on port 3001');
    })
  }
}

export { TcpServer }