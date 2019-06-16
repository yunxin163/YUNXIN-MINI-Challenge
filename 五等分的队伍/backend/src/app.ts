import 'reflect-metadata';
import { createConnection } from 'typeorm';
import * as Koa from 'koa';
import * as session from 'koa-session';
import * as passport from 'koa-passport';
import * as bodyParser from 'koa-bodyparser';
import * as path from 'path';
import serve = require('koa-static');
import response from './middleware/response'
import { router } from './routes';
import { TcpServer } from './tcp';

createConnection().then(async connection => {

  // create koa app
  const app = new Koa();

  // cors
  const cors = require('@koa/cors')
  app.use(cors({
    credentials: true
  }));

  // response
  app.use(response);

  // body parser
  app.use(bodyParser());

  // Sessions
  app.keys = ['sessions-secret']
  app.use(session({}, app));

  // passport
  require('./auth')
  app.use(passport.initialize());
  app.use(passport.session());

  // router config
  app.use(router.routes());
  app.use(router.allowedMethods());

  // static
  app.use(serve(path.join(__dirname, '..', 'uploads')));

  app.listen(3000);

  console.log("Koa application is up and running on port 3000");

  // open tcp server
  const tcpServer = new TcpServer();
  tcpServer.listen();

}).catch(error => console.log("TypeORM connection error: ", error));
