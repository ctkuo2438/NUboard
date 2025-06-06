import { useState } from "react";
import axios from 'axios';

function UpdateUser(){
    const[editedUser, setEditedUser] = useState({
        username: "",
        email: "",
        program: "",
        locationId: "",
        collegeId: "",
    });
    const[targetUser, setTargetUser] = useState();

    async function updateUser(id){
        try {
            await axios.put(`http://localhost:8080/api/users/${id}`, {
                    username: editedUser.username,
                    email: editedUser.email,
                    program: editedUser.program,
                    locationId: Number(editedUser.locationId),
                    collegeId: Number(editedUser.collegeId),
                })
                .then(response => {
                    console.log(response.data.message);
                })
                .catch(error => {
                    if (error.response) {
                        console.error(error.response.data.message);
                    } else {
                        console.error(error);
                    }
                });

            // Only saving after successful submission
            setEditedUser({
                username: "",
                email: "",
                program: "",
                locationId: "",
                collegeId: "",
            });

        }
        catch (error) {
                console.error("Failed to save:", error);
                alert("Fail to save, please try again");
        }
    }
    
    function inputEditedUser(e, field){
        setEditedUser(prev => ({
        ...prev,
        [field]:e.target.value,
        }))
    }

    return (
        <div>
            <div>
                <input 
                    type="text"
                    placeholder='user id'
                    onChange={e => setTargetUser(e.target.value)} 
                />

                <input 
                    type="text"
                    placeholder='username'
                    onChange={(e) => inputEditedUser(e, "username")} 
                />

                <input 
                    type="text"
                    placeholder='email'
                    onChange={(e) => inputEditedUser(e, "email")} 
                />

                <input 
                    type="text"
                    placeholder='program'
                    onChange={(e) => inputEditedUser(e, "program")} 
                />

                <input 
                    type="text"
                    placeholder='locationId'
                    onChange={(e) => inputEditedUser(e, "locationId")} 
                />

                <input 
                    type="text"
                    placeholder='collegeId'
                    onChange={(e) => inputEditedUser(e, "collegeId")} 
                />
            </div>

            <button type="button" onClick={()=> updateUser(targetUser)}>Update User</button>
        </div>
    );
}

export default UpdateUser;