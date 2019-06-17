import * as passport from 'koa-passport';
import { ExtractJwt, Strategy as JwtStrategy } from 'passport-jwt';

import { User } from './entity/User';
import { getManager, Repository } from 'typeorm';

export const PRIVATE_KEY = 'SimpleAdmin_JWT_Secret';
const opts: any = {};
opts.secretOrKey = PRIVATE_KEY;
opts.jwtFromRequest = ExtractJwt.fromAuthHeaderAsBearerToken();
passport.use(new JwtStrategy(opts, async (jwt_payload, done) => {
  if (jwt_payload.id) {
    const userRepository: Repository<User> = getManager().getRepository(User);
    const user = await userRepository.findOne({ id: jwt_payload.id, is_delete: false });
    done(null, user);
  } else {
    done(null, false);
  }
}));

passport.serializeUser((user: any, done) => {
  done(null, user.id)
});

passport.deserializeUser(async (id: number, done) => {
  const userRepository: Repository<User> = getManager().getRepository(User);
  const user = await userRepository.findOne({ id, is_delete: false }, {
    select: ['id']
  });
  done(null, user);
});

