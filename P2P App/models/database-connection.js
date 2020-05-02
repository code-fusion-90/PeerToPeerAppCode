const mysql = require('mysql');


var pool  = mysql.createPool({
				  connectionLimit : 50,
				  host            : 'localhost',
				  user            : 'root',
				  password        : 'root',
				  database        : 'p2p_db',
				  port			  : '8889'
	});







module.exports = pool;