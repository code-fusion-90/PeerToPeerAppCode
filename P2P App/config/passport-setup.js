const passport = require('passport');
const LinkedinStrategy = require('passport-linkedin-oauth2').Strategy;
const FacebookStrategy = require('passport-facebook').Strategy;
const TwitterStrategy = require('passport-twitter').Strategy;
const pool = require('../models/database-connection');
const crud = require('../models/database-crud');


passport.serializeUser((user, done)=>{
	console.log(user);
	done(null, user);


});

passport.deserializeUser((user, done)=>{

    // fetch id from db
	//done(null, user.id);
	done(null, user);
	console.log("deserailzed***********"+user);

});



passport.use(
	new LinkedinStrategy({
		//options for the google strat
		clientID: '',
  		clientSecret: '',
  		callbackURL: "/auth/linkedin/success",
  		//callbackURL: "/auth/google/redirect",
  		scope: ['r_emailaddress', 'r_basicprofile','r_liteprofile'],

	}, (accessToken, refreshToken, profile, done) => {

		//passport callback function
		console.log('passport callback function');
		//console.log(profile);
		//console.log(profile._json.currentShare.author);
		//console.log(accessToken);
		  
		crud.insert('UserData', { 
		  					Type: 'linkedin', 
		  					SocialID: profile.id, 
		  					Name: profile._json.formattedName,
		  					Email: profile._json.emailAddress,
		  					Phone: null,
		  					DOB: null,
		  					ProfilePic: profile._json.pictureUrl
		 });

		crud.insert('Companies', { 
		  					Type: 'linkedin', 
		  					SocialID: profile.id, 
		  					CompanyID: profile._json.positions.values[0].company.id,
		  					CompanyName: profile._json.positions.values[0].company.name,
		  					Role: profile._json.positions.values[0].title,
		  					Duration: profile._json.positions.values[0].startDate.year
		 });


		crud.insert('SocialSessions', { 
		  					Type: 'linkedin', 
		  					SocialID: profile.id, 
		  					accToken: accessToken,
		  					NoOfCompanyPositions: profile._json.positions._total,
		  					NoOfConnections: profile._json.numConnections,
		  					Location: profile._json.location.name,
		  					pictureUrl: profile._json.pictureUrl,
		  					Headline: profile._json.headline,
		  					Summary: profile._json.summary
		 });

		done(null, profile.id);

		
		//crud.read();
		
		//crud.insert('UserData', "Type, SocialId, Name, Email, Phone, DOB, ProfilePic", values);
	})

);


passport.use(
	new FacebookStrategy({
		//options for the google strat
		clientID: '',
  		clientSecret: '',
  		callbackURL: "/auth/facebook/success",
  		//callbackURL: "/auth/google/redirect",
  		//scope: ['r_emailaddress', 'r_basicprofile'],
  		 profileFields: ['id', 'displayName', 'birthday', 'email','gender','location', 'relationship_status', 'religion', 
  		 							'significant_other', ,'website','accounts', 'work','is_shared_login']

	}, (accessToken, refreshToken, profile, done) => {

		//passport callback function
		console.log('facebook passport callback function');
		console.log(profile);
		console.log(accessToken);
	})

);



passport.use(
	new TwitterStrategy({
		//options for the google strat
		consumerKey: '81pbms292luf2f',
  		consumerSecret: '1cX4ymzbd6FeM0EA',
  		callbackURL: "/auth/twitter/success",
  		//callbackURL: "/auth/google/redirect",
  		//scope: ['r_emailaddress', 'r_basicprofile'],


	}, (accessToken, refreshToken, profile, done) => {

		//passport callback function
		console.log('twitter passport callback function');
		console.log(profile);

	})

);