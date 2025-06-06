import { useState } from "react";
import axios from 'axios';

function SearchUser(){
    const [user, setUser] = useState({
        username: "",
        email: "",
        program: "",
        locationId: "",
        collegeId: "",
    });
    const [userId, setUserId] = useState();

    function searchUser(id) {
        axios.get(`http://localhost:8080/api/users/${id}`)
            .then((response) => {
                if (response.data.code === 0){
                    setUser(response.data.data);
                }
                else {
                    console.log(response.data.message);
                }
            })
            .catch((error) => {
                if (error.response) {
                    console.error(error.response.status);
                    console.error(error.response.data);
                } else if (error.request) {
                // No response after request
                console.error('No response', error.request);
                } else {
                // Failed to request
                console.error('Failed to request', error.message);
                }
            });
    }

    return (
        <div>
            <h1>Search User</h1>
            <input 
                type="text"
                placeholder='userid want to search'
                onChange={e => setUserId(e.target.value)} 
            />
            <button type="button" onClick={()=> searchUser(userId)}>Search User</button>

            <h1>User Info</h1>
            <ul>
                <p>{user.id}</p>
                <p>{user.name}</p>
                <p>{user.program}</p>
            </ul>
        </div>
    );
}

export default SearchUser;