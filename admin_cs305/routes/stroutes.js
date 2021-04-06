const express = require('express');
const {
       updateStudent,
       deleteClassRoom,
       addClassroom,
       getAllUsers,
       getAllClassRooms,
       getBookingHistory,
       getClassRoom
      } = require('../controllers/controller');

const router = express.Router();

router.get('/',(req,res) => {
    res.render("../views/home.ejs")
})
router.get('/classrooms',(req,res) => {
    res.render("../views/classrooms.ejs")
})
router.get('/users',getAllUsers)
router.post('/classrooms',addClassroom)
router.get('/classrooms/edit',getAllClassRooms)
router.get('/users/:id',getBookingHistory)

module.exports = {
    routes: router
}