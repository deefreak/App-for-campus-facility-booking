'use strict';

const firebase = require('../db');
const Classroom = require('../models/classroom');
const Sport = require('../models/sport');
const Lab = require('../models/lab');

const Student = require('../models/student');
const User = require('../models/user');
const BookingHistory = require('../models/bookinghistory')
const firestore = firebase.firestore();

const addClassroom = async(req,res,next) => {
    try {
        const data = req.body;
        await firestore.collection('ClassRooms').doc(req.body.Name).set(data);
        res.send('Record saved successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const addSport = async(req,res,next) => {
    try {
        const data = req.body;
        await firestore.collection('Sports').doc(req.body.Name).set(data)
        res.send('Record saved successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const addLab = async(req,res,next) => {
    try {
        const data = req.body;
        await firestore.collection('Labs').doc(req.body.Name).set(data);
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

const getAllLabs = async (req, res, next) => {
    try {
        const lab = await firestore.collection('Labs');
        const data = await lab.get();
        const labArray = [];
        if(data.empty) {
            res.status(404).send('No lab found');
        }else {
            data.forEach(doc => {
                const lab = new Lab(
                    doc.data().Name,
                    doc.data().BuildingName,
                    doc.data().Department,
                    doc.data().Equipments
                );
                labArray.push(lab);
            });
            res.render("../views/editlabs.ejs",{labArray})
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}


const getAllSports = async (req, res, next) => {
    try {
        const sport = await firestore.collection('Sports');
        const data = await sport.get();
        const sportArray = [];
        if(data.empty) {
            res.status(404).send('No sport found');
        }else {
            data.forEach(doc => {
                const sport = new Sport(
                    doc.data().Name
                );
                sportArray.push(sport);
            });
            res.render("../views/editsports.ejs",{sportArray})
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
const updateClassroom = async (req, res, next) => {
    try {
        const id = req.params.id;
        const classroom = await firestore.collection('ClassRooms').doc(id).get()
        const name = classroom.data().Name
        const buildingname = classroom.data().BuildingName
        const strength = classroom.data().Strength
        const department = classroom.data().Department
        res.render('../views/update.ejs',{name,buildingname,strength,department})        
    } catch (error) {
        res.status(400).send(error.message);
    }
}
const editThisClassroom = async (req, res, next) => {
    try {
        const id = req.params.id;
        const data = req.body;
        const classroom = await firestore.collection('ClassRooms').doc(id)
        await classroom.update(data)
        res.send("Updated")     
    } catch (error) {
        res.status(400).send(error.message);
    }
}


const deleteClassRoom = async (req, res, next) => {
    try {
        const id = req.params.id;
        const classroom = await firestore.collection('ClassRooms').doc(id).delete();
        res.send("Deleted Successfully");
    } catch (error) {
        res.status(400).send(error.message);
    }
}
const deleteSports = async (req, res, next) => {
    try {
        const id = req.params.id;
        const classroom = await firestore.collection('Sports').doc(id).delete();
        res.send("Deleted Successfully");
    } catch (error) {
        res.status(400).send(error.message);
    }
}
const deleteLabs = async (req, res, next) => {
    try {
        const id = req.params.id;
        const classroom = await firestore.collection('Labs').doc(id).delete();
        res.send("Deleted Successfully");
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
    getClassRoom,
    addSport,
    getAllSports,
    addLab,
    getAllLabs,
    deleteSports,
    deleteLabs,
    updateClassroom,
    editThisClassroom
}