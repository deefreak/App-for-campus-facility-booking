import express from 'express';
import bodyParser from 'body-parser';



const app = express();

app.use(bodyParser.json({limit: "30mb",extended: true}));
app.use(bodyParser.urlencoded({limit: "30mb",extended: true}));
app.set('view engine', 'ejs')

app.get('/',(req,res) => {
    res.render('home.ejs')
})


app.get('/classrooms',(req,res) => {
    res.render('classrooms.ejs')
})
app.get('/users',(req,res) => {
    res.render('users.ejs')
})


const PORT = process.env.port || 5000;

app.listen(PORT,()=> console.log("Server Running"))
