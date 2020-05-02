const express = require("express");
const app = express();
var authRoutes = require('./routes/auth-route.js');
var paspportSetup = require('./config/passport-setup');
var mysql = require('mysql');
var dbconnection = require('./models/database-connection');
const cookieSession = require('cookie-session');
const passport = require('passport');
var bodyParser = require('body-parser');


//connect with db
dbconnection.getConnection((err, con)=>{
	if(err) console.log(err);
	console.log("Connected to db!");			
});


//set up view engine
app.set('view engine','ejs');


  app.use(bodyParser.json());
  app.use(bodyParser.urlencoded({extended: true}));

//setup cookie
app.use(cookieSession({
	maxAge: 24*60*60*1000,
	keys: ['encryptTheCookie']
}));


//initialize passport
app.use(passport.initialize());
app.use(passport.session());




//set up routes
app.use('/auth' , authRoutes);



//create home route
app.get('/', (req, res) => {

	res.render('home');

});


//Server listening on port 3000
app.listen(3000, ()=>{
		console.log('PJ@HOSETDev Server running on port 3000');

});
