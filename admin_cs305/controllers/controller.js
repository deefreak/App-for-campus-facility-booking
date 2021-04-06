'use strict';

const firebase = require('../db');
const Classroom = require('../models/classroom');
const Student = require('../models/student');
const User = require('../models/user');
const BookingHistory = require('../models/bookinghistory')
const firestore = firebase.firestore();

const addClassroom = async(req,res,next) => {
    try {
        const data = req.body;
        await firestore.collection('ClassRooms').doc().set(data);
        res.send('Record saved successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getAllUsers = async (req, res, next) => {
    try {
        const users = await firestore.collection('Users');
        const data = await users.get();
        const userArray = [];
        if(data.empty) {
            res.status(404).send('No student record found');
        }else {
            data.forEach(doc => {
                const user = new User(
                    doc.data().id,
                    doc.data().email,
                    doc.data().name,
                    doc.data().mobile
                );
                userArray.push(user);
            });
            res.render("../views/users.ejs",{userArray})
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}
const getAllClassRooms = async (req, res, next) => {
    try {
        const classroom = await firestore.collection('ClassRooms');
        const data = await classroom.get();
        const classroomArray = [];
        if(data.empty) {
            res.status(404).send('No student record found');
        }else {
            data.forEach(doc => {
                const classroom = new Classroom(
                    doc.data().Name,
                    doc.data().BuildingName,
                    doc.data().Department,
                    doc.data().Strength
                );
                classroomArray.push(classroom);
            });
            res.render("../views/edit.ejs",{classroomArray})
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getClassRoom = async (req,res,next) => {
    try {
        const id = req.params.id;
        const history = await firestore.collection('ClassRooms');
        const data = await history.get()
        const classroomArray = [];
        data.forEach(doc => {
            if(doc.data().Name == id){
                const classroom = new Classroom(
                    doc.data().Name,
                    doc.data().BuildingName,
                    doc.data().Department,
                    doc.data().Strength
                );
                classroomArray.push(classroom);
            }
        });
        res.render('../views/singleClassroom.ejs',{classroomArray})
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getBookingHistory = async (req, res, next) => {
    try {
        const id = req.params.id;
        const history = await firestore.collection('BookingHistory');
        const data = await history.get()
        const historyArray = [];
        data.forEach(doc => {
            if(doc.data().bookedBy == id){
                const myHistory = new BookingHistory(
                    doc.data().bookedBy,
                    doc.data().building,
                    doc.data().date,
                    doc.data().facilityName,
                    doc.data().name,
                    doc.data().purpose,
                    doc.data().slot
                );
                historyArray.push(myHistory);
            }
        });
        res.render('../views/bookinghistory.ejs',{historyArray})
    } catch (error) {
        res.status(400).send(error.message);
    }
}


const updateStudent = async (req, res, next) => {
    try {
        const id = req.params.id;
        const data = req.body;
        const student =  await firestore.collection('students').doc(id);
        await student.update(data);
        res.send('Student record updated successfuly');        
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const deleteClassRoom = async (req, res, next) => {
    try {
        const id = req.params.id;
        res.send(id)
        const classroom = await firestore.collection('ClassRooms');
        const data = await classroom.get()
        data.forEach(doc => {
            if(doc.data().Name == id){
                doc.delete()
                res.render('/classrooms/edit')
            }
        })
    } catch (error) {
        res.status(400).send(error.message);
    }
}

module.exports = {
    updateStudent,
    addClassroom,
    getAllUsers,
    getAllClassRooms,
    getBookingHistory,
    deleteClassRoom,
    getClassRoom
}