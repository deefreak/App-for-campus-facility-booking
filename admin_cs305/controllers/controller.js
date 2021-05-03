'use strict';

const firebase = require('../db');
const Classroom = require('../models/classroom');
const Sport = require('../models/sport');
const Lab = require('../models/lab');

const Student = require('../models/student');
const User = require('../models/user');
const BookingHistory = require('../models/bookinghistory')
const firestore = firebase.firestore();

const lib = require("../globals/global.js");

const addClassroom = async(req,res,next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const data = req.body;
        await firestore.collection('ClassRooms').doc(req.body.Name).set(data);
        res.send('Record saved successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const deleteCollection = async(req,res,next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try {
        /*const date = new Date()
        const year = date.getFullYear().toString()
        let month = date.getMonth()
        month = month + 1
        if(month < 10){
            month = "0" + month.toString()
        }
        let day = date.getDate()
        if(day<10){
            day = "0" + day.toString()
        }
        const today_date = day.toString() + month.toString() + year*/

        var d = new Date();
        d.setDate(d.getDate()).toString();
        var today_dat = d.toDateString()

        var month1 = new Map([['Jan','01'],['Feb','02'],['Mar','03'],['Apr','04'],['May','05'],['Jun','06'],['Jul','07'],['Aug','08'],['Sep','09'],['Oct','10'],['Nov','11'],['Dec','12']]);

        const today_date = today_dat.substring(8,10) + month1.get(today_dat.substring(4,7)) + today_dat.substring(11,15)
        var i = 1
        var c = 0
        var list = []
        while(true){

            var x = new Date()
            x.setDate(x.getDate() - i).toString();
            var prev = x.toDateString()
            var prev_date = prev.substring(8,10) + month1.get(prev.substring(4,7)) + prev.substring(11,15)
            const users = await firestore.collection(prev_date);
            const data = await users.get();
            
            if(data.empty) {
                c = c+1
                if(c == 5){
                    res.render('../views/selectdate.ejs')
                    break
                }    
            }else {
                c = 0
                data.forEach(doc => {
                    firestore.collection(prev_date).doc(doc.id).delete()    
                });
            }
            i=i+1
        }
        
    } catch (error) {
        res.status(400).send(error.message);
    }
    
}
/*const deleteupcomingslots = async(req,res,next) => {
    try {
        const date = new Date()
        const year = date.getFullYear().toString()
        let month = date.getMonth()
        month = month + 1
        if(month < 10){
            month = "0" + month.toString()
        }
        let day = date.getDate()
        if(day<10){
            day = "0" + day.toString()
        }
        const today_date = day.toString() + month.toString() + year

        var d = new Date();
        d.setDate(d.getDate()).toString();
        var today_dat = d.toDateString()

        var month1 = new Map([['Jan','01'],['Feb','02'],['Mar','03'],['Apr','04'],['May','05'],['Jun','06'],['Jul','07'],['Aug','08'],['Sep','09'],['Oct','10'],['Nov','11'],['Dec','12']]);

        const today_date = today_dat.substring(8,10) + month1.get(today_dat.substring(4,7)) + today_dat.substring(11,15)
        var i = 1
        var c = 0
        var list = []
        while(i<7){

            var x = new Date()
            x.setDate(x.getDate() + i).toString();
            var prev = x.toDateString()
            var prev_date = prev.substring(8,10) + month1.get(prev.substring(4,7)) + prev.substring(11,15)
            const users = await firestore.collection(prev_date);
            const data = await users.get();
            
            if(data.empty == false) {
                data.forEach(doc => {
                    firestore.collection(prev_date).doc(doc.id).delete()    
                });
            }
            i=i+1
            
        }
        res.render('../views/selectdate.ejs')
        
    } catch (error) {
        res.status(400).send(error.message);
    }
    
}*/

const addSport = async(req,res,next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const data = req.body;
        await firestore.collection('Sports').doc(req.body.Name).set(data)
        res.send('Record saved successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getAllDateBookings = async(req,res,next) => {
    try {
        const date = req.body.date;
        var newdate = '';
        newdate = date[8] + date[9] + date[5] + date[6] + date[0] + date[1] + date[2] + date[3];
        const history = await firestore.collection('BookingHistory');
        const data = await history.get()
        const historyArray = [];
        data.forEach(doc => {
            var datee = doc.data().date
            var bookdate = datee.substring(0,2) + "-" + datee.substring(2,4) + "-" + datee.substring(4,8)
            if(doc.data().date == newdate){
                const myHistory = new BookingHistory(
                    doc.data().bookedBy,
                    doc.data().building,
                    bookdate,
                    doc.data().facilityName,
                    doc.data().name,
                    doc.data().purpose,
                    doc.data().slot
                );
                historyArray.push(myHistory);
            }
        });
        res.render('../views/bookings.ejs',{historyArray})
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const addLab = async(req,res,next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const data = req.body;
        await firestore.collection('Labs').doc(req.body.Name).set(data);
        res.send('Record saved successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}
const getAllUsers = async (req, res, next) => {
    if(lib.login=='false')
    res.redirect('/login');
    console.log(lib);
    if(lib.login=='false')
    res.redirect('/'); 
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
                    doc.data().mobile,
                    doc.data().type
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
    if(lib.login=='false')
    res.redirect('/login');
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

const getAllClassRooms1 = async (req, res, next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const classroom = await firestore.collection('ClassRooms');
        const data = await classroom.get();
        const classroomArray = [];
        const datedata = [];
        if(data.empty) {
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
        } 
        const lab = await firestore.collection('Labs');
        const data1 = await lab.get();
        const labArray = [];
        if(data1.empty) {
            
        }else {
            data1.forEach(doc => {
                const lab = new Lab(
                    doc.data().Name,
                    doc.data().BuildingName,
                    doc.data().Department,
                    doc.data().Equipments
                );
                labArray.push(lab);
            });

            const sport = await firestore.collection('Sports');
        const data2 = await sport.get();
        const sportArray = [];
          if(data2.empty) {
        }else {
            data2.forEach(doc => {
                const sport = new Sport(
                    doc.data().Name
                );
                sportArray.push(sport);
            });
        }
            datedata.push(req.params.DATE);
            res.render("../views/allitems.ejs",{datedata , classroomArray,labArray,sportArray})
        }
    }
     catch (error) {
        res.status(400).send(error.message);
    }
}
const getAllLabs = async (req, res, next) => {
    if(lib.login=='false')
    res.redirect('/login');
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
    if(lib.login=='false')
    res.redirect('/login');
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
    if(lib.login=='false')
    res.redirect('/login');
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
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const id = req.params.id;
        const history = await firestore.collection('BookingHistory');
        const data = await history.get()
        const historyArray = [];
        data.forEach(doc => {
            var datee = doc.data().date
            var bookdate = datee.substring(0,2) + "-" + datee.substring(2,4) + "-" + datee.substring(4,8)
            if(doc.data().bookedBy == id){
                const myHistory = new BookingHistory(
                    doc.data().bookedBy,
                    doc.data().building,
                    bookdate,
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
    if(lib.login=='false')
    res.redirect('/login');
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
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const id = req.params.id;
        const classroom = await firestore.collection('ClassRooms').doc(id).get()
        const name = classroom.data().Name
        const buildingname = classroom.data().BuildingName
        const strength = classroom.data().Strength
        const department = classroom.data().Department
        const latitude =classroom.data().Latitude
        const longitude = classroom.data().Longitude
        res.render('../views/update.ejs',{name,buildingname,strength,department,latitude,longitude})        
    } catch (error) {
        res.status(400).send(error.message);
    }
}
const editThisClassroom = async (req, res, next) => {
    if(lib.login=='false')
    res.redirect('/login');
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

const updateSport = async (req, res, next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const id = req.params.id;
        const sport = await firestore.collection('Sports').doc(id).get()
        const name = sport.data().Name
        
        res.render('../views/updatesports.ejs',{name})        
    } catch (error) {
        res.status(400).send(error.message);
    }
}
const editThisSport = async (req, res, next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const id = req.params.id;
        const data = req.body;
        const sport = await firestore.collection('Sports').doc(id)
        await sport.update(data)
        res.send("Updated")     
    } catch (error) {
        res.status(400).send(error.message);
    }
} 

const updateLab = async (req, res, next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const id = req.params.id;
        const lab = await firestore.collection('Labs').doc(id).get()
        const name = lab.data().Name
        const buildingname = lab.data().BuildingName
        const equipments = lab.data().Equipments
        const department = lab.data().Department
        const latitude = lab.data().Latitude
        const longitude = lab.data().Longitude
        res.render('../views/updatelabs.ejs',{name,buildingname,equipments,department,latitude,longitude})        
    } catch (error) {
        res.status(400).send(error.message);
    }
}
const editThisLab = async (req, res, next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const id = req.params.id;
        const data = req.body;
        const lab = await firestore.collection('Labs').doc(id)
        await lab.update(data)
        res.send("Updated")     
    } catch (error) {
        res.status(400).send(error.message);
    }
}


const deleteClassRoom = async (req, res, next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const id = req.params.id;
        const classroom = await firestore.collection('ClassRooms').doc(id).delete();
        res.send("Deleted Successfully");
    } catch (error) {
        res.status(400).send(error.message);
    }
}
const deleteSports = async (req, res, next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const id = req.params.id;
        const classroom = await firestore.collection('Sports').doc(id).delete();
        res.send("Deleted Successfully");
    } catch (error) {
        res.status(400).send(error.message);
    }
}
const deleteLabs = async (req, res, next) => 
{
    if(lib.login=='false')
    res.redirect('/login');
    try {
        const id = req.params.id;
        const classroom = await firestore.collection('Labs').doc(id).delete();
        res.send("Deleted Successfully");
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const addslot = async (req, res, next) => {
    if(lib.login=='false')
    res.redirect('/login');
    try{
        const classroom = await firestore.collection('ClassRooms');
        const data = await classroom.get();
        const classroomArray = [];
        if(data.empty) {
            res.status(404).send('No student record found');
        }else {
            data.forEach(doc => {
                classroomArray.push(doc.data().Name);
            });
        }
        const lab = await firestore.collection('Labs');
        const data1 = await lab.get();
        const labArray = [];
        if(data1.empty) {
            res.status(404).send('No student record found');
        }else {
            data1.forEach(doc => {
                labArray.push(doc.data().Name);
            });
        }
        const sports = await firestore.collection('Sports');
        const data2 = await sports.get();
        const sportsArray = [];
        if(data2.empty) {
            res.status(404).send('No student record found');
        }else {
            data2.forEach(doc => {
                sportsArray.push(doc.data().Name);
            });
        }
        var d = new Date();
        d.setDate(d.getDate()).toString();
        var today_dat = d.toDateString()

        var month1 = new Map([['Jan','01'],['Feb','02'],['Mar','03'],['Apr','04'],['May','05'],['Jun','06'],['Jul','07'],['Aug','08'],['Sep','09'],['Oct','10'],['Nov','11'],['Dec','12']]);

        const today_date = today_dat.substring(8,10) + month1.get(today_dat.substring(4,7)) + today_dat.substring(11,15)
        var i = 0
        var c = 0
        var list = []
        var docData = {}
        docData['09to10'] = 'available'
        docData['10to11'] = 'available'
        docData['11to12'] = 'available'
        docData['12to13'] = 'available'
        docData['13to14'] = 'available'
        docData['14to15'] = 'available'
        docData['15to16'] = 'available'
        docData['16to17'] = 'available'
        docData['type'] = 'ClassRooms'
        var docData1 = {}
        docData1['09to12'] = 'available'
        docData1['12to15'] = 'available'
        docData1['15to18'] = 'available'
        docData1['type'] = 'Sports'
        var docData2 = {}
        docData2['09to10'] = 'available'
        docData2['10to11'] = 'available'
        docData2['11to12'] = 'available'
        docData2['12to13'] = 'available'
        docData2['13to14'] = 'available'
        docData2['14to15'] = 'available'
        docData2['15to16'] = 'available'
        docData2['16to17'] = 'available'
        docData2['type'] = 'Labs'
        var list = []
        while(i<7){

            var x = new Date()
            x.setDate(x.getDate() + i).toString();
            var prev = x.toDateString()
            var prev_date = prev.substring(8,10) + month1.get(prev.substring(4,7)) + prev.substring(11,15)
            const users = await firestore.collection(prev_date);
            const facility = await users.get();
                
                if(facility.empty){
                    classroomArray.forEach(doc => {
                        //const slots = firestore.collection(prev_date).doc(doc).get()
                        //if(slots.exists == false){
                            firestore.collection(prev_date).doc(doc).set(docData)
                        //}
                        list.push("yes")
                    });
                    labArray.forEach(doc => {
                        //const slots1 = firestore.collection(prev_date).doc(doc).get()
                        //if(slots1.empty){
                            firestore.collection(prev_date).doc(doc).set(docData2)
                        //}
                        list.push("yes")
                    });
                    sportsArray.forEach(doc => {
                        //const slots2 = firestore.collection(prev_date).doc(doc).get()
                        //if(slots2.empty){
                            firestore.collection(prev_date).doc(doc).set(docData1)
                        //}
                        list.push("yes")
                    });
                } 
                else{
                    for(let doc of classroomArray) {
                        const slots =  await users.doc(doc).get()
                        if(!slots.exists){
                            firestore.collection(prev_date).doc(doc).set(docData)
                        }
                        else{
                            list.push["yes"]
                        }
                        
                    }
                    for(let doc of labArray) {
                        const slots =  await users.doc(doc).get()
                        if(!slots.exists){
                            firestore.collection(prev_date).doc(doc).set(docData2)
                        }
                        else{
                            list.push["yes"]
                        }
                        
                    }
                    for(let doc of sportsArray) {
                        const slots =  await users.doc(doc).get()
                        if(!slots.exists){
                            firestore.collection(prev_date).doc(doc).set(docData1)
                        }
                        else{
                            list.push["yes"]
                        }
                        
                    }
                }   
   
            i=i+1
        }
        //res.send(list)
        res.render('../views/selectdate.ejs')
    } catch (error) {
        res.status(400).send(error.message);
    }
}

module.exports = {
    updateStudent,
    addClassroom,
    getAllUsers,
    getAllClassRooms,
    getAllClassRooms1,
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
    editThisClassroom,
    deleteCollection,
    addslot,
    updateSport,
    editThisSport,
    updateLab,
    editThisLab,
    getAllDateBookings
       
}