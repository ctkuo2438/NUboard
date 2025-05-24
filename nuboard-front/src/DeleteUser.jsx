import { useState } from "react";
import axios from 'axios';

function DeleteUser(){
    const [userId, setUserId] = useState();

    function deleteUser(id){
        axios.delete(`http://localhost:8080/api/events/${id}`)
            .then(() => {
                console.log("delete successfully");
            })
            .catch((err) => {
                console.error("Fail to delete, please try again");
            });
    }

    return (
        <div>
            <h1>Delete User</h1>
            <input 
                type="text"
                placeholder='userid want to delete'
                onChange={() => setUserId(e.target.value)} 
            />
            <button type="button" onClick={()=> deleteUser(userId)}>Delete User</button>
        </div>
    );
}

export default DeleteUser;