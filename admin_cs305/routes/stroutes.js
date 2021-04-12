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
       getAllLabs
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


router.get('/users/:id',getBookingHistory)

module.exports = {
    routes: router
}