import { useState } from "react";
import axios from 'axios';

function CreateUser(){
    const[newUser, setNewUser] = useState({
        username: "",
        email: "",
        program: "",
        locationId: "",
        collegeId: "",
    });

    async function createUser(){
        try {
            await axios.post('http://localhost:8080/api/users', {
                username: newUser.username,
                email: newUser.email,
                program: newUser.program,
                locationId: Number(newUser.locationId),
                collegeId: Number(newUser.collegeId),
            });

            // Only saving after successful submission
            setNewUser({
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
    
    function inputNewUser(e, field){
        setNewUser(prev => ({
        ...prev,
        [field]:e.target.value,
        }))
    }

    return (
        <div>
            <h1>Create User</h1>
            <div>
                <input 
                    type="text"
                    placeholder='username'
                    onChange={(e) => inputNewUser(e, "username")} 
                />

                <input 
                    type="text"
                    placeholder='email'
                    onChange={(e) => inputNewUser(e, "email")} 
                />

                <input 
                    type="text"
                    placeholder='program'
                    onChange={(e) => inputNewUser(e, "email")} 
                />

                <input 
                    type="text"
                    placeholder='locationId'
                    onChange={(e) => inputNewUser(e, "locationId")} 
                />

                <input 
                    type="text"
                    placeholder='collegeId'
                    onChange={(e) => inputNewUser(e, "collegeId")} 
                />
            </div>
            <button type="button" onClick={createUser}>Create User</button>
        </div>
    );
}

export default CreateUser;