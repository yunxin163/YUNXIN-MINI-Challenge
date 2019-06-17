import { createConnection } from 'net';

const socket0 = createConnection({
  host: 'localhost',
  port: 3001
});
const socket1 = createConnection({
  host: 'localhost',
  port: 3001
});

socket0.on('connect', () => {
  console.log('c0 connect to server');
  socket0.write(JSON.stringify({
    cmd: 0,
    body: { username: '13777818202' }
  }))
})
socket0.on('data', data => {
  console.log(data.toString());
})

socket1.on('connect', () => {
  console.log('c0 connect to server');
  socket0.write(JSON.stringify({
    cmd: 0,
    body: { username: '13777818201' }
  }))
})

