var firebaseConfig = {
    apiKey: "AIzaSyBZqUc8-DvSjpI1a5nn35LrZXUO3Lc0fi8",
    authDomain: "campusfacilitybooking.firebaseapp.com",
    projectId: "campusfacilitybooking",
    storageBucket: "campusfacilitybooking.appspot.com",
    messagingSenderId: "43196823541",
    appId: "1:43196823541:web:7afd5d4b210a2fd86f12bc",
    measurementId: "G-93DEX0DPBM"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  firebase.analytics();


  
  document.getElementById("classroom_add_id").addEventListener("submit",(e)=>{
    var buildingname = document.getElementById("buildingname").value;
    var department = document.getElementById("department").value;
    var classroom_name = document.getElementById("classroom-name").value;
    var strength = document.getElementById("strength").value;
    console.log(buildingname);
    e.preventDefault();
    createTask(buildingname,department,classroom_name,strength);
    //classroom_add.reset();
  
  });

  function createTask(buildingname,department,classroom_name,strength)
{
    var new_class = {
        BuildingName:buildingname,
        Department:department,
        Name:classroom_name,
        Strength:strength
    }
    let db = firebase.firestore().collection("ClassRooms/");
    db.add(new_class).then(()=>{
       Swal.fire(
           'Good job!',
           'New Class Addded!',
           'success'
       )
      
    })
}

function deleteTask(id)
{
    firebase.firestore().collection("ClassRooms").doc(id).delete().then(()=>{
        Swal.fire(
            'Good job!',
            'Class Removed!',
            'Success'
        )
    })

}

  
function updateTask(id,name,buildingname,department,strength)
{
    
    document.getElementById("classroom_edit_id").addEventListener("submit",(e)=>{
        e.preventDefault();
    });
    
    document.getElementById("button2").addEventListener("click",(e)=>{
        updateTask2(id,document.getElementById("buildingname1").value,document.getElementById("department1").value,
        document.getElementById("classroom-name1").value,document.getElementById("strength1").value);
    });
    document.getElementById("buildingname1").value = buildingname;
    document.getElementById("department1").value = department;
    document.getElementById("classroom-name1").value = name;
    document.getElementById("strength1").value = strength;

}
function updateTask2(id,buildingname,department,name,strength)
{
    var classUpdated = {
      BuildingName:buildingname,
      Department:department,
      Name:name,
      Strength:strength
    }
    let db = firebase.firestore().collection("ClassRooms").doc(id);
    db.set(classUpdated).then(()=>{
        Swal.fire(
            'Good job!',
            'Class Updated',
            'Success'
        )
    })


    readTask();

}

function readTask()
{
    firebase.firestore().collection("ClassRooms").onSnapshot(function(snapshot){
        document.getElementById("ClassSection").innerHTML='';
        snapshot.forEach(function(taskValue){
            document.getElementById("ClassSection").innerHTML+= `<div class="col col-class1">
            <div class="card" style="width: 18rem;">
              <div class="card-body">
                <h5 class="card-title">${taskValue.data().Name}</h5>
                <h6 class="card-subtitle mb-2 text-muted">${taskValue.data().BuildingName}</h6>
                <p class="card-text">${taskValue.data().Name} is present in ${taskValue.data().BuildingName} with strength of ${taskValue.data().Strength}</p>
                
                <button type="submit" style ="color:white" data-bs-toggle="modal" data-bs-target="#exampleModal" class = "btn btn-warning" 
                onclick = "updateTask('${taskValue.id}','${taskValue.data().Name}' , '${taskValue.data().BuildingName}','${taskValue.data().Department}','${taskValue.data().Strength}')"><i class = "fas
                fa=edit"></i>Edit</button>
                
                <button type = "submit" class = "btn btn-danger" onclick = "deleteTask('${taskValue.id}')">
                <i class = "fas fa-trash-alt"></i>Delete</button>
                 
              </div>
            </div>  
          </div>
            `

        });


    });
}



function readUsers()
{
    firebase.firestore().collection("Users").onSnapshot(function(snapshot){
        document.getElementById("UserSection").innerHTML='';
        snapshot.forEach(function(taskValue){
            document.getElementById("UserSection").innerHTML+= `<div class="col col-class1">
            <div class="card" style="width: 18rem;">
              <div class="card-body">
                <h5 class="card-title">${taskValue.data().name}</h5>
                <h6 class="card-subtitle mb-2 text-muted">${taskValue.data().email}</h6>
                <p class="card-text">${taskValue.data().mobile}</p>
                <button type = "submit" class = "btn btn-danger" onclick="sendemail('${taskValue.data().email}')" > 
                <i class = "fas fa-trash-alt"></i>View History</button>
              </div>
            </div>  
          </div>
            `

        });


    });
}

function sendemail(email)
{
    localStorage.setItem("emailfromuser",email)
    window.location.href='/booking_history.html'
}

function readhistory()
{
    var mail = localStorage.getItem("emailfromuser")
    firebase.firestore().collection("BookingHistory").onSnapshot(function(snapshot){
        document.getElementById("HistorySection").innerHTML='';
        snapshot.forEach(function(taskValue){
            if(taskValue.data().bookedBy==mail){
            document.getElementById("HistorySection").innerHTML+= `<div class="col col-class1">
            <div class="card" style="width: 18rem;">
              <div class="card-body">
                <h5 class="card-title">${taskValue.data().building}</h5>
                <h6 class="card-subtitle mb-2 text-muted">${taskValue.data().facilityName}</h6>
                <p class="card-text">${taskValue.data().date}</p>
                
              </div>
            </div>  
          </div>
            `
            }
        });


    });
}