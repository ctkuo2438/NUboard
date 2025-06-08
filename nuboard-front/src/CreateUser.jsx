import { useState } from "react";
import axios from 'axios';

function CreateUser(){
    const[newUser, setNewUser] = useState({
        username: "",
        email: "",
        program: "",
        locationId: null,
        collegeId: null,
    });

    async function createUser(){
        console.log('newUser:', newUser);
        // 基本的数据验证
        if (!newUser.username || !newUser.email || !newUser.program || !newUser.locationId || !newUser.collegeId) {
            alert("请填写所有必填字段");
            return;
        }

        try {
            await axios.post('http://localhost:8080/api/users', {
                username: newUser.username,
                email: newUser.email,
                program: newUser.program,
                locationId: Number(newUser.locationId),
                collegeId: Number(newUser.collegeId),
            }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            // Only saving after successful submission
            setNewUser({
                username: "",
                email: "",
                program: "",
                locationId: null,
                collegeId: null,
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
                    value={newUser.username}
                    onChange={(e) => inputNewUser(e, "username")}
                />

                <input 
                    type="text"
                    placeholder='email'
                    value={newUser.email}
                    onChange={(e) => inputNewUser(e, "email")}
                />

                <input 
                    type="text"
                    placeholder='program'
                    value={newUser.program}
                    onChange={(e) => inputNewUser(e, "program")}
                />

                <input 
                    type="text"
                    placeholder='locationId'
                    value={newUser.locationId}
                    onChange={(e) => inputNewUser(e, "locationId")}
                />

                <input 
                    type="text"
                    placeholder='collegeId'
                    value={newUser.collegeId}
                    onChange={(e) => inputNewUser(e, "collegeId")}
                />
            </div>
            <button type="button" onClick={createUser}>Create User</button>
            {console.log("即将发送数据：", newUser)}
        </div>
    );
}

export default CreateUser;