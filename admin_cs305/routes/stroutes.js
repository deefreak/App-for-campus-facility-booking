const express = require('express');

const firebase = require('../db');
const firestore = firebase.firestore();

const lib = require("../globals/global.js");
const {
       updateStudent,
       deleteClassRoom,
       addClassroom,
       getAllUsers,
       getAllClassRooms,
       getAllClassRooms1,
       getBookingHistory,
       getClassRoom,
       addSport,
       getAllSports,
       addLab,
       getAllLabs,
       deleteSports,
       deleteLabs,
       updateClassroom,
       editThisClassroom,

       updateLab,
       editThisLab,
       
       updateSport,
       editThisSport,
       

       deleteCollection,
       addslot,
       getAllDateBookings
       
      } = require('../controllers/controller');

const router = express.Router();


router.get('/',(req,res) => {
  
    if(lib.login=='true')
    res.render("../views/home.ejs");
    else
    res.render("../views/login.ejs");
})

router.get('/login',(req,res) => {
  if(lib.login=='true') 
  res.redirect('/');
  else
  res.render("../views/login.ejs")
})

router.post('/getlogin',function(req,res,next){
  

  var username = req.body.exampleInputEmail1;
  var password = req.body.exampleInputPassword1;

  console.log('start');
  console.log(username);
  console.log(password);
  
  
  if(username == "admin@gmail.com" && password == "Admin123")
  {
    lib.login  = 'true';
    console.log('worked');
  }
  else
  {
    console.log('not');
  }
  res.redirect('/');

})

router.get('/classrooms',(req,res) => {
    if(lib.login=='false')
    res.redirect('/login');

    res.render("../views/classrooms.ejs")
})
router.get('/sports',(req,res) => {
  if(lib.login=='false')
  res.redirect('/login');

    res.render("../views/sports.ejs")
})

router.get('/labs',(req,res) => {
  if(lib.login=='false')
  res.redirect('/login');

    res.render("../views/labs.ejs")
})


router.get('/addslot',(req,res) => {
  if(lib.login=='false')
    res.redirect('/login');
    
    res.render("../views/selectdate.ejs")
})

router.get('/logout',(req,res)=>{
  lib.login =  'false';
  res.redirect('/');
})
router.get('/delete',deleteCollection)

router.get('/addslotfor7',addslot)
//router.get('/deleteupcoming',deleteupcomingslots)
 



router.get('/users',getAllUsers)

router.post('/classrooms',addClassroom)

router.get('/classrooms/edit',getAllClassRooms)

router.post('/sports',addSport)
router.get('/sports/editsports',getAllSports)

router.post('/labs',addLab)
router.get('/labs/editlabs',getAllLabs)
router.post('/classrooms/edit/:id',deleteClassRoom)
router.post('/sports/editsports/:id',deleteSports)
router.post('/labs/editlabs/:id',deleteLabs)
router.post('/bookings',getAllDateBookings)
 


router.get('/users/:id',getBookingHistory)

router.get('/classrooms/edit/:id/editthis',updateClassroom)
router.post('/classrooms/edit/:id/editthis',editThisClassroom)

router.get('/sports/editsports/:id/editthis',updateSport)
router.post('/sports/editsports/:id/editthis',editThisSport)

router.get('/labs/editlabs/:id/editthis',updateLab)
router.post('/labs/editlabs/:id/editthis',editThisLab)



//ADD slot section------------
router.get('/addslot/:DATE',getAllClassRooms1);

router.post('/addslot/submit',function(req,res,next){
    var DATE = req.body.date;
    res.redirect('/addslot/'+DATE);
 
})

router.get('/addslot/:DATE/:CLASSNAME/:TYPE',function(req,res,next)
{
    const DATE = req.params.DATE;
    const CLASSNAME = req.params.CLASSNAME;
    const TYPE  = req.params.TYPE;
    res.render("../views/additem.ejs",{DATE,CLASSNAME,TYPE});
      
})

router.post('/addslot/:DATE/:CLASSNAME/:TYPE',async function(req,res,next){
   var date = req.params.DATE;
   const docname = req.params.CLASSNAME;
   const type = req.params.TYPE;
   const data = {};
   if(req.body.btncheck1)
     data['09to10'] = 'acadSlots';
   if(req.body.btncheck2)
     data['13to14'] = 'acadSlots';
   if(req.body.btncheck3)
     data['16to17'] = 'acadSlots';

   if(req.body.btncheck4)
     data['09to10'] = 'available';
   if(req.body.btncheck5)
     data['13to14'] = 'available';
   if(req.body.btncheck6)
     data['16to17'] = 'available';

     data['type'] = type;
   date = date.toString();
   var newdate = '';
   newdate = date[8] + date[9] + date[5] + date[6] + date[0] + date[1] + date[2] + date[3];
   

   await firestore.collection(newdate).doc(docname).set(data);
   res.redirect('/addslot/'+date);


})



module.exports = {
    routes: router
}