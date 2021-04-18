const express = require('express');
const firebase = require('../db');
const firestore = firebase.firestore();

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
       deleteCollection
      } = require('../controllers/controller');

const router = express.Router();

router.get('/',(req,res) => {
    res.render("../views/home.ejs")
})

router.get('/classrooms',(req,res) => {
    res.render("../views/classrooms.ejs")
})
router.get('/sports',(req,res) => {
    res.render("../views/sports.ejs")
})

router.get('/labs',(req,res) => {
    res.render("../views/labs.ejs")
})


router.get('/addslot',(req,res) => {
    res.render("../views/selectdate.ejs")
})

router.get('/delete',deleteCollection)




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
 


router.get('/users/:id',getBookingHistory)
router.get('/classrooms/edit/:id/editthis',updateClassroom)
router.post('/classrooms/edit/:id/editthis',editThisClassroom)

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