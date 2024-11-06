var express = require('express');
var app = express();

app.use(express.json());

const sqlite3 = require('sqlite3').verbose();
// const db = new sqlite3.Database(':memory:');
const db = new sqlite3.Database('db.sqlite');

let cars = [
    {plateNumber: '1234', brand: 'Toyota', model: 'Corolla', year: 2015, color: 'white', price: 100},
    {plateNumber: '5678', brand: 'Honda', model: 'Civic', year: 2016, color: 'black', price: 120},
    {plateNumber: '9012', brand: 'Nissan', model: 'Sentra', year: 2017, color: 'red', price: 110}
];

db.serialize(function() {
    db.run(`CREATE TABLE IF NOT EXISTS cars (
        plateNumber TEXT PRIMARY KEY,
        brand TEXT,
        model TEXT,
        year INTEGER,
        color TEXT,
        price INTEGER
    )`);
    db.run(`CREATE TABLE IF NOT EXISTS rentals (
        INTEGER PRIMARY KEY,
        plateNumber TEXT,
        begin TEXT,
        end TEXT,
        active INTEGER,
        FOREIGN KEY (plateNumber) REFERENCES cars(plateNumber)
    )`);

    let stmt = db.prepare("INSERT OR IGNORE INTO cars VALUES (?, ?, ?, ?, ?, ?)");
    cars.forEach(car => {
        stmt.run(car.plateNumber, car.brand, car.model, car.year, car.color, car.price);
    });
    stmt.finalize();
});

app.get('/', function (req, res) {
    res.send(`
        Welcome to the car rental app:<br>
        - You can rent a car by sending a PUT request to /cars/:plateNumber?rent=true with begin and end dates in the body.<br>
        - You can return a car by sending a PUT request to /cars/:plateNumber?rent=false.<br>
        - You can get a list of cars by sending a GET request to /cars.<br>
        - You can get a car by sending a GET request to /cars/:plateNumber.
    `);
});

app.get('/cars', function (req, res) {
    db.all("SELECT C.*, IFNULL(R.active, 0) AS rented FROM cars C LEFT JOIN rentals R on R.plateNumber = C.plateNumber AND R.active = 1", function(err, rows) {
        res.send(rows);
    });
});

app.put('/cars/:plateNumber', function (req, res) {
    let plateNumber = req.params.plateNumber;
    db.get("SELECT C.*, IFNULL(R.active, 0) AS rented FROM cars C LEFT JOIN rentals R on R.plateNumber = C.plateNumber AND R.active = 1 WHERE C.plateNumber = ?", plateNumber, function(err, row) {
        if (row) {
            let rented = req.query.rent;
            if (rented === 'true') {
                let begin = req.body.begin;
                let end = req.body.end;
                if (row.rented) {
                    res.status(400).send('Car is already rented');
                }
                else if (begin && end) {
                    db.run("INSERT INTO rentals (plateNumber, begin, end, active) VALUES (?, ?, ?, 1)", plateNumber, begin, end);
                    res.status(200).send('Car is rented');
                }
                else {
                    res.status(400).send('Invalid rental dates');
                }
            }
            else if (rented === 'false') {
                if (!row.rented) {
                    res.status(400).send('Car is not rented');
                }
                else {
                    db.run("UPDATE rentals SET active = 0 WHERE plateNumber = ? AND active = 1", plateNumber);
                    res.status(200).send('Car is returned');
                }
            }
            else {
                res.status(400).send('Invalid query parameter');
            }
        }
        else {
            res.status(404).send('Car not found');
        }
    });
});

app.get('/cars/:plateNumber', function (req, res) {
    let plateNumber = req.params.plateNumber;
    db.get("SELECT C.*, IFNULL(R.active, 0) AS rented FROM cars C LEFT JOIN rentals R on R.plateNumber = C.plateNumber AND R.active = 1 WHERE C.plateNumber = ?", plateNumber, function(err, row) {
        if (row) {
            res.send(row);
        }
        else {
            res.status(404).send('Car not found');
        }
    });
});

app.listen(3000, function () {
    console.log('Rental app listening on port 3000!');
});