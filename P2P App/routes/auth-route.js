const router = require('express').Router();
const passport = require('passport');
const crud = require('../models/database-crud');

//android register user
router.post('/register', (req, res)=>{

	var values = {

		uniqueId: Math.floor(Math.random() * (99999 - 1000 +1)) + 1000,
		name: req.body.name,
		email: req.body.email,
		encrypted_password: req.body.password
	};

	crud.read('RegisteredUsers',[req.body.email], "email = ?", (err, result)=>{
		console.log('in callback');
		if(result && result.length){
			res.json('User Already Exists');
		}else{
			crud.insert('RegisteredUsers', values);
			res.json('User registered Sucessfully');
		}
	});

	



});

//android login user
router.post('/loginuser', (req, res)=>{

	var values = {

		email: req.body.email,
		encrypted_password: req.body.password
	};
	

	crud.read('RegisteredUsers',[req.body.email, req.body.password], "email = ? and encrypted_password = ?", (err, result)=>{
		console.log('in login callback');
		console.log(result[0]);
		if(result && result.length){
			res.json(result[0]);
		}else{
			//crud.insert('RegisteredUsers', values);
			res.json('Incorrect Password');
		}
	});



});





// auth login router

router.get('/login', (req, res)=>{
	res.render('login');

});


// auth logout router

router.get('/logout', (req, res)=>{
	//handle with passport
	res.send('loggin out');

});


// ************* auth with linkedin & callback route for linkedin to redirect
router.get('/linkedin', passport.authenticate('linkedin', { state: '2223334'}), 
	(req, res)=>{

	}
);

router.get('/linkedin/success',passport.authenticate('linkedin'), (req, res)=>{
	res.send('you reached the linkedin callback uri');
});



// ************* auth with facebook & callback route for facebook to redirect
router.get('/facebook', passport.authenticate('facebook', { scope: ['user_likes','user_events', 'user_birthday','instagram_basic'] }), 
	(req, res)=>{

	}
);


router.get('/facebook/success',passport.authenticate('facebook'), (req, res)=>{
	res.send('you reached the facebook callback uri');
});


// ************* anddroid post with retrofit for fb
router.post('/android/facebook', (req, res)=>{

	console.log(req.body);

		var values =    { 
		  					Type: 'facebook', 
		  					SocialID: req.body.id,
		  					Name: req.body.name,
		  					Email: req.body.email,
		  					Phone: null,
		  					DOB: null
		 				}

	crud.read('UserData',[req.body.id], "SocialId = ?", (err, result)=>{
		console.log('in callback');
		if(result && result.length){
			res.json('User Already Exists');
		}else{
			crud.insert('UserData', values);
			res.json('Fb User entered Sucessfully');
		}
	});

});



// ************** auth with twitter & callback route for twitter to redirect
router.get('/twitter', passport.authenticate('twitter'), 
	(req, res)=>{

	}
);

router.get('/twitter/success',passport.authenticate('twitter'), (req, res)=>{
	res.send('you reached the twitter callback uri');
});



module.exports = router;