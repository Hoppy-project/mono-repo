// const { User } = require('../models/User');

// let auth = (req, res, next) => {
//   // console.log('res111', response.payload)
//   // console.log('local', localStorage)
//   let token = req.cookies.w_auth;

//   User.findByToken(token, (err, user) => {
//     if (err) throw err;
//     if (!user)
//       return res.json({
//         isAuth: false,
//         error: true
//       });

//     req.token = token;
//     req.user = user;
//     next();
//   });
// };

// module.exports = { auth };
