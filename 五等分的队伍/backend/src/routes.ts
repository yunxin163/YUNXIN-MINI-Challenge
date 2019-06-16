import * as Router from 'koa-router';
import controller = require('./controller');
import * as passport from 'koa-passport';
import * as multer from 'koa-multer';

const router = new Router();

const jwtAuth = passport.authenticate('jwt', { session: false });


const storage  = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, 'uploads/avatar/')
  },
  filename: (req, file, cb) => {
    const suffix = (file.originalname).split(".");
    cb(null, Date.now() + '.' + suffix[suffix.length - 1]);
  }
});
const uploads = multer({ storage });

// USER ROUTES
router.post('/user/login', controller.User.login);
router.post('/user/getSms', controller.User.getSMScode);
router.post('/user/validateSms', controller.User.validateSMScode);
router.post('/user/register', controller.User.addUser);
router.get('/user/getInfo', jwtAuth, controller.User.info);
router.post('/user/logout', jwtAuth, controller.User.logout);
router.post('/user/uploadAvatar', jwtAuth, uploads.single('avatar'), controller.User.uploadAvatar);
router.get('/user/getFriends', jwtAuth, controller.User.friends);
router.post('/user/addFriend', jwtAuth, controller.User.addFriend);
router.post('/user/editUser', jwtAuth, controller.User.editUser);
router.post('/user/getNeteaseToken', jwtAuth, controller.User.getNeteaseToken);


export { router };