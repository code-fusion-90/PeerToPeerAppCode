const pool = require('../models/database-connection');
const mysql = require('mysql');


var crud = {

	insert: (table, values)=>{
			insert(table, values);
		},
	read: (table, values, whereclause, callback) => {
		read(table, values, whereclause, callback);
	}
}


//insert to db
function insert(table, values){
	
	console.log('in insert function');

	var sql = "INSERT INTO "+table+" SET ?";

	pool.query(sql, values, (err, res) => {
			 
			 if(err) throw err;
			  console.log('Last insert ID:', res.insertId);
	});

}


//read from db
function read(table, values, whereclause, callback){
	//console.log(table, values, whereclause);

	var sql = "Select * from "+table+" where "+whereclause;
	console.log(sql);
	pool.query(sql, values, (err, result) => {
			 
			 if(err) {
			 	console.log(err);
			 	return callback(err, null);
			 }
			 else{
			  //console.log('Last insert ID:', result);
			  console.log(result);
			 return callback(null, result);

			}
	});


}

module.exports = crud;