const express = require('express');
const {
       updateStudent,
       deleteClassRoom,
       addClassroom,
       getAllUsers,
       getAllClassRooms,
       getBookingHistory,
       getClassRoom,
       addSport,
       getAllSports,
       addLab,
       getAllLabs,
       deleteSports,
       deleteLabs,
       updateClassroom,
       editThisClassroom
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
module.exports = {
    routes: router
}